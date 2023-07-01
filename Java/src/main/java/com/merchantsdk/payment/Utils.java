package com.merchantsdk.payment;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import com.merchantsdk.payment.exception.SDKRuntimeException;

public class Utils {
    /**
     * Hash a string using MD5 algorithm and retuen Hex encode of MD5 string
     * dddddddd
     * @param msg the data need to be hashed
     * @return String
     * @throws SDKRuntimeException
     */
    public static String md5Hex(String msg) throws SDKRuntimeException, IllegalArgumentException {
        if (msg == null) {
            throw new IllegalArgumentException("md5Hex `msg` parameter cannot be null");
        }
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(msg.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new SDKRuntimeException("MD5 algorithm not available");
        }
    }

    public static String base64URLEncode(@NotNull String data) {
        return data.replace("=", "").replace('/', '_').replace('+', '-');
    }

    private static final String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * Return a random String with specified length
     * 
     * @param len length of result String
     * @return String
     */
    public static String getRandomString(int len) {
        Random rd = new Random();
        String res = "";
        for (int i = 0; i < len; i++) {
            res += charSet.charAt(rd.nextInt(charSet.length()));

        }
        return res;
    }

    private static final ZoneId gmtZoneId = ZoneId.of("GMT");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z");

    /**
     * convert Time to String as format Thu, 17 Jan 2019 02:45:06 GMT
     * 
     * @param now Unix time
     * @return String
     */
    public static String getFormattedDateTime(Instant now) {
        ZonedDateTime datetime = now.atZone(gmtZoneId);
        return datetime.format(formatter);
    }

    /**
     * Merge second json object to the first one.
     * 
     * @param o1 - json object
     * @param o2 - json object
     * @return merged object
     */
    public static JSONObject mergeJsonObjects(JSONObject o1, JSONObject o2) {
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
     * Using SHA256 to hash the input
     * 
     * @param message
     * @return base64 of message's SHA256 hash
     */
    public static String sha256Base64(String message) throws SDKRuntimeException {
        if (message == null) {
            throw new SDKRuntimeException("message cannot be null when calling sha256Base64()");
        }
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new SDKRuntimeException("Error hashing data caused by:\n" + e.getMessage());
        }
    }

    public static String hmacSha256(String message, String secret)
            throws InvalidKeyException, NoSuchAlgorithmException {
        if (message == null) {
            throw new SDKRuntimeException("message cannot be null when calling hmacSha256()");
        }
        if (secret == null) {
            throw new SDKRuntimeException("secret cannot be null when calling hmacSha256()");
        }

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        sha256Hmac.init(secretKey);

        byte[] macData = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(macData);
    }

}
