package com.merchantsdk.payment.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.config.ApiUrl.ApiEndpoint;

import org.json.JSONObject;

public class OfflineTransaction {
    private PosConfig config;
    private Requester requester;

    public OfflineTransaction(PosConfig config) {
        this.config = config;
        this.requester = new Requester(config);
    }

    private JSONObject configurePayload(JSONObject requestBody) {
        JSONObject additionalPayload = new JSONObject();
        additionalPayload.put("msgID", Utils.getRandomString(32));
        additionalPayload.put("grabID", config.getMerchantId());
        additionalPayload.put("terminalID", config.getTerminalId());
        return Utils.mergeJsonObjects(additionalPayload, requestBody);
    }

    /**
     * This method is used to create new QR code
     * 
     * @param
     * @return json object of response
     */
    public CloseableHttpResponse createQrCode(String msgID, String partnerTxID, long amount, String currency) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("partnerTxID", partnerTxID);
        if (msgID != null) {
            requestBody.put("msgID", msgID);
        }

        requestBody = configurePayload(requestBody);

        String path = this.config.getApiUrl(ApiEndpoint.POS_CREATE_QR_CODE);
        return this.requester.sendHmacRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse performTxn(String msgID, String partnerTxID, long amount, String currency,
            String code) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("partnerTxID", partnerTxID);
        requestBody.put("code", code);
        if (msgID != null) {
            requestBody.put("msgID", msgID);
        }

        requestBody = configurePayload(requestBody);

        String path = this.config.getApiUrl(ApiEndpoint.POS_PERFORM_TRANSACTION);
        return this.requester.sendHmacRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse cancelTxn(String msgID, String partnerTxID, String origPartnerTxID, String origTxID,
            String currency) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("currency", currency);
        requestBody.put("origTxID", origTxID);
        requestBody.put("partnerTxID", partnerTxID);
        if (msgID != null) {
            requestBody.put("msgID", msgID);
        }

        requestBody = configurePayload(requestBody);

        String path = this.config.getApiUrl(ApiEndpoint.POS_CANCEL_TRANSACTION);
        path = path.replace("{origPartnerTxID}", origPartnerTxID);
        return this.requester.sendHmacRequest("PUT", path, null, null, requestBody);
    }

    public CloseableHttpResponse refundTxn(String msgID, String partnerTxID, long amount, String currency,
            String origPartnerTxID, String description) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("currency", currency);
        requestBody.put("amount", amount);
        requestBody.put("reason", description);
        requestBody.put("partnerTxID", partnerTxID);
        if (msgID != null) {
            requestBody.put("msgID", msgID);
        }

        requestBody = configurePayload(requestBody);

        String path = this.config.getApiUrl(ApiEndpoint.POS_REFUND_TRANSACTION);
        path = path.replace("{origPartnerTxID}", origPartnerTxID);
        return this.requester.sendHmacRequest("PUT", path, null, null, requestBody);
    }

    public CloseableHttpResponse getTxnStatus(String msgID, String partnerTxID, String currency) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_TRANSACTION_DETAILS);
        path = path.replace("{partnerTxID}", partnerTxID);

        Map<String, String> query = new HashMap<String, String>();
        query.put("currency", currency);
        query.put("txType", "P2M");
        if (msgID == null) {
            query.put("msgID", Utils.getRandomString(32));
        } else {
            query.put("msgID", msgID);
        }

        return this.requester.sendHmacRequest("GET", path, query, null);
    }

    public CloseableHttpResponse getRefundStatus(String msgID, String partnerTxID, String currency) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_TRANSACTION_DETAILS);
        path = path.replace("{partnerTxID}", partnerTxID);

        Map<String, String> query = new HashMap<String, String>();
        query.put("currency", currency);
        query.put("txType", "Refund");
        if (msgID == null) {
            query.put("msgID", Utils.getRandomString(32));
        } else {
            query.put("msgID", msgID);
        }
        return this.requester.sendHmacRequest("GET", path, query, null);
    }
}
