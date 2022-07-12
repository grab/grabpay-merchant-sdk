import os
import base64
import hashlib
import json
from datetime import datetime
import unittest
from unittest.mock import Mock, patch
from RestClient import RestClient
from Config import Config
import http.client

config = Config(
    "STG",
    "VN",
    "partnerID",
    "partnerSecret",
    "merchantID",
    "terminalID",
    "clientID",
    "clientSecret",
    "http://localhost:8888/result",
)


class TestRestClient(unittest.TestCase):
    def test_sha256(self):
        result = RestClient.sha256("abc")
        self.assertEqual(
            result, "ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=", "Should be the same"
        )

    def test_base64URLEncode(self):
        result = RestClient.base64URLEncode("abc")
        self.assertEqual(result, "abc", "Should be the same")

        result = RestClient.base64URLEncode("+++")
        self.assertEqual(result, "---", "Should be the same")

    def test_randomString(self):
        result = RestClient.randomString(5)
        self.assertEqual(len(result), 5, "Length should be 5")

        result = RestClient.randomString(0)
        self.assertEqual(len(result), 0, "Length should be 0")

        result = RestClient.randomString(1000)
        self.assertEqual(len(result), 1000, "Length should be 1000")

    def test_merge_two_dicts(self):
        result = RestClient.merge_two_dicts({"a": 1}, {"b": 2})
        self.assertEqual(len(result), 2, "Length should be 2")

        result = RestClient.merge_two_dicts({"a": 1}, {})
        self.assertEqual(len(result), 1, "Length should be 1")

        result = RestClient.merge_two_dicts({}, {})
        self.assertEqual(len(result), 0, "Length should be 0")

    def test_fmt_date(self):
        timestamp = datetime.now().timestamp()
        result = RestClient.fmt_date(timestamp)
        self.assertEqual(len(result), 29, "length of now should be 29")

    def test_generateHmac(self):
        result = RestClient.generateHmac(
            config, "GET", "/some-path", "", {}, "Sun, 07 Nov 2021 03:59:44 GMT"
        )
        self.assertEqual(
            result, "0kuK7qEF1XZC9p9OHMio/fxdo8n2mdKQntIfQTlq6sU=", "Should be equal"
        )
        self.assertEqual(len(result), 44, "Length should be 44")

    def test_generatePOPSign(self):
        result = RestClient.generatePOPSign(
            config, "some-access-token", 1636258588.112487
        )
        self.assertEqual(
            result,
            "eyJ0aW1lX3NpbmNlX2Vwb2NoIjogMTYzNjI1ODU4OCwgInNpZyI6ICJpS1ZBRHBtZ09DRDdPOHJtd0o3OWR5elpZOFlsQWF6UjBfSjBWRmtILVZZIn0",
            "Should be something else",
        )

    def test_http_build_query(self):
        bodyTest = {
            "action": "add",
            "controller": "invoice",
            "code": "debtor",
        }
        result = RestClient.http_build_query(bodyTest)
        self.assertEqual(
            result,
            "action=add&controller=invoice&code=debtor",
            "Result should be action=add&controller=invoice&code=debtor",
        )

    def test_prepareRequest(self):
        # case 1 test get
        partnerTxID = hashlib.md5(
            RestClient.randomString(32).encode("utf-8")
        ).hexdigest()
        msgID = hashlib.md5(RestClient.randomString(32).encode("utf-8")).hexdigest()
        requestCreateQrCode = {
            "amount": 1000,
            "currency": "VND",
            "partnerTxID": partnerTxID,
        }
        resultGet = RestClient.prepareRequest(
            config,
            "GET",
            config.pathPosCreateQRCode,
            "application/json",
            requestCreateQrCode,
            "OFFLINE",
            "",
        )
        resultGet = json.loads(json.dumps(resultGet))
        if resultGet:
            assert (
                resultGet["headers"]["Authorization"].find(config.partnerID) != -1
            ), "It need to have partnerID in header"
            assert resultGet["body"] == "", "Body should be null"
            assert len(resultGet["headers"]) == 8, "Should be 8"
            result = (
                "/mocapay/partners/v1/terminal/qrcode/create?grabID="
                + config.merchantID
                + "&terminalID="
                + config.terminalID
            )
            assert resultGet["apiUrl"] == result, "Should be " + result

        # case 2 test put
        requestBodyCancel = {
            "msgID": msgID,
            "grabID": "84dfaba5-7a1b-4e91-aa1c-f7ef93895266",
            "terminalID": "terminal25-meow-meow-meow",
            "currency": "VND",
            "origTxID": "partner-xi-1",
            "partnerTxID": partnerTxID,
        }
        resultPut = RestClient.prepareRequest(
            config,
            "PUT",
            config.pathPosCancel,
            "application/json",
            requestBodyCancel,
            "OFFLINE",
            "",
        )
        if resultPut:
            assert (
                resultPut["headers"]["Authorization"].find(config.partnerID) != -1
            ), "It need to have partnerID in header"
            assert len(resultGet["headers"]) == 8, "Length should be 8"

        # case 3 test post
        requestChargeInit = {
            "partnerTxID": partnerTxID,
            "partnerGroupTxID": partnerTxID,
            "amount": 4000,
            "currency": "VND",
            "merchantID": "0a46279c-c38c-480b-9fda-1466a5700445",
            "description": "testing otc",
            "metaInfo": {"brandName": ""},
        }
        resultPost = RestClient.prepareRequest(
            config,
            "POST",
            config.pathPosCancel,
            "application/json",
            requestChargeInit,
            "OFFLINE",
            "",
        )
        if resultPost:
            assert len(resultPost["headers"]) == 9, "Length should be 9"

        # case 4 test post
        requestChargeComplete = {"partnerTxID": partnerTxID, "accessToken": "fake"}
        resultPostChargeComplete = RestClient.prepareRequest(
            config,
            "POST",
            config.pathOnaChargeComplete,
            "application/json",
            requestChargeComplete,
            "ONLINE",
            "fake",
        )

        if resultPostChargeComplete:
            assert len(resultPostChargeComplete["headers"]) == 10, "Length should be 10"

        # case 5 test oauth token
        requestOAuth = {"partnerTxID": partnerTxID, "code": "fake"}
        requestOAuth = RestClient.prepareRequest(
            config,
            "POST",
            config.pathOnaOAuth2Token,
            "application/json",
            requestOAuth,
            "ONLINE",
            "",
        )
        self.assertTrue(requestOAuth)
        self.assertEqual(len(requestOAuth["headers"]), 7, "Length should be 7")

    def prepare_http_mock(self, mock):
        the_response = Mock(spec=http.client.HTTPResponse)
        the_response.status = 200
        read_bytes = the_response.read.return_value
        http_res_result_str = '{"result":"ok"}'
        read_bytes.decode.return_value = http_res_result_str
        instance = mock.return_value
        instance.getresponse.return_value = the_response

        return http_res_result_str, instance, the_response

    @patch("http.client.HTTPSConnection")
    def test_get(self, mock):
        http_res_result_str, instance, the_response = self.prepare_http_mock(mock)

        result = RestClient.get(
            config, config.pathPosCreateQRCode, "application/json", "OFFLINE"
        )
        instance.request.assert_called()
        instance.getresponse.assert_called()
        self.assertTrue(result)
        self.assertEqual(result["status"], 200, "Status should be 200")
        self.assertEqual(result["data"], json.loads(http_res_result_str))

    @patch("http.client.HTTPSConnection")
    def test_post(self, mock):
        http_res_result_str, instance, the_response = self.prepare_http_mock(mock)

        requestChargeInit = {
            "partnerTxID": hashlib.md5(
                RestClient.randomString(32).encode("utf-8")
            ).hexdigest(),
            "partnerGroupTxID": hashlib.md5(
                RestClient.randomString(32).encode("utf-8")
            ).hexdigest(),
            "amount": 4000,
            "currency": "VND",
            "merchantID": "0a46279c-c38c-480b-9fda-1466a5700445",
            "description": "testing otc",
            "metaInfo": {"brandName": ""},
        }
        result = RestClient.post(
            config,
            config.pathOnaChargeInit,
            requestChargeInit,
            "application/json",
            "ONLINE",
        )
        instance.request.assert_called()
        instance.getresponse.assert_called()
        self.assertTrue(result)
        self.assertEqual(result["status"], 200, "Status should be 200")
        self.assertEqual(result["data"], json.loads(http_res_result_str))

    @patch("http.client.HTTPSConnection")
    def test_put(self, mock):
        http_res_result_str, instance, the_response = self.prepare_http_mock(mock)
        the_response.status = 400

        bodyCancel = {
            "msgID": hashlib.md5(
                RestClient.randomString(32).encode("utf-8")
            ).hexdigest(),
            "grabID": "84df1234-1234-1234-1234-f7ef93891234",
            "terminalID": "terminal25-meow-meow-meow",
            "currency": "VND",
            "origTxID": "partner-xi-1",
            "partnerTxID": hashlib.md5(
                RestClient.randomString(32).encode("utf-8")
            ).hexdigest(),
        }
        result = RestClient.put(
            config, config.pathPosCancel, bodyCancel, "application/json", "OFFLINE"
        )
        instance.request.assert_called()
        instance.getresponse.assert_called()
        self.assertTrue(result)
        self.assertEqual(
            result["status"],
            400,
            "Status should be 400 because the transaction is fake",
        )
        self.assertEqual(result["data"], json.loads(http_res_result_str))
