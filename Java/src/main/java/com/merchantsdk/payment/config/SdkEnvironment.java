package com.merchantsdk.payment.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.merchantsdk.payment.Country;

public class SdkEnvironment {
    static class EnvCountryPair {
        private final EnvironmentType env;
        private final Country country;

        EnvCountryPair(EnvironmentType env, Country country) {
            this.env = env;
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            EnvCountryPair pair = (EnvCountryPair) o;
            return this.country.equals(pair.country) && this.env.equals(pair.env);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.env, this.country);
        }
    }

    static class EnvironmentSet {
        private final String baseUrl;

        public EnvironmentSet(final String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public final String getBaseUrl() {
            return baseUrl;
        }
    }

    private static final Map<EnvCountryPair, EnvironmentSet> definedEnvVars = new HashMap<EnvCountryPair, EnvironmentSet>() {
        {
            put(new EnvCountryPair(EnvironmentType.STAGING, Country.VIETNAM),
                    new EnvironmentSet("https://stg-paysi.moca.vn"));
            put(new EnvCountryPair(EnvironmentType.PRODUCTION, Country.VIETNAM),
                    new EnvironmentSet("https://partner-gw.moca.vn"));
        }
    };
    private static final Map<EnvironmentType, EnvironmentSet> fallbackEnvVars = new HashMap<EnvironmentType, EnvironmentSet>() {
        {
            put(EnvironmentType.STAGING, new EnvironmentSet("https://partner-api.stg-myteksi.com"));
            put(EnvironmentType.PRODUCTION, new EnvironmentSet("https://partner-api.grab.com"));
        }
    };

    public final static String SDK_VERSION = "2.0.0";
    public final static String SDK_SIGNATURE = "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7";

    public static EnvironmentSet getEnvVars(EnvironmentType env, Country country) {
        EnvCountryPair pair = new EnvCountryPair(env, country);
        if (definedEnvVars.containsKey(pair)) {
            return definedEnvVars.get(pair);
        }

        return fallbackEnvVars.get(env);
    }
}
