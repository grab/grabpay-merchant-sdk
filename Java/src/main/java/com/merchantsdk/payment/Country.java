package com.merchantsdk.payment;

public enum Country {
    VIETNAM("VN"),
    SINGAPORE("SG"),
    MALAYSIA("MY"),
    PHILIPPINES("PH"),
    THAILAND("TH"),
    INDONESIA("ID"),
    MYANMAR("MM"),
    CAMBODIA("KH");

    private final String countryCode;

    Country(final String countryCode) {
        this.countryCode = countryCode;
    }

    public final String getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return countryCode;
    }
}
