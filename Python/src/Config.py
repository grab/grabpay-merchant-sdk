import json

VN_PRD_HOST = "https://partner-gw.moca.vn"
VN_STG_HOST = "https://stg-paysi.moca.vn"

REGIONAL_PRD_HOST = "https://partner-api.grab.com"
REGIONAL_STG_HOST = "https://partner-api.stg-myteksi.com"


class Config:
    def __init__(
        self,
        env,
        country,
        partner_id,
        partner_secret,
        merchant_id,
        terminal_id,
        client_id,
        client_secret,
        redirect_uri,
    ):
        self.environment = env
        self.country = country
        self.partnerID = partner_id
        self.partnerSecret = partner_secret
        self.merchantID = merchant_id
        self.terminalID = terminal_id
        self.clientID = client_id
        self.clientSecret = client_secret
        self.redirectUri = redirect_uri
        self.sdkVersion = "2.0.0"
        self.sdkSignature = (
            "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7"
        )

        is_vietnam = self.country and self.country.upper() == "VN"
        is_prod = self.environment and self.environment.upper() == "PRD"

        if is_prod:
            # This to get the which domain can runing on their country
            if is_vietnam:
                self.url = VN_PRD_HOST
            else:
                self.url = REGIONAL_PRD_HOST
        else:
            # This to get the which domain can runing on their country
            if is_vietnam:
                self.url = VN_STG_HOST
            else:
                self.url = REGIONAL_STG_HOST

        if is_vietnam:
            # online path
            self.pathOnaChargeInit = "/mocapay/partner/v2/charge/init"
            self.pathOnaOAuth2Token = "/grabid/v1/oauth2/token"
            self.pathOnaChargeComplete = "/mocapay/partner/v2/charge/complete"
            self.pathOnaChargeStatus = (
                "/mocapay/partner/v2/charge/{partnerTxID}/status?currency={currency}"
            )
            self.pathOnaRefund = "/mocapay/partner/v2/refund"
            self.pathOnaGetRefundStatus = "/mocapay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}"
            self.pathOnaOTCStatus = "/mocapay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}"
            # offline path
            self.pathPosCreateQRCode = "/mocapay/partners/v1/terminal/qrcode/create"
            self.pathPosPerformQRCode = (
                "/mocapay/partners/v1/terminal/transaction/perform"
            )
            self.pathPosCancel = (
                "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/cancel"
            )
            self.pathPosRefund = (
                "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/refund"
            )
            self.pathPosTxnDetails = "/mocapay/partners/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}"
        else:
            # online path
            self.pathOnaChargeInit = "/grabpay/partner/v2/charge/init"
            self.pathOnaOAuth2Token = "/grabid/v1/oauth2/token"
            self.pathOnaChargeComplete = "/grabpay/partner/v2/charge/complete"
            self.pathOnaChargeStatus = (
                "/grabpay/partner/v2/charge/{partnerTxID}/status?currency={currency}"
            )
            self.pathOnaRefund = "/grabpay/partner/v2/refund"
            self.pathOnaGetRefundStatus = "/grabpay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}"
            self.pathOnaOTCStatus = "/grabpay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}"
            # offline path
            self.pathPosCreateQRCode = "/grabpay/partner/v1/terminal/qrcode/create"
            self.pathPosPerformQRCode = (
                "/grabpay/partner/v1/terminal/transaction/perform"
            )
            self.pathPosCancel = (
                "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/cancel"
            )
            self.pathPosRefund = (
                "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/refund"
            )
            self.pathPosTxnDetails = "/grabpay/partner/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}"

    def getConfig(self):
        config = {
            "environment": self.environment,
            "country": self.country,
            "partnerID": self.partnerID,
            "partnerSecret": self.partnerSecret,
            "merchantID": self.merchantID,
            "terminalID": self.terminalID,
            "clientID": self.clientID,
            "clientSecret": self.clientSecret,
            "redirect_uri": self.redirectUri,
            "url": self.url,
            "sdkVersion": self.sdkVersion,
            "sdkSignature": self.sdkSignature,
            "pathOnaChargeInit": self.pathOnaChargeInit,
            "pathOnaOAuth2Token": self.pathOnaOAuth2Token,
            "pathOnaChargeComplete": self.pathOnaChargeComplete,
            "pathOnaChargeStatus": self.pathOnaChargeStatus,
            "pathOnaRefund": self.pathOnaRefund,
            "pathOnaGetRefundStatus": self.pathOnaGetRefundStatus,
            "pathOnaOTCStatus": self.pathOnaOTCStatus,
            "pathPosCreateQRCode": self.pathPosCreateQRCode,
            "pathPosPerformQRCode": self.pathPosPerformQRCode,
            "pathPosCancel": self.pathPosCancel,
            "pathPosRefund,           ": self.pathPosRefund,
            "pathPosTxnDetails": self.pathPosTxnDetails,
        }

        return config
