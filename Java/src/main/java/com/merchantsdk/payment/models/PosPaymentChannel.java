package com.merchantsdk.payment.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Payment channel
 * <p>
 * This field will be used to differentiate the different POS payment channels,
 * and will determine which other Conditional parameters will need to be
 * required in the request, or processed in the response.
 * 
 */
public enum PosPaymentChannel {

    /**
     * Consumer Present QR Code
     */
    CPQR("CPQR"),

    /**
     * Merchant Present QR Code
     */
    MPQR("MPQR");

    private final String value;
    private final static Map<String, PosPaymentChannel> CONSTANTS = new HashMap<String, PosPaymentChannel>();

    static {
        for (PosPaymentChannel c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private PosPaymentChannel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static PosPaymentChannel fromValue(String value) {
        PosPaymentChannel constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}