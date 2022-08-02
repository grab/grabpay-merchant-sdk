import os
from datetime import datetime
import urllib
from wsgiref.handlers import format_date_time
import random
import string
import base64
import json
import hmac
import hashlib
import http.client


class RestClientV3:
    @staticmethod
    def sha256(data: string):
        return base64.b64encode(hashlib.sha256(data.encode("utf-8")).digest()).decode(
            "utf-8"
        )

    @staticmethod
    def base64URLEncode(data: string):
        return data.replace("=", "").replace("/", "_").replace("+", "-")

    @staticmethod
    def randomString(length: int):
        return "".join(
            random.choices(
                string.ascii_letters
                + string.ascii_uppercase
                + string.ascii_lowercase
                + string.digits,
                k=length,
            )
        )

    @staticmethod
    def merge_two_dicts(x, y):
        z = x.copy()  # start with keys and values of x
        z.update(y)  # modifies z with keys and values of y
        return z

    @staticmethod
    def fmt_date(timestamp):
        return format_date_time(timestamp)

    @staticmethod
    def generateHmac(config, requestMethod, apiUrl, contentType, requestBody, date):
        if requestBody != "":
            body = json.dumps(requestBody)
            hashedPayload = RestClientV3.sha256(body)
        else:
            hashedPayload = ""

        content = ""
        content += requestMethod
        content += "\n"
        content += contentType
        content += "\n"
        content += date
        content += "\n"
        content += apiUrl
        content += "\n"
        content += hashedPayload
        content += "\n"

        return base64.b64encode(
            hmac.new(
                bytes(config.partnerSecret.encode("utf-8")),
                bytes(content.encode("utf-8")),
                hashlib.sha256,
            ).digest()
        ).decode("utf-8")

    @staticmethod
    def generatePOPSign(config, accessToken, timestamp):
        timestampUnix = int(timestamp)
        message = str(timestampUnix) + accessToken
        signature = base64.b64encode(
            hmac.new(
                bytes(config.clientSecret.encode("utf-8")),
                bytes(message.encode("utf-8")),
                hashlib.sha256,
            ).digest()
        ).decode("utf-8")
        sub = RestClientV3.base64URLEncode(signature)

        payload = {"time_since_epoch": timestampUnix, "sig": sub}
        payloadBytes = json.dumps(payload)
        return RestClientV3.base64URLEncode(
            base64.b64encode(payloadBytes.encode("utf-8")).decode("utf-8")
        )

    @staticmethod
    def http_build_query(data):
        parents = list()
        pairs = dict()

        def renderKey(parents):
            depth, outStr = 0, ""
            for x in parents:
                s = "[%s]" if depth > 0 or isinstance(x, int) else "%s"
                outStr += s % str(x)
                depth += 1
            return outStr

        def r_urlencode(data):
            if isinstance(data, list) or isinstance(data, tuple):
                for i in range(len(data)):
                    parents.append(i)
                    r_urlencode(data[i])
                    parents.pop()
            elif isinstance(data, dict):
                for key, value in data.items():
                    parents.append(key)
                    r_urlencode(value)
                    parents.pop()
            else:
                pairs[renderKey(parents)] = str(data)

            return pairs

        return urllib.parse.urlencode(r_urlencode(data))

    # @staticmethod
    # def prepareRequest(
    #     config, requestMethod, apiUrl, contentType, requestBody, payType, access_token
    # ):
    #     timestamp = datetime.now().timestamp()
    #     nowfmt = RestClientV3.fmt_date(timestamp)
    #     headers = {
    #         "Accept": "application/json",
    #         "Content-Type": contentType,
    #         "X-Request-ID": hashlib.md5(
    #             RestClientV3.randomString(32).encode("utf-8")
    #         ).hexdigest(),
    #         "X-Sdk-Country": config.country,
    #         "X-Sdk-Version": config.sdkVersion,
    #         "X-Sdk-Language": "PYTHON",
    #         "X-Sdk-Signature": config.sdkSignature,
    #     }
    #     headers2 = {}
    #     # to set the header use for api get authtoken
    #     if apiUrl == config.pathOnaOAuth2Token:
    #         headers2 = {}
    #     # to set the header use for API online but not for charge init and OTC status
    #     elif (
    #         payType == "ONLINE"
    #         and apiUrl != config.pathOnaChargeInit
    #         and -1 != config.pathOnaOTCStatus.find("one-time-charge")
    #     ):
    #         headers2 = {
    #             "Date": nowfmt,
    #             "X-GID-AUX-POP": RestClientV3.generatePOPSign(
    #                 config, access_token, timestamp
    #             ),
    #             "Authorization": "Bearer " + access_token,
    #         }
    #     # default
    #     else:
    #         hmac = RestClientV3.generateHmac(
    #             config, requestMethod, apiUrl, contentType, requestBody, nowfmt
    #         )
    #         # set header specical for inquiry on POS
    #         if payType == "OFFLINE" and requestMethod == "GET":
    #             headers.pop("Accept")

    #         headers2 = {
    #             "Date": nowfmt,
    #             "Authorization": (config.partnerID + ":" + hmac),
    #         }

    #     headers = RestClientV3.merge_two_dicts(headers, headers2)

    #     requestBody = json.dumps(requestBody)
    #     if requestMethod == "GET":
    #         requestBody = ""

    #     result = {"headers": headers, "body": requestBody, "apiUrl": apiUrl}
    #     return result

    @staticmethod
    def sendRequest(
        config, requestMethod, apiUrl, contentType, requestBody
    ):
       
        headers1 = {
            "Accept": "application/json",
            "Content-Type": contentType,
            "X-Request-ID": hashlib.md5(
                RestClientV3.randomString(32).encode("utf-8")
            ).hexdigest(),
            "X-Sdk-Country": config.country,
            "X-Sdk-Version": config.sdkVersion,
            "X-Sdk-Language": "PYTHON",
            "X-Sdk-Signature": config.sdkSignature,
        }
        timestamp = datetime.now().timestamp()
        nowfmt = RestClientV3.fmt_date(timestamp)
        
        hmac = RestClientV3.generateHmac(config, requestMethod, apiUrl, contentType, requestBody, nowfmt)

        headers2 = {
            "Date" : nowfmt,
            "Authorization": (config.partnerID + ":" + hmac),
        }
        headers = RestClientV3.merge_two_dicts(headers1, headers2)

    
        requestBody = json.dumps(requestBody)
        if requestMethod == "GET":
            requestBody = ""

        connection = http.client.HTTPSConnection(config.url.replace("https://", ""))
        connection.request(requestMethod, apiUrl, requestBody, headers)
        response = connection.getresponse()

        result = {
            "status": response.status,
            "data": json.loads(response.read().decode("utf-8")),
        }
        connection.close()
        return result

    @staticmethod
    def get(config, apiUrl, contentType, request):
        return RestClientV3.sendRequest(
            config, "GET", apiUrl, contentType, ""
        )

    @staticmethod
    def post(config, apiUrl, contentType, requestBody):
        return RestClientV3.sendRequest(
            config,
            "POST",
            apiUrl,
            contentType,
            requestBody
        )

    @staticmethod
    def put(config, apiUrl, contentType, requestBody):
        return RestClientV3.sendRequest(
            config,
            "PUT",
            apiUrl,
            contentType,
            requestBody
        )