from MerchantIntegrationOfflineV3 import MerchantIntegrationOfflineV3
from RestClientV3 import RestClientV3
import os
# from datetime import datetime
import urllib
from wsgiref.handlers import format_date_time
import random
import string
import base64
import json
import hmac
import hashlib
import http.client
import datetime

# Sample call API charge init to get QR offline.
ENV = 'STG'
COUNTRY = 'SG'
CURRENCY = 'SGD'
PARTNER_ID = os.getenv("SG_STG_POS_PARTNER_ID")
PARTNER_SECRET = os.getenv("SG_STG_POS_PARTNER_SECRET")
MERCHANT_ID = os.getenv("SG_STG_POS_MERCHANT_ID")
TERMINAL_ID = os.getenv("SG_STG_POS_TERMINAL_ID")

call = MerchantIntegrationOfflineV3(ENV, COUNTRY, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, TERMINAL_ID)
partnerTxID = hashlib.md5(RestClientV3.randomString(32).encode('utf-8')).hexdigest()
msgID = hashlib.md5(RestClientV3.randomString(32).encode('utf-8')).hexdigest() 

amount = 120

time_change = datetime.timedelta(seconds=600)
timestamp = datetime.datetime.now() + time_change
timestamp = int(timestamp.timestamp())
print(partnerTxID)
transactionDetails = {
        "paymentChannel": "MPQR",
        "storeGrabID": MERCHANT_ID,
        "partnerTxID": partnerTxID,
        "partnerGroupTxID": partnerTxID,
        "amount": amount,
        "currency": "SGD",
        "paymentExpiryTime": timestamp
    }
POSDetails = {
    "terminalID": TERMINAL_ID
}
respQRInit = call.PosInitate(transactionDetails, None, POSDetails)
print(respQRInit)


inquireTransactionDetails = {
        "paymentChannel": "MPQR",
        "storeGrabID": MERCHANT_ID,
        "txType": "PAYMENT",
        "txRefType": "PARTNERTXID",
        "amount": amount,
        "currency": "SGD",
        "txRefID": partnerTxID
    }
respQRInquire = call.PosInquire(inquireTransactionDetails)
print(respQRInquire)

ready = input('Plz input yes when you already payment: ')
if (ready == 'yes'):
    refundTransactionDetails = {
        "paymentChannel": "MPQR",
        "storeGrabID": MERCHANT_ID,
        "originPartnerTxID": partnerTxID,
        "partnerTxID": hashlib.md5(RestClientV3.randomString(32).encode('utf-8')).hexdigest(),
        "partnerGroupTxID": hashlib.md5(RestClientV3.randomString(32).encode('utf-8')).hexdigest(),
        "currency": "SGD",
        "amount": amount
    }
    respRefund = call.PosRefund(refundTransactionDetails)
    print(respRefund)
else:
    cancelTransactionDetails = {
         "paymentChannel": "MPQR",
        "storeGrabID": MERCHANT_ID,
        "originPartnerTxID": partnerTxID,
        "currency": "SGD"
    }
    respCancel = call.PosCancel(cancelTransactionDetails)
    print(respCancel)

