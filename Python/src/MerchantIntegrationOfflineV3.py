import os
import base64
import hashlib
import json
from Config import Config
from RestClientV3 import RestClientV3

class MerchantIntegrationOfflineV3:
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

    # 1. V3 Pos - PosInitateQR : generate QRcode for both MPQR and CPQR
    def PosInitate(self, transactionDetails, paymentMethod,POSDetails):
        try:
            msgID = RestClientV3.randomString(32)
            requestBody = {
                "transactionDetails": transactionDetails,
                "msgID": msgID,
            }
            if paymentMethod != None :
                requestBody["paymentMethod"] = paymentMethod
            if POSDetails != None:
                requestBody["POSDetails"] = POSDetails

            path = self.config.pathPosInitiateV3
            return RestClientV3.post(self.config, path, "application/json",requestBody)
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 2. V3 Pos - PosInquire : 
    def PosInquire(self, transactionDetails):
        try:
            msgID = RestClientV3.randomString(32)
            requestBody = {
                "transactionDetails": transactionDetails,
                "msgID": msgID,
            }

            path = self.config.pathPosInquireV3
            if "?" in path:
                path += "&"
            else:
                path += "?"
            params = RestClientV3.http_build_query(requestBody)
            params = params.replace("%5B",".")
            params = params.replace("%5D","")
            path += params
            
            return RestClientV3.get(self.config, path, "application/x-www-form-urlencoded","")
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))


    # 3. V3 Pos - PosCancel : Cancel transaction
    def PosCancel(self, transactionDetails):
        try:
            msgID = RestClientV3.randomString(32)
            requestBody = {
                "transactionDetails": transactionDetails,
                "msgID": msgID,
        
            }
            path = self.config.pathPosCancelV3
            return RestClientV3.put(self.config, path,"application/json", requestBody)
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))

    # 4. V3 Pos - PosRefund : Refund transaction
    def PosRefund(self, transactionDetails):
        try:
            msgID = RestClientV3.randomString(32)
            requestBody = {
                "transactionDetails": transactionDetails,
                "msgID": msgID,
            }
            path = self.config.pathPosRefundV3
            return RestClientV3.put(self.config, path,"application/json", requestBody)
        except OSError as err:
            print("Something else went wrong with message:{0}".format(err))


    