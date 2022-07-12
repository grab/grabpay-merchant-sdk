package com.merchantsdk.payment;

import com.merchantsdk.payment.config.ApiUrl;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.PathUtility;
import com.merchantsdk.payment.service.AuthorizationService;
import com.merchantsdk.payment.service.OfflineTransaction;
import com.merchantsdk.payment.service.OnlineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class MerchantIntegration {
    private Config config;
    private OnlineTransaction onlineTransaction;
    private OfflineTransaction offlineTransaction;

    public MerchantIntegration() {
    }

    /**
     * Constructor for MerchantIntegration
     * 
     * @param stage          - which type of use SDK - Staging: "STG", Production:
     *                       "PRD"
     * @param country        - Country of the merchant location, Ref:
     *                       https://countrycode.org/
     * @param partner_id     - Unique ID for a partner. Retrieve from Developer Home
     * @param partner_secret - Secret key for a partner. Retrieve from Developer
     *                       Home
     * @param merchant_id    - ? Retrieve from Developer Home.
     * @param terminal_id    - ? Retrieve from Developer Home. Only available for
     *                       POS (P2M)
     * @param client_id      - ? Retrieve from Developer Home. Only available for
     *                       OnA
     * @param client_secret  - ? Retrieve from Developer Home. Only available for
     *                       OnA
     * @param redirect_url   - ? The url configured in Developer Home. Only
     *                       available for OnA
     */
    public MerchantIntegration(String stage, String country, String partner_id, String partner_secret,
            String merchant_id, String terminal_id, String client_id, String client_secret, String redirect_url) {
        this.config = new Config(stage, country, partner_id, partner_secret, merchant_id, terminal_id, client_id,
                client_secret, redirect_url);
        Map<PathUtility, String> countryURL = new ApiUrl().getURL(country);
        if (terminal_id != null && !terminal_id.isEmpty())
            setUpOffline(countryURL);
        else
            setUpOnline(countryURL);
    }

    private void setUpOnline(Map<PathUtility, String> countryURL) {
        this.onlineTransaction = new OnlineTransaction(this.config, countryURL);
    }

    private void setUpOffline(Map<PathUtility, String> countryURL) {
        this.offlineTransaction = new OfflineTransaction(this.config, countryURL);
    }

    public JSONObject processResponse(CloseableHttpResponse response) {
        try {
            JSONObject resObj = new JSONObject();
            if (response.getCode() >= 400) {
                resObj.put("Error", response.getCode());
            }
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null)
                return resObj;
            String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            JSONObject resObj1 = new JSONObject(responseString);

            return AuthorizationService.merge_jsonObj(resObj, resObj1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public OnlineTransaction getOnlineTransaction() {
        return this.onlineTransaction;
    }

    public void setOnlineTransaction(OnlineTransaction onlineTransaction) {
        this.onlineTransaction = onlineTransaction;
    }

    public OfflineTransaction getOfflineTransaction() {
        return this.offlineTransaction;
    }

    public void setOfflineTransaction(OfflineTransaction offlineTransaction) {
        this.offlineTransaction = offlineTransaction;
    }
}
