package com.merchantsdk.payment.models;

import java.util.HashMap;
import java.util.Map;

/**
 * currency
 * <p>
 * Currency that is associated with the payment amount. Specify the three-letter
 * [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) currency code.
 * 
 * 
 */
public enum Currency {

    SGD("SGD"),
    MYR("MYR"),
    PHP("PHP"),
    IDR("IDR"),
    VND("VND"),
    THB("THB"),
    MMK("MMK"),
    KHR("KHR");

    private final String value;
    private final static Map<String, Currency> CONSTANTS = new HashMap<String, Currency>();

    static {
        for (Currency c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Currency(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static Currency fromValue(String value) {
        Currency constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}