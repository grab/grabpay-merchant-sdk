package com.merchantsdk.payment;

import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.OtcConfig;
import com.merchantsdk.payment.service.OnlineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.json.JSONObject;

public class MerchantIntegrationOnline extends MerchantIntegration {

    private final OnlineTransaction onlineTransaction;

    public MerchantIntegrationOnline(EnvironmentType environment, Country country, String partnerId,
            String partnerSecret,
            String merchantId, String clientId, String clientSecret, String redirectUrl) {
        super(new OtcConfig(environment, country, partnerId, partnerSecret, merchantId, clientId, clientSecret,
                redirectUrl));
        this.onlineTransaction = new OnlineTransaction((OtcConfig) this.getConfig());
    }

    public JSONObject chargeInit(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String description, JSONObject metaInfo, JSONObject[] items, JSONObject shippingDetails,
            String[] hidePaymentMethods) {
        CloseableHttpResponse response = this.onlineTransaction.chargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, metaInfo, items, shippingDetails, hidePaymentMethods);
        return processResponse(response);
    }

    public String generateWebUrl(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String codeVerifier, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails,
            String[] hidePaymentMethods, String state) throws Exception {
        String response = this.onlineTransaction.generateWebUrl(partnerTxID, partnerGroupTxID, amount, currency,
                codeVerifier, description, metaInfo, items, shippingDetails, hidePaymentMethods, state);
        return response;
    }

    public JSONObject getOAuth2Token(String code, String codeVerifier) {
        CloseableHttpResponse response = this.onlineTransaction.getOAuth2Token(code, codeVerifier);
        return processResponse(response);
    }

    public JSONObject chargeComplete(String partnerTxID, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.chargeComplete(partnerTxID, accessToken);
        return processResponse(response);
    }

    public JSONObject getChargeStatus(String partnerTxID, String currency, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.getChargeStatus(partnerTxID, currency, accessToken);
        return processResponse(response);
    }

    public JSONObject refund(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String txID, String description, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.refundTxn(partnerTxID, partnerGroupTxID,
                amount, currency, txID, description, accessToken);
        return processResponse(response);
    }

    public JSONObject getRefundStatus(String partnerTxID, String currency, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.getRefundStatus(partnerTxID, currency,
                accessToken);
        return processResponse(response);
    }

    public JSONObject getOtcStatus(String partnerTxID, String currency) {
        CloseableHttpResponse response = this.onlineTransaction.getOtcStatus(partnerTxID, currency);
        return processResponse(response);
    }
}
