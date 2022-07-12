package com.merchantsdk.payment;

import com.merchantsdk.payment.service.OfflineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.json.JSONObject;

public class MerchantIntegrationOffline extends MerchantIntegration {

    private final OfflineTransaction offlineTransaction;

    public MerchantIntegrationOffline(String environment, String country, String partner_id, String partner_secret,
            String merchant_id, String terminal_id) {
        super(environment, country, partner_id, partner_secret, merchant_id, terminal_id, "", "", "");
        this.offlineTransaction = getOfflineTransaction();
    }

    public JSONObject posCreateQRCode(String msgID, String partnerTxID, long amount, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.apiCreateQrCode(msgID, partnerTxID, amount, currency);
        return processResponse(response);
    }

    public JSONObject posPerformQRCode(String msgID, String partnerTxID, long amount, String currency, String code) {
        CloseableHttpResponse response = this.offlineTransaction.apiPosPerformTxn(msgID, partnerTxID, amount, currency,
                code);
        return processResponse(response);
    }

    public JSONObject posCancel(String msgID, String partnerTxID, String origPartnerTxID, String origTxID,
            String currency) {
        CloseableHttpResponse response = this.offlineTransaction.apiPosCancel(msgID, partnerTxID, origPartnerTxID,
                origTxID, currency);
        return processResponse(response);
    }

    public JSONObject posRefund(String msgID, String refundPartnerTxID, long amount, String currency,
            String origPartnerTxID, String description) {
        CloseableHttpResponse response = this.offlineTransaction.apiPosRefund(msgID, refundPartnerTxID, amount,
                currency, origPartnerTxID, description);
        return processResponse(response);
    }

    public JSONObject posGetTxnDetails(String msgID, String partnerTxID, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.apiPosGetTxnStatus(msgID, partnerTxID, currency);
        return processResponse(response);
    }

    public JSONObject posGetRefundDetails(String msgID, String refundPartnerTxID, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.apiPosGetRefundStatus(msgID, refundPartnerTxID,
                currency);
        return processResponse(response);
    }

}
