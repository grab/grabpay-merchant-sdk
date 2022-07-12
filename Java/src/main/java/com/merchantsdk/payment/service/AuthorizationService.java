package com.merchantsdk.payment.service;

import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.exception.SDKRuntimeException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AuthorizationService {

    public String base64URLEncode(@NotNull String data) {
        return data.replace("=", "").replace('/', '_').replace('+', '-');
    }

    /**
     * Using sha265 to hash the input sign
     * 
     * @param requestBody
     * @return base64 of hashed requestBody
     */
    public String _sha265(@NotNull String requestBody) throws SDKRuntimeException {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(requestBody.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new SDKRuntimeException("Error hashing data caused by:\n" + e.getMessage());
        }
    }

    /**
     * Create HMAC signature
     * 
     * @param unixTime : given created request time (java.time.Instant)
     * @return hash value of sign
     */
    public String generateHmac(@NotNull Config config,
            @NotNull String requestMethod,
            @NotNull String apiUrl,
            @NotNull String contentType,
            @NotNull String requestBody,
            @NotNull Instant unixTime) throws SDKRuntimeException {
        try {
            String jsonBody = requestBody.isEmpty() ? "" : requestBody;

            // convert Time to String as format Thu, 17 Jan 2019 02:45:06 GMT
            String timestamp = getFormattedDateTime(unixTime);

            // Hash body as sha265
            String hashedPayload = jsonBody.length() > 0 ? _sha265(jsonBody) : "";

            String stringToSign = requestMethod + '\n'
                    + contentType + '\n'
                    + timestamp + '\n'
                    + apiUrl + '\n'
                    + hashedPayload + '\n';

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(config.getPartner_secret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] macData = sha256Hmac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(macData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SDKRuntimeException("Error when generate HMAC signature caused by:\n" + e.getMessage());
        }
    }

    /**
     * Return a X-GID-AUX-POP HMAC signature
     * 
     * @param config      - Config instance
     * @param accessToken - access_token, which get from authorization server
     * @param unixTime    - timestamp as unix
     * @return String
     * @throws SDKRuntimeException
     */
    public String generatePOPSig(@NotNull Config config,
            @NotNull String accessToken,
            @NotNull Instant unixTime) throws SDKRuntimeException {
        try {
            long epochTime = unixTime.getEpochSecond();
            String message = epochTime + accessToken;

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(config.getClient_secret().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");

            sha256Hmac.init(secretKey);
            byte[] macData = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getEncoder().encodeToString(macData);
            String sub = base64URLEncode(signature);

            JSONObject payload = new JSONObject();
            payload.put("time_since_epoch", epochTime);
            payload.put("sig", sub);

            String payloadBytes = payload.toString();
            return base64URLEncode(Base64.getEncoder().encodeToString(payloadBytes.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new SDKRuntimeException("Error generate POP signature caused by : \n" + e.getMessage());
        }
    }

    /**
     * Hash a string using MD5 algorithm and retuen Hex encode of MD5 string
     * 
     * @param msg the data need to be hashed
     * @return String
     * @throws SDKRuntimeException
     */
    public String generateMD5(String msg) throws SDKRuntimeException {
        if (msg == null)
            return "";
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(msg.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception e) {
            throw new SDKRuntimeException("Error during hashing MD5 " + e.getMessage());
        }
    }

    /**
     * convert Time to String as format Thu, 17 Jan 2019 02:45:06 GMT
     * 
     * @param now Unix time
     * @return String
     */
    public String getFormattedDateTime(Instant now) {
        ZonedDateTime datetime = now.atZone(ZoneId.of("GMT"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");
        String timestamp = datetime.format(formatter);
        return timestamp;
    }

    /**
     * Return a random String with specified length
     * 
     * @param len length of result String
     * @return String
     */
    public String getRandomString(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rd = new Random();
        String res = "";
        for (int i = 0; i < len; i++) {
            res += chars.charAt(rd.nextInt(chars.length()));

        }
        return res;
    }

    /**
     * Merge second json object to the first one.
     * 
     * @param o1 - json object
     * @param o2 - json object
     * @return merged object
     */
    public static JSONObject merge_jsonObj(JSONObject o1, JSONObject o2) {
        JSONObject res = new JSONObject();
        if (o1 == null)
            return o2;
        if (o2 == null)
            return o1;
        o1.keySet().forEach(it -> res.put(it, o1.get(it)));
        o2.keySet().forEach(it -> res.put(it, o2.get(it)));
        return res;
    }

    /**
     * Generate msgID. Unless msgID input is right, create new one with length
     * equals 32.
     * 
     * @param msgID - defined msgID
     * @return a String as msgID
     */
    public String generateMsgID(String msgID) {
        if (msgID == null || msgID.length() != 32) {
            String msg = getRandomString(32);
            msgID = generateMD5(msg);
        }
        return msgID;
    }

    /**
     * Generate codeVerifier. Unless msgID input is right, create new one with valid
     * length[43-128].
     * 
     * @param codeVerifier - given codeVerifier
     * @return a String as msgID
     */
    public String generateCodeVerifier(String codeVerifier) {
        if (codeVerifier.length() >= 43 && codeVerifier.length() <= 128) {
            return codeVerifier;
        }

        while (codeVerifier.length() < 43) {
            codeVerifier = "0".concat(codeVerifier);
        }
        return codeVerifier;
    }
}