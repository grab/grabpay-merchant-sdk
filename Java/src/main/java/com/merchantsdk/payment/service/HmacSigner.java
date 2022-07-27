package com.merchantsdk.payment.service;

import java.time.Instant;

import org.jetbrains.annotations.NotNull;

import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.exception.SDKRuntimeException;

public class HmacSigner {
    private Config config;

    public HmacSigner(Config config) {
        this.config = config;
    }

    public String buildHmacSignature(
            @NotNull String requestMethod,
            @NotNull String apiUrl,
            @NotNull String contentType,
            @NotNull String requestBody,
            @NotNull Instant unixTime) throws SDKRuntimeException {
        String timestamp = Utils.getFormattedDateTime(unixTime);
        return buildHmacSignature(requestMethod, apiUrl, contentType, requestBody, timestamp);
    }

    /**
     * Build HMAC signature for a request
     * 
     * @return hash value of sign
     */
    public String buildHmacSignature(
            @NotNull String requestMethod,
            @NotNull String apiUrl,
            @NotNull String contentType,
            @NotNull String requestBody,
            @NotNull String formattedTime) throws SDKRuntimeException {
        try {
            String jsonBody = requestBody.isEmpty() ? "" : requestBody;

            String hashedPayload = jsonBody.length() > 0 ? Utils.sha256Base64(jsonBody) : "";

            String stringToSign = requestMethod + '\n'
                    + contentType + '\n'
                    + formattedTime + '\n'
                    + apiUrl + '\n'
                    + hashedPayload + '\n';

            return Utils.hmacSha256(stringToSign, config.getPartnerSecret());
        } catch (Exception e) {
            throw new SDKRuntimeException("Error when generate HMAC signature caused by:\n" + e.getMessage());
        }
    }
}
