package com.merchantsdk.payment.config;

import com.merchantsdk.payment.Country;

public class OtcConfig extends Config {
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public OtcConfig(EnvironmentType env,
            Country country,
            String partnerId,
            String partnerSecret,
            String merchantId,
            String clientId,
            String clientSecret,
            String redirectUri) {
        super(env, country, partnerId, partnerSecret, merchantId);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getRedirectUri() {
        return this.redirectUri;
    }
}
