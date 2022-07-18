package com.merchantsdk.payment;

import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.service.OfflineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.json.JSONObject;

public class MerchantIntegrationOffline extends MerchantIntegration {

    private final OfflineTransaction offlineTransaction;

    public MerchantIntegrationOffline(EnvironmentType env, Country country, String partnerId, String partnerSecret,
            String merchantId, String terminalId) {
        super(new PosConfig(env, country, partnerId, partnerSecret, merchantId, terminalId));
        this.offlineTransaction = new OfflineTransaction((PosConfig) this.getConfig());
    }

    public JSONObject createQrCode(String msgID, String partnerTxID, long amount, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.createQrCode(msgID, partnerTxID, amount, currency);
        return processResponse(response);
    }

    public JSONObject performQrCode(String msgID, String partnerTxID, long amount, String currency, String code) {
        CloseableHttpResponse response = this.offlineTransaction.performTxn(msgID, partnerTxID, amount, currency,
                code);
        return processResponse(response);
    }

    public JSONObject cancel(String msgID, String partnerTxID, String origPartnerTxID, String origTxID,
            String currency) {
        CloseableHttpResponse response = this.offlineTransaction.cancelTxn(msgID, partnerTxID, origPartnerTxID,
                origTxID, currency);
        return processResponse(response);
    }

    public JSONObject refund(String msgID, String partnerTxID, long amount, String currency,
            String origPartnerTxID, String description) {
        CloseableHttpResponse response = this.offlineTransaction.refundTxn(msgID, partnerTxID, amount,
                currency, origPartnerTxID, description);
        return processResponse(response);
    }

    public JSONObject getTxnDetails(String msgID, String partnerTxID, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.getTxnStatus(msgID, partnerTxID, currency);
        return processResponse(response);
    }

    public JSONObject getRefundDetails(String msgID, String partnerTxID, String currency) {
        CloseableHttpResponse response = this.offlineTransaction.getRefundStatus(msgID, partnerTxID,
                currency);
        return processResponse(response);
    }

}
