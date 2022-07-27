package com.merchantsdk.payment.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.OtcConfig;
import com.merchantsdk.payment.exception.SDKRuntimeException;

public class HmacPopSigner {
    private OtcConfig config;

    public HmacPopSigner(OtcConfig config) {
        this.config = config;
    }

    /**
     * Build X-GID-AUX-POP HMAC signature for given access token and timestamp
     * 
     * @param accessToken - access_token, which get from authorization server
     * @param unixTime    - timestamp as unix
     * @return String
     * @throws SDKRuntimeException
     */
    public String buildPopSignature(
            @NotNull String accessToken,
            @NotNull Instant unixTime) throws SDKRuntimeException {
        try {
            long epochTime = unixTime.getEpochSecond();
            String message = epochTime + accessToken;

            String signature = Utils.hmacSha256(message, config.getClientSecret());
            String sub = Utils.base64URLEncode(signature);

            JSONObject payload = new JSONObject();
            payload.put("time_since_epoch", epochTime);
            payload.put("sig", sub);

            String payloadBytes = payload.toString();
            return Utils
                    .base64URLEncode(Base64.getEncoder().encodeToString(payloadBytes.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new SDKRuntimeException("Error generate POP signature caused by : \n" + e.getMessage());
        }
    }

}
