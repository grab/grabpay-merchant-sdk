import os
import base64
import hashlib
import json
from RestClient import RestClient
from Config import Config


class MerchantIntegrationOnline:
    def __init__(
        self,
        env,
        country,
        partner_id,
        partner_secret,
        merchant_id,
        client_id,
        client_secret,
        redirect_uri,
    ):
        config = Config(
            env,
            country,
            partner_id,
            partner_secret,
            merchant_id,
            "",
            client_id,
            client_secret,
            redirect_uri,
        )
        self.config = config

    # 1 onaChargeInit Charge init the transaction
    def onaChargeInit(
        self,
        partnerTxID,
        partnerGroupTxID,
        amount,
        currency,
        description,
        metaInfo={},
        items={},
        shippingDetails={},
        hidePaymentMethods=[],
    ):
        try:
            requestBody = {
                "partnerTxID": partnerTxID,
                "partnerGroupTxID": partnerGroupTxID,
                "amount": int(amount),
                "currency": currency,
                "merchantID": self.config.merchantID,
                "description": description,
            }
            if hidePaymentMethods:
                requestBody = RestClient.merge_two_dicts(
                    requestBody, {"hidePaymentMethods": hidePaymentMethods}
                )
            if metaInfo:
                requestBody = RestClient.merge_two_dicts(
                    requestBody, {"metaInfo": metaInfo}
                )
            if items:
                requestBody = RestClient.merge_two_dicts(requestBody, {"items": items})
            if shippingDetails:
                requestBody = RestClient.merge_two_dicts(
                    requestBody, {"shippingDetails": shippingDetails}
                )

            path = self.config.pathOnaChargeInit
            return RestClient.post(self.config, path, requestBody, "ONLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 1 onaCreateWebUrl for make charg init and generated web url for end user
    def onaCreateWebUrl(
        self,
        partnerTxID,
        partnerGroupTxID,
        amount,
        currency,
        description,
        codeVerifier,
        metaInfo={},
        items={},
        shippingDetails={},
        hidePaymentMethods=[],
        state=None,
    ):
        try:
            resp = self.onaChargeInit(
                partnerTxID,
                partnerGroupTxID,
                amount,
                currency,
                description,
                metaInfo,
                items,
                shippingDetails,
                hidePaymentMethods,
            )
            # generated web url for desktop and mobile browser onA
            if resp["status"] == 200:
                data = json.loads(resp["data"])
                if self.config.country == "VN":
                    scope = "payment.vn.one_time_charge"
                else:
                    scope = "openid+payment.one_time_charge"
                codeChallenge = RestClient.base64URLEncode(
                    base64.b64encode(
                        hashlib.sha256(
                            RestClient.base64URLEncode(codeVerifier).encode("utf-8"),
                            usedforsecurity=True,
                        ).digest()
                    ).decode("utf-8")
                )
                return (
                    self.config.url
                    + "/grabid/v1/oauth2/authorize?acr_values=consent_ctx%3AcountryCode%3D"
                    + self.config.country
                    + ",currency%3D"
                    + currency
                    + "&client_id="
                    + self.config.clientID
                    + "&code_challenge="
                    + codeChallenge
                    + "&code_challenge_method=S256&nonce="
                    + RestClient.randomString(16)
                    + "&redirect_uri="
                    + self.config.redirectUri
                    + "&request="
                    + data["request"]
                    + "&response_type=code&scope="
                    + scope
                    + "&state="
                    + state
                )
            else:
                return resp
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 2. onaOAuthToken to get access_token
    def onaOAuthToken(self, code, code_verifier):
        try:
            requestBody = {
                "grant_type": "authorization_code",
                "client_id": self.config.clientID,
                "client_secret": self.config.clientSecret,
                "code_verifier": code_verifier,
                "redirect_uri": self.config.redirectUri,
                "code": code,
            }
            path = self.config.pathOnaOAuth2Token
            return RestClient.post(self.config, path, requestBody, "ONLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 3. onaChargeComplete to finished transaction
    def onaChargeComplete(self, partnerTxID, accessToken):
        try:
            requestBody = {"partnerTxID": partnerTxID}
            path = self.config.pathOnaChargeComplete
            return RestClient.post(
                self.config, path, requestBody, "ONLINE", accessToken
            )
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 4. onaGetChargeStatus to check status end of transaction
    def onaGetChargeStatus(self, partnerTxID, currency, accessToken):
        try:
            requestBody = {"partnerTxID": partnerTxID}
            path = self.config.pathOnaChargeStatus.replace(
                "{partnerTxID}", partnerTxID
            ).replace("{currency}", currency)
            return RestClient.get(self.config, path, "", "ONLINE", accessToken)
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 5. onaRefundTxn to refund transaction
    def onaRefund(
        self,
        refundPartnerTxID,
        partnerGroupTxID,
        amount,
        currency,
        txID,
        description,
        accessToken,
    ):
        try:
            requestBody = {
                "partnerTxID": refundPartnerTxID,
                "partnerGroupTxID": partnerGroupTxID,
                "amount": int(amount),
                "currency": currency,
                "merchantID": self.config.merchantID,
                "description": description,
                "originTxID": txID,
            }
            path = self.config.pathOnaRefund
            return RestClient.post(
                self.config, path, requestBody, "ONLINE", accessToken
            )
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 6. onaGetRefundStatus to check status end of transaction
    def onaGetRefundStatus(self, refundPartnerTxID, currency, accessToken):
        try:
            path = self.config.pathOnaGetRefundStatus.replace(
                "{refundPartnerTxID}", refundPartnerTxID
            ).replace("{currency}", currency)
            return RestClient.get(
                self.config, path, "application/json", "ONLINE", accessToken
            )
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 7. onaGetOtcStatus to get OAuthCode
    def onaGetOtcStatus(self, partnerTxID, currency):
        try:
            path = self.config.pathOnaOTCStatus.replace(
                "{partnerTxID}", partnerTxID
            ).replace("{currency}", currency)
            return RestClient.get(self.config, path, "application/json", "ONLINE", "")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))
