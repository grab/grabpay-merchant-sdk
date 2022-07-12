package com.merchantsdk.payment.service;

import com.merchantsdk.payment.config.ApiUrl;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.PathUtility;
import com.merchantsdk.payment.exception.SDKRuntimeException;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;

import java.time.Instant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Transaction {

    private AuthorizationService authorizationService;
    private CloseableHttpClient client;

    public Transaction() {
        this.authorizationService = new AuthorizationService();
        client = HttpClients.createDefault();
    }

    public CloseableHttpResponse sendRequest(Config config, String httpMethod, String path, String contentType,
            JSONObject requestBody, String accessToken, String type, String msgID) {
        HashMap<String, Object> requestParams = this.prepareRequest(authorizationService, config, httpMethod, path,
                contentType, requestBody, accessToken, type, msgID);
        CloseableHttpResponse response;
        try {
            Map<String, String> header = getHeaderFromRequest(requestParams);
            String body = getBodyFromRequest(requestParams);
            String url = getUrlFromRequest(requestParams);

            switch (httpMethod.toUpperCase()) {
                case "GET":
                    response = this.get(url, body, header, this.client);
                    break;
                case "POST":
                    response = this.post(url, body, header, this.client);
                    break;
                case "PUT":
                    response = this.put(url, body, header, this.client);
                    break;
                case "DELETE":
                    response = this.delete(url, body, header, this.client);
                    break;
                default:
                    throw new Exception("wrong http method");
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SDKRuntimeException("Error during sending request : " + e.getMessage());
        }
    }

    public HashMap<String, Object> prepareRequest(AuthorizationService authorizationService, Config config,
            String httpMethod, String path, String contentType, JSONObject requestBody, String accessToken, String type,
            String msgID) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        // Define timestamp when call request
        Instant timestamp = Instant.now();

        // define new msgId if msgID is wrong
        msgID = authorizationService.generateMsgID(msgID);

        // define path and requestBody for POS
        type = type.toUpperCase();
        httpMethod = httpMethod.toUpperCase();
        if (type.equals("OFFLINE")) {
            JSONObject offlineParams = new JSONObject();
            offlineParams.put("msgID", msgID);
            offlineParams.put("grabID", config.getMerchant_id());
            offlineParams.put("terminalID", config.getTerminal_id());

            if (!httpMethod.equals("GET")) {
                requestBody = AuthorizationService.merge_jsonObj(requestBody, offlineParams);
            } else {
                path = buildHttpGetUrl(offlineParams, path);
            }
        }
        // convert request body to string
        String jsonBody = requestBody == null || requestBody.length() == 0 ? "" : requestBody.toString();

        // generate header of request
        String hmac = authorizationService.generateHmac(config, httpMethod, path, contentType, jsonBody, timestamp);
        Map<String, String> header = generateHeaders(authorizationService, config, httpMethod, contentType, path, type,
                accessToken, hmac, timestamp, msgID);

        // define path of request
        String fullPath = config.getUrl() + path;

        // define return value
        result.put("header", header);
        result.put("body", jsonBody);
        result.put("apiUrl", fullPath);
        return result;
    }

    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<String, String> getHeaderFromRequest(HashMap<String, Object> requestParams) {
        try {
            ConcurrentHashMap<String, String> header = (ConcurrentHashMap<String, String>) requestParams.get("header");
            return header;
        } catch (Exception e) {
            return null;
        }
    }

    public String getBodyFromRequest(HashMap<String, Object> requestParams) {
        try {
            String body = (String) requestParams.get("body");
            return body;
        } catch (Exception e) {
            return null;
        }
    }

    public String getUrlFromRequest(HashMap<String, Object> requestParams) {
        try {
            String url = (String) requestParams.get("apiUrl");
            return url;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> generateHeaders(AuthorizationService authorizationService, Config config,
            String httpMethod, String contentType, String path, String transaction_type, String accessToken,
            String hmac, Instant timestamp, String msgID) {
        String formattedTimestamp = authorizationService.getFormattedDateTime(timestamp);
        Map<PathUtility, String> api = new ApiUrl().getURL(config.getCountry());
        if (accessToken == null)
            accessToken = "";

        Map<String, String> header = new ConcurrentHashMap<String, String>();
        header.put("Accept", "application/json");
        header.put("Content-Type", contentType);
        header.put("X-Request-ID", msgID);
        header.put("X-Sdk-Country", config.getCountry());
        header.put("X-Sdk-Version", config.getSdkVersion());
        header.put("X-Sdk-Language", "JAVA");
        header.put("X-Sdk-Signature", config.getSdkSignature());

        // to set the header use for api get authtoken
        if (path.equals(api.get(PathUtility.PATH_OAUTH_TOKEN))) {
            return header;
        }
        // to set the header use for API online but not for charge init and OTC status
        else if (transaction_type.equals("ONLINE") && !path.equals(api.get(PathUtility.PATH_ONA_CHARGE_INIT))
                && !path.contains("one-time-charge")) {
            header.put("Date", formattedTimestamp);
            header.put("X-GID-AUX-POP", authorizationService.generatePOPSig(config, accessToken, timestamp));
            header.put("Authorization", "Bearer " + accessToken);
        }
        // default charge init/otc status or offline
        else {
            header.put("Date", formattedTimestamp);
            header.put("Authorization", config.getPartner_id() + ":" + hmac);
        }
        return header;
    }

    public String buildHttpGetUrl(JSONObject params, String path) {
        if (!path.contains("?"))
            path = path + "?";
        else
            path = path + "&";
        Set<String> keySet = params != null ? params.keySet() : new HashSet<String>();
        for (String key : keySet) {
            path += key + "=" + params.get(key) + "&";
        }

        return path.substring(0, path.length() - 1);
    }

    public CloseableHttpResponse get(String path, String body, Map<String, String> headers, CloseableHttpClient client)
            throws Exception {
        if (path == null || headers == null)
            return null;
        HttpGet httpGet = new HttpGet(path);
        headers.forEach(httpGet::setHeader);

        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpGet.setEntity(reqEntity);
        }

        return client.execute(httpGet);
    }

    public CloseableHttpResponse post(String path, String body, Map<String, String> headers, CloseableHttpClient client)
            throws Exception {
        if (path == null || headers == null)
            return null;
        HttpPost httpPost = new HttpPost(path);
        headers.forEach(httpPost::setHeader);

        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpPost.setEntity(reqEntity);
        }

        return client.execute(httpPost);
    }

    public CloseableHttpResponse put(String path, String body, Map<String, String> headers, CloseableHttpClient client)
            throws Exception {
        if (path == null || headers == null)
            return null;
        HttpPut httpPut = new HttpPut(path);
        headers.forEach(httpPut::setHeader);

        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpPut.setEntity(reqEntity);
        }

        return client.execute(httpPut);
    }

    public CloseableHttpResponse delete(String path, String body, Map<String, String> headers,
            CloseableHttpClient client) throws Exception {
        if (path == null || headers == null)
            return null;
        HttpDelete httpDelete = new HttpDelete(path);
        headers.forEach(httpDelete::setHeader);

        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpDelete.setEntity(reqEntity);
        }
        return client.execute(httpDelete);
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }
}
