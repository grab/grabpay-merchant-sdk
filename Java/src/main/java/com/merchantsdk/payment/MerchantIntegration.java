package com.merchantsdk.payment;

import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.exception.SDKRuntimeException;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public abstract class MerchantIntegration {
    private Config config;

    /**
     * Constructor for MerchantIntegration
     */
    public MerchantIntegration(Config config) {
        this.config = config;
    }

    protected JSONObject processResponse(CloseableHttpResponse response) {
        try {
            JSONObject resObj = new JSONObject();
            if (response.getCode() >= 400) {
                resObj.put("error", response.getCode());
            }
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null)
                return resObj;
            String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            if (responseString.isEmpty()) {
                return resObj;
            }
            JSONObject resObj1 = new JSONObject(responseString);

            return Utils.mergeJsonObjects(resObj, resObj1);
        } catch (Exception e) {
            throw new SDKRuntimeException("Error processing response:\n" + e.getMessage());
        }
    }

    public Config getConfig() {
        return config;
    }
}
