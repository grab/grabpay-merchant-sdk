import os
import base64
import hashlib
import json
from RestClient import RestClient
from Config import Config


class MerchantIntegrationOffline:
    def __init__(
        self, env, country, partner_id, partner_secret, merchant_id, terminal_id
    ):
        config = Config(
            env,
            country,
            partner_id,
            partner_secret,
            merchant_id,
            terminal_id,
            "",
            "",
            "",
        )
        self.config = config

    # 1. posCreateQRCode to Create QR code for POS
    def posCreateQRCode(self, msgID, partnerTxID, amount, currency):
        try:
            requestBody = {
                "amount": int(amount),
                "currency": currency,
                "partnerTxID": partnerTxID,
                "msgID": msgID,
            }
            path = self.config.pathPosCreateQRCode
            return RestClient.post(self.config, path, requestBody, "OFFLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 2. posPerformQRCode use if method is CPQR
    def posPerformQRCode(self, msgID, partnerTxID, amount, currency, code):
        try:
            requestBody = {
                "amount": int(
                    amount
                ),  # todo (phuoc.nguyenthanh): should be float as other current accept float
                "currency": currency,
                "partnerTxID": partnerTxID,
                "msgID": msgID,
                "code": code,
            }
            path = self.config.pathPosPerformQRCode
            return RestClient.post(self.config, path, requestBody, "OFFLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 3. posCancel if user QR do not scan or expire
    def posCancel(self, msgID, partnerTxID, origPartnerTxID, origTxID, currency):
        try:
            requestBody = {
                "currency": currency,
                "origTxID": origTxID,
                "partnerTxID": partnerTxID,
                "msgID": msgID,
            }
            path = self.config.pathPosCancel.replace(
                "{origPartnerTxID}", origPartnerTxID
            )
            return RestClient.put(self.config, path, requestBody, "OFFLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 4. posRefund to refund transaction already success.
    def posRefund(
        self, msgID, refundPartnerTxID, amount, currency, origPartnerTxID, description
    ):
        try:
            requestBody = {
                "currency": currency,
                "amount": int(amount),
                "reason": description,
                "partnerTxID": refundPartnerTxID,
                "msgID": msgID,
            }
            path = self.config.pathPosRefund.replace(
                "{origPartnerTxID}", origPartnerTxID
            )
            return RestClient.put(self.config, path, requestBody, "OFFLINE")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 5. posGetTxnStatus :get status of txn use if method is CPQR
    def posGetTxnStatus(self, msgID, partnerTxID, currency):
        try:
            path = (
                self.config.pathPosTxnDetails.replace("{partnerTxID}", partnerTxID)
                .replace("{currency}", currency)
                .replace("{msgID}", msgID)
                .replace("{posTxType}", "P2M")
            )
            return RestClient.get(
                self.config, path, "application/x-www-form-urlencoded", "OFFLINE"
            )
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 6. posGetRefundStatus :get status of txn use if method is CPQR
    def posGetRefundStatus(self, msgID, refundPartnerTxID, currency):
        try:
            path = (
                self.config.pathPosTxnDetails.replace(
                    "{partnerTxID}", refundPartnerTxID
                )
                .replace("{currency}", currency)
                .replace("{msgID}", msgID)
                .replace("{posTxType}", "Refund")
            )
            return RestClient.get(
                self.config, path, "application/x-www-form-urlencoded", "OFFLINE"
            )
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    