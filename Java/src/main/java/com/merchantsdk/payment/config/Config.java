package com.merchantsdk.payment.config;

import java.util.Map;
import java.util.Objects;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.config.ApiUrl.ApiEndpoint;
import com.merchantsdk.payment.config.SdkEnvironment.EnvironmentSet;

public abstract class Config {
    private EnvironmentType env;
    private Country country;
    private String partnerId;
    private String partnerSecret;
    private String merchantId;

    private EnvironmentSet envVars;
    private Map<ApiEndpoint, String> apiUrlSet;

    public Config(EnvironmentType env,
            Country country,
            String partnerId,
            String partnerSecret,
            String merchantId) {
        this.env = env;
        this.country = country;
        this.partnerId = partnerId;
        this.partnerSecret = partnerSecret;
        this.merchantId = merchantId;
        this.envVars = SdkEnvironment.getEnvVars(env, country);
        this.apiUrlSet = ApiUrl.getUrlSet(country);
    }

    public EnvironmentType getEnv() {
        return env;
    }

    public Country getCountry() {
        return country;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPartnerSecret() {
        return partnerSecret;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getBaseUrl() {
        return this.envVars.getBaseUrl();
    }

    public String getApiUrl(ApiEndpoint endpoint) {
        return this.apiUrlSet.get(endpoint);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Config config = (Config) o;
        return Objects.equals(env, config.env) && Objects.equals(country, config.country)
                && Objects.equals(partnerId, config.partnerId)
                && Objects.equals(partnerSecret, config.partnerSecret)
                && Objects.equals(merchantId, config.merchantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(env, country, partnerId, partnerSecret, merchantId);
    }
}
