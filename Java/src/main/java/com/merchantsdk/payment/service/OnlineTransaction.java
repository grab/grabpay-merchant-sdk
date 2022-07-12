package com.merchantsdk.payment.service;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.PathUtility;
import com.merchantsdk.payment.exception.SDKRuntimeException;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OnlineTransaction {
    private static final String type = "ONLINE";
    private Config config;
    private Transaction transaction;
    private AuthorizationService authorizationService;
    private Map<PathUtility, String> countryURL;

    public OnlineTransaction() {
        this.authorizationService = new AuthorizationService();
    }

    public OnlineTransaction(Config config, Map<PathUtility, String> countryURL) {
        this();
        this.config = config;
        this.countryURL = countryURL;
        this.transaction = new Transaction();
    }

    public CloseableHttpResponse apiChargeInit(String partnerTxID, String partnerGroupTxID, long amount,
            String currency, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails, String[] hidePaymentMethods) throws SDKRuntimeException {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("partnerTxID", partnerTxID);
            requestBody.put("partnerGroupTxID", partnerGroupTxID);
            requestBody.put("amount", amount);
            requestBody.put("currency", currency);
            requestBody.put("merchantID", this.config.getMerchant_id());
            requestBody.put("description", description);

            if (metaInfo != null)
                requestBody.put("metaInfo", metaInfo);
            if (items != null)
                requestBody.put("items", items);
            if (shippingDetails != null)
                requestBody.put("shippingDetails", shippingDetails);
            if (hidePaymentMethods != null && hidePaymentMethods.length > 0)
                requestBody.put("hidePaymentMethods", hidePaymentMethods);

            String path = this.countryURL.get(PathUtility.PATH_ONA_CHARGE_INIT);
            CloseableHttpResponse response = transaction.sendRequest(this.config, "POST", path, "application/json",
                    requestBody, null, type, null);

            return response;
        } catch (Exception e) {
            throw new SDKRuntimeException("Error when init new charge : " + e.getMessage());
        }
    }

    public String apiGenerateWebUrl(String partnerTxID, String partnerGroupTxID, long amount, String currency,
            String codeVerifier, String description, JSONObject metaInfo, JSONObject[] items,
            JSONObject shippingDetails,
            String[] hidePaymentMethods, String state) {
        CloseableHttpResponse res = apiChargeInit(partnerTxID, partnerGroupTxID, amount, currency, description,
                metaInfo, items, shippingDetails, hidePaymentMethods);
        if (res.getCode() >= 400)
            return "Error! Code = " + res.getCode();
        HttpEntity responseEntity = res.getEntity();
        try {
            String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            String response = "";
            if (!responseString.isEmpty()) {
                JSONObject data = new JSONObject(responseString);
                String scope = "payment.vn.one_time_charge";
                if (!this.config.getCountry().equals("VN")) {
                    scope = "openid+payment.one_time_charge";
                }

                String path = "/grabid/v1/oauth2/authorize";
                String codeChallenge = this.authorizationService
                        .base64URLEncode(this.authorizationService
                                ._sha265(this.authorizationService.base64URLEncode(codeVerifier)));
                String acr_values = "consent_ctx%3AcountryCode%3D" + this.config.getCountry() + ",currency%3D"
                        + currency;
                String code_challenge_method = "S256";
                String nonce = this.authorizationService.getRandomString(16);
                String response_type = "code";
                String request = data.get("request").toString();
                if (state == null || state.isEmpty())
                    state = this.authorizationService.getRandomString(7);
                response = this.config.getUrl()
                        + path
                        + "?acr_values=" + acr_values
                        + "&client_id=" + this.config.getClient_id()
                        + "&code_challenge=" + codeChallenge
                        + "&code_challenge_method=" + code_challenge_method
                        + "&nonce=" + nonce
                        + "&redirect_uri=" + this.config.getRedirect_uri()
                        + "&request=" + request
                        + "&response_type=" + response_type
                        + "&scope=" + scope
                        + "&state=" + state;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public CloseableHttpResponse apiOAuthToken(String code, String codeVerifier) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("grant_type", "authorization_code");
            requestBody.put("client_id", this.config.getClient_id());
            requestBody.put("client_secret", this.config.getClient_secret());
            requestBody.put("code_verifier", this.authorizationService.base64URLEncode(codeVerifier));
            requestBody.put("redirect_uri", this.config.getRedirect_uri());
            requestBody.put("code", code);

            String path = this.countryURL.get(PathUtility.PATH_OAUTH_TOKEN);
            CloseableHttpResponse response = this.transaction.sendRequest(this.config, "POST", path, "application/json",
                    requestBody, null, type, null);
            return response;
        } catch (Exception e) {
            throw new SDKRuntimeException("Error when get OAuth token : " + e.getMessage());
        }
    }

    public CloseableHttpResponse apiChargeComplete(String partnerTxID, String accessToken) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerTxID", partnerTxID);
        String path = this.countryURL.get(PathUtility.PATH_ONA_CHARGE_COMPLETE);
        CloseableHttpResponse response = this.transaction.sendRequest(this.config, "POST", path, "application/json",
                requestBody, accessToken, type, null);
        return response;
    }

    public CloseableHttpResponse apiGetChargeStatus(String partnerTxID, String currency, String accessToken) {
        String path = this.countryURL.get(PathUtility.PATH_ONA_CHARGE_STATUS);
        path = path.replace("{partnerTxID}", partnerTxID).replace("{currency}", currency);
        CloseableHttpResponse response = this.transaction.sendRequest(this.config, "GET", path, "application/json",
                null, accessToken, type, null);
        return response;
    }

    public CloseableHttpResponse apiRefundTxnOnA(String refundPartnerTxID, String partnerGroupTxID, long amount,
            String currency, String originTxID, String description, String accessToken) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("partnerTxID", refundPartnerTxID);
        requestBody.put("partnerGroupTxID", partnerGroupTxID);
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("merchantID", this.config.getMerchant_id());
        requestBody.put("description", description);
        requestBody.put("originTxID", originTxID);
        String path = this.countryURL.get(PathUtility.PATH_ONA_REFUND);
        CloseableHttpResponse response = this.transaction.sendRequest(this.config, "POST", path, "application/json",
                requestBody, accessToken, type, null);
        return response;
    }

    public CloseableHttpResponse apiGetRefundStatus(String refundPartnerTxID, String currency, String accessToken) {
        String path = this.countryURL.get(PathUtility.PATH_ONA_REFUND_STATUS);
        path = path.replace("{refundPartnerTxID}", refundPartnerTxID).replace("{currency}", currency);
        CloseableHttpResponse response = this.transaction.sendRequest(this.config, "GET", path, "application/json",
                null, accessToken, type, null);
        return response;
    }

    public CloseableHttpResponse apiGetOtcStatus(String partnerTxID, String currency) {
        String path = this.countryURL.get(PathUtility.PATH_ONA_ONE_TIME_CHARGE_STATUS);
        path = path.replace("{partnerTxID}", partnerTxID).replace("{currency}", currency);
        CloseableHttpResponse response = this.transaction.sendRequest(this.config, "GET", path, "application/json",
                null, null, type, null);
        return response;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }
}
