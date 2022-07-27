package com.merchantsdk.payment.models;

import java.util.HashMap;
import java.util.Map;

public enum PosPaymentMethod {
    GPWALLET("GPWALLET"),
    POSTPAID("POSTPAID"),
    INSTALMENT_4("INSTALMENT_4"),
    CARD("CARD"),
    PAYNOW("PAYNOW"),
    DUITNOW("DUITNOW"),
    QRPH("QRPH");

    private final String value;
    private final static Map<String, PosPaymentMethod> CONSTANTS = new HashMap<String, PosPaymentMethod>();

    static {
        for (PosPaymentMethod c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private PosPaymentMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static PosPaymentMethod fromValue(String value) {
        PosPaymentMethod constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}