package com.merchantsdk.payment.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

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

    public CloseableHttpResponse initiate(JSONObject requestBody) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_INITIATE);
        return this.requester.sendHmacRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse inquire(JSONObject requestBody) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_INQUIRY);
        Map<String, String> query = new HashMap<String, String>();
        JSONObject transactionDetails = requestBody.getJSONObject("transactionDetails");
        if (transactionDetails != null) {
            for (String key : transactionDetails.keySet()) {
                query.put(String.format("transactionDetails.%s", key), transactionDetails.get(key).toString());
            }
        }
        return this.requester.sendHmacRequest("GET", path, query, null);
    }

    public CloseableHttpResponse refund(JSONObject requestBody) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_REFUND);
        return this.requester.sendHmacRequest("PUT", path, null, null, requestBody);
    }

    public CloseableHttpResponse cancel(JSONObject requestBody) {
        String path = this.config.getApiUrl(ApiEndpoint.POS_CANCEL);
        return this.requester.sendHmacRequest("PUT", path, null, null, requestBody);
    }

}
