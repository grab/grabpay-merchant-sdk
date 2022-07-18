package com.merchantsdk.payment.config;

import com.merchantsdk.payment.Country;

public class PosConfig extends Config {
    private String terminalId;

    public PosConfig(EnvironmentType env,
            Country country,
            String partnerId,
            String partnerSecret,
            String merchantId,
            String terminalId) {
        super(env, country, partnerId, partnerSecret, merchantId);
        this.terminalId = terminalId;
    }

    public String getTerminalId() {
        return terminalId;
    }
}
