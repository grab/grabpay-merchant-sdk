package com.merchantsdk.payment;

import com.merchantsdk.payment.service.OnlineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.json.JSONObject;

public class MerchantIntegrationOnline extends MerchantIntegration {

    private final OnlineTransaction onlineTransaction;

    public MerchantIntegrationOnline(String environment, String country, String partner_id, String partner_secret,
            String merchant_id, String client_id, String client_secret, String redirect_url) {
        super(environment, country, partner_id, partner_secret, merchant_id, "", client_id, client_secret,
                redirect_url);
        this.onlineTransaction = getOnlineTransaction();
    }

    public JSONObject onaChargeInit(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String description, JSONObject metaInfo, JSONObject[] items, JSONObject shippingDetails,
            String[] hidePaymentMethods) {
        CloseableHttpResponse response = this.onlineTransaction.apiChargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, metaInfo, items, shippingDetails, hidePaymentMethods);
        return processResponse(response);
    }

    public String onaCreateWebUrl(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String codeVerifier, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails,
            String[] hidePaymentMethods, String state) {
        String response = this.onlineTransaction.apiGenerateWebUrl(partnerTxID, partnerGroupTxID, amount, currency,
                codeVerifier, description, metaInfo, items, shippingDetails, hidePaymentMethods, state);
        return response;
    }

    public JSONObject onaOAuth2Token(String partnerTxID, String code) {
        CloseableHttpResponse response = this.onlineTransaction.apiOAuthToken(partnerTxID, code);
        return processResponse(response);
    }

    public JSONObject onaChargeComplete(String partnerTxID, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.apiChargeComplete(partnerTxID, accessToken);
        return processResponse(response);
    }

    public JSONObject onaGetChargeStatus(String partnerTxID, String currency, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.apiGetChargeStatus(partnerTxID, currency, accessToken);
        return processResponse(response);
    }

    public JSONObject onaRefund(String refundPartnerTxID, String partnerGroupTxID, long amount, String currency,
            String txID, String description, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.apiRefundTxnOnA(refundPartnerTxID, partnerGroupTxID,
                amount, currency, txID, description, accessToken);
        return processResponse(response);
    }

    public JSONObject onaGetRefundStatus(String refundPartnerTxID, String currency, String accessToken) {
        CloseableHttpResponse response = this.onlineTransaction.apiGetRefundStatus(refundPartnerTxID, currency,
                accessToken);
        return processResponse(response);
    }

    public JSONObject onaGetOTCStatus(String partnerTxID, String currency) {
        CloseableHttpResponse response = this.onlineTransaction.apiGetOtcStatus(partnerTxID, currency);
        return processResponse(response);
    }
}
