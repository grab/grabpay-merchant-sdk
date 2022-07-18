package com.merchantsdk.payment.service;

import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.SdkEnvironment;
import com.merchantsdk.payment.exception.SDKRuntimeException;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.Instant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Requester {

    public static class RequestParams {
        Map<String, String> headers;
        String url;
        String body;

        public RequestParams(String url, Map<String, String> headers, String body) {
            this.url = url;
            this.headers = headers;
            this.body = body;
        }

        public Map<String, String> getHeaders() {
            return this.headers;
        }

        public String getUrl() {
            return this.url;
        }

        public String getBody() {
            return this.body;
        }
    }

    private CloseableHttpClient client;
    private Config config;

    public Requester(Config config) {
        this(config, HttpClients.createDefault());
    }

    public Requester(Config config, CloseableHttpClient client) {
        this.config = config;
        this.client = client;
    }

    protected String buildPathWithQuery(String path, Map<String, String> query) {
        if (query == null || query.isEmpty()) {
            return path;
        }
        String pathWithParams = path;
        URIBuilder builder = new URIBuilder();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }
        try {
            pathWithParams += "?" + builder.build().getQuery();
        } catch (URISyntaxException e) {
            throw new SDKRuntimeException("Unable to build URL query for " + path);
        }
        return pathWithParams;
    }

    public CloseableHttpResponse sendHmacRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers) {
        return this.sendHmacRequest(httpMethod, path, query, headers, "");
    }

    public CloseableHttpResponse sendHmacRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers, JSONObject requestBody) {
        String requestBodyJsonStr = requestBody == null || requestBody.length() == 0 ? "" : requestBody.toString();
        return this.sendHmacRequest(httpMethod, path, query, headers, requestBodyJsonStr);
    }

    public CloseableHttpResponse sendHmacRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers, String requestBodyJsonStr) {
        HmacSigner signer = new HmacSigner(config);
        String pathWithParams = buildPathWithQuery(path, query);
        String contentType = headers != null && headers.containsKey("Content-Type") ? headers.get("Content-Type")
                : "application/json";
        String timestamp = headers != null && headers.containsKey("Date") ? headers.get("Date")
                : Utils.getFormattedDateTime(Instant.now());
        String hmacSignature = signer.buildHmacSignature(httpMethod, pathWithParams, contentType, requestBodyJsonStr,
                timestamp);

        Map<String, String> signedHeaders = new HashMap<>();
        signedHeaders.put("Authorization", String.format("%s:%s", config.getPartnerId(), hmacSignature));
        signedHeaders.put("Content-Type", contentType);
        signedHeaders.put("Date", timestamp);
        if (headers != null && !headers.isEmpty()) {
            signedHeaders.putAll(headers);
        }

        return this.sendRequest(httpMethod, path, query, signedHeaders, requestBodyJsonStr);
    }

    public CloseableHttpResponse sendRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers, String requestBodyJsonStr) {

        RequestParams requestParams = this.prepareRequest(httpMethod, path, query, headers, requestBodyJsonStr);
        CloseableHttpResponse response;
        try {
            switch (httpMethod.toUpperCase()) {
                case "GET":
                    response = this.get(requestParams);
                    break;
                case "POST":
                    response = this.post(requestParams);
                    break;
                case "PUT":
                    response = this.put(requestParams);
                    break;
                case "DELETE":
                    response = this.delete(requestParams);
                    break;
                default:
                    throw new SDKRuntimeException("Invalid HTTP method provided: " + httpMethod);
            }
            return response;
        } catch (Exception e) {
            throw new SDKRuntimeException("Error during sending request : " + e.getMessage());
        }
    }

    public CloseableHttpResponse sendRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers) {
        return this.sendRequest(httpMethod, path, query, headers, "");
    }

    public CloseableHttpResponse sendRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers, JSONObject requestBody) {
        String jsonBody = requestBody == null || requestBody.length() == 0 ? "" : requestBody.toString();
        return this.sendRequest(httpMethod, path, query, headers, jsonBody);
    }

    public RequestParams prepareRequest(String httpMethod, String path, Map<String, String> query,
            Map<String, String> headers, String requestBodyJsonStr) {

        // Define timestamp when call request
        String formattedTimestamp = Utils.getFormattedDateTime(Instant.now());
        Map<String, String> finalHeaders = new ConcurrentHashMap<String, String>();
        finalHeaders.put("Date", formattedTimestamp);
        finalHeaders.put("Accept", "application/json");
        finalHeaders.put("Content-Type", "application/json");
        finalHeaders.put("X-Sdk-Country", config.getCountry().toString());
        finalHeaders.put("X-Sdk-Version", SdkEnvironment.SDK_VERSION);
        finalHeaders.put("X-Sdk-Language", "java");
        finalHeaders.put("X-Sdk-Signature", SdkEnvironment.SDK_SIGNATURE);
        if (headers != null && !headers.isEmpty()) {
            finalHeaders.putAll(headers);
        }

        String pathWithParams = buildPathWithQuery(path, query);

        // define path of request
        String fullPath = config.getBaseUrl() + pathWithParams;

        // define return value
        return new RequestParams(fullPath, finalHeaders, requestBodyJsonStr);
    }

    public CloseableHttpResponse get(RequestParams requestParams)
            throws Exception {
        HttpGet httpGet = new HttpGet(requestParams.getUrl());
        if (requestParams.getHeaders() != null) {
            requestParams.getHeaders().forEach(httpGet::setHeader);
        }

        String body = requestParams.getBody();
        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpGet.setEntity(reqEntity);
        }

        return client.execute(httpGet);
    }

    public CloseableHttpResponse post(RequestParams requestParams)
            throws Exception {
        HttpPost httpPost = new HttpPost(requestParams.getUrl());
        if (requestParams.getHeaders() != null) {
            requestParams.getHeaders().forEach(httpPost::setHeader);
        }

        String body = requestParams.getBody();
        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpPost.setEntity(reqEntity);
        }

        return client.execute(httpPost);
    }

    public CloseableHttpResponse put(RequestParams requestParams)
            throws Exception {
        HttpPut httpPut = new HttpPut(requestParams.getUrl());
        if (requestParams.getHeaders() != null) {
            requestParams.getHeaders().forEach(httpPut::setHeader);
        }

        String body = requestParams.getBody();
        if (body != null && !body.isEmpty()) {
            StringEntity reqEntity = new StringEntity(body);
            httpPut.setEntity(reqEntity);
        }

        return client.execute(httpPut);
    }

    public CloseableHttpResponse delete(RequestParams requestParams) throws Exception {
        HttpDelete httpDelete = new HttpDelete(requestParams.getUrl());
        if (requestParams.getHeaders() != null) {
            requestParams.getHeaders().forEach(httpDelete::setHeader);
        }

        String body = requestParams.getBody();
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
}
