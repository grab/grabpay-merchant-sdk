package com.merchantsdk.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.exception.SDKRuntimeException;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class MerchantIntegration<ConfigType extends Config> {
    private ConfigType config;

    /**
     * Constructor for MerchantIntegration
     */
    public MerchantIntegration(ConfigType config) {
        this.config = config;
    }

    protected JSONObject convertResponseToObj(CloseableHttpResponse response) {
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

    protected <TObj> TObj convertResponseToObj(CloseableHttpResponse response, Class<TObj> classDef) {
        try {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null)
                return null;
            String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            if (responseString.isEmpty()) {
                return null;
            }
            TObj resObj = buildFromJson(responseString, classDef);
            return resObj;
        } catch (Exception e) {
            throw new SDKRuntimeException("Error processing response:\n" + e.getMessage());
        }
    }

    protected <TObj> TObj buildFromJson(String json, Class<TObj> classDef)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TObj result = mapper.readValue(json, classDef);
        return result;
    }

    public ConfigType getConfig() {
        return config;
    }
}
