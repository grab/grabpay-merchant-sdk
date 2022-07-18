package com.merchantsdk.payment.service;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.OtcConfig;
import com.merchantsdk.payment.config.ApiUrl.ApiEndpoint;
import com.merchantsdk.payment.exception.SDKRuntimeException;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class OnlineTransaction {
    private OtcConfig config;
    private Requester requester;

    public OnlineTransaction(OtcConfig config) {
        this.requester = new Requester(config);
        this.config = config;
    }

    public CloseableHttpResponse chargeInit(String partnerTxID, String partnerGroupTxID, long amount,
            String currency, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails, String[] hidePaymentMethods) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerTxID", partnerTxID);
        requestBody.put("partnerGroupTxID", partnerGroupTxID);
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("merchantID", this.config.getMerchantId());
        requestBody.put("description", description);

        if (metaInfo != null)
            requestBody.put("metaInfo", metaInfo);
        if (items != null)
            requestBody.put("items", items);
        if (shippingDetails != null)
            requestBody.put("shippingDetails", shippingDetails);
        if (hidePaymentMethods != null && hidePaymentMethods.length > 0)
            requestBody.put("hidePaymentMethods", hidePaymentMethods);

        String path = this.config.getApiUrl(ApiEndpoint.ONA_CHARGE_INIT);
        CloseableHttpResponse response = requester.sendHmacRequest("POST", path,
                null, null, requestBody);

        return response;
    }

    public String generateWebUrl(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String codeVerifier, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails,
            String[] hidePaymentMethods, String state) throws SDKRuntimeException, IOException, ParseException {
        CloseableHttpResponse res = chargeInit(partnerTxID, partnerGroupTxID, amount, currency, description,
                metaInfo, items, shippingDetails, hidePaymentMethods);
        if (res.getCode() >= 400)
            return "Error! Code = " + res.getCode();
        HttpEntity responseEntity = res.getEntity();
        String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        if (responseString.isEmpty()) {
            throw new SDKRuntimeException("Invalid response from chargeInit");
        }
        JSONObject data = new JSONObject(responseString);
        String scope = "payment.vn.one_time_charge";
        if (!this.config.getCountry().equals(Country.VIETNAM)) {
            scope = "openid+payment.one_time_charge";
        }

        String path = "/grabid/v1/oauth2/authorize";
        String codeChallenge = Utils
                .base64URLEncode(Utils.sha256Base64(codeVerifier));
        String acr_values = "consent_ctx%3AcountryCode%3D" + this.config.getCountry() + ",currency%3D"
                + currency;
        String code_challenge_method = "S256";
        String nonce = Utils.getRandomString(16);
        String response_type = "code";
        String request = data.get("request").toString();
        if (state == null || state.isEmpty())
            state = Utils.getRandomString(7);
        String response = this.config.getBaseUrl()
                + path
                + "?acr_values=" + acr_values
                + "&client_id=" + this.config.getClientId()
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=" + code_challenge_method
                + "&nonce=" + nonce
                + "&redirect_uri=" + this.config.getRedirectUri()
                + "&request=" + request
                + "&response_type=" + response_type
                + "&scope=" + scope
                + "&state=" + state;
        return response;
    }

    public CloseableHttpResponse getOAuth2Token(String code, String codeVerifier) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("client_id", this.config.getClientId());
        requestBody.put("client_secret", this.config.getClientSecret());
        requestBody.put("code_verifier", codeVerifier);
        requestBody.put("redirect_uri", this.config.getRedirectUri());
        requestBody.put("code", code);

        String path = this.config.getApiUrl(ApiEndpoint.OAUTH_TOKEN);
        return this.requester.sendRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse chargeComplete(String partnerTxID, String accessToken) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerTxID", partnerTxID);
        String path = this.config.getApiUrl(ApiEndpoint.ONA_CHARGE_COMPLETE);
        return this.requester.sendRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse getChargeStatus(String partnerTxID, String currency, String accessToken) {
        String path = this.config.getApiUrl(ApiEndpoint.ONA_CHARGE_STATUS);
        path = path.replace("{partnerTxID}", partnerTxID);

        Map<String, String> query = new HashMap<String, String>();
        query.put("currency", currency);

        return this.requester.sendRequest("GET", path, query, null);
    }

    public CloseableHttpResponse refundTxn(String partnerTxID, String partnerGroupTxID, long amount,
            String currency, String originTxID, String description, String accessToken) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerTxID", partnerTxID);
        requestBody.put("partnerGroupTxID", partnerGroupTxID);
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("merchantID", this.config.getMerchantId());
        requestBody.put("description", description);
        requestBody.put("originTxID", originTxID);
        String path = this.config.getApiUrl(ApiEndpoint.ONA_REFUND);
        return this.requester.sendRequest("POST", path, null, null, requestBody);
    }

    public CloseableHttpResponse getRefundStatus(String partnerTxID, String currency, String accessToken) {
        String path = this.config.getApiUrl(ApiEndpoint.ONA_REFUND_STATUS);
        path = path.replace("{partnerTxID}", partnerTxID);

        Map<String, String> query = new HashMap<String, String>();
        query.put("currency", currency);
        return this.requester.sendRequest("GET", path, query, null);
    }

    public CloseableHttpResponse getOtcStatus(String partnerTxID, String currency) {
        String path = this.config.getApiUrl(ApiEndpoint.ONA_ONE_TIME_CHARGE_STATUS);
        path = path.replace("{partnerTxID}", partnerTxID);

        Map<String, String> query = new HashMap<String, String>();
        query.put("currency", currency);

        return this.requester.sendHmacRequest("GET", path, query, null);
    }
}
