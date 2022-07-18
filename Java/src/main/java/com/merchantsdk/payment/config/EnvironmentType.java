package com.merchantsdk.payment.config;

public enum EnvironmentType {
    STAGING("STG"),
    PRODUCTION("PRD");

    private final String value;

    EnvironmentType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
