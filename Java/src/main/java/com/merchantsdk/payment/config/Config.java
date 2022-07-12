package com.merchantsdk.payment.config;

import com.merchantsdk.payment.exception.SDKRuntimeException;

import java.util.Objects;

public class Config {
    private final static String sdkVersion = "2.0.0";
    private final static String sdkSignature = "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7";
    private String staging;
    private String country;
    private String url;
    private String partner_id;
    private String partner_secret;

    // OnA
    private String merchant_id;
    private String store_id;
    private String client_id;
    private String client_secret;
    private String redirect_uri;

    // POS
    private String grab_id;
    private String terminal_id;

    public Config() {
    }

    public Config(String staging,
            String country,
            String partner_id,
            String partner_secret,
            String merchant_id,
            String terminal_id,
            String client_id,
            String client_secret,
            String redirect_uri) {
        this.staging = staging.toUpperCase();
        this.country = country.toUpperCase();
        this.partner_id = partner_id;
        this.partner_secret = partner_secret;
        this.merchant_id = merchant_id;
        this.terminal_id = terminal_id;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.init();
    }

    private void init() {
        if (this.getCountry().length() != 2)
            throw new SDKRuntimeException("Please correct your country code");
        if (this.staging.toUpperCase().equals("PRD")) {
            if (this.country.equals("VN")) {
                this.url = "https://partner-gw.moca.vn";
            } else {
                this.url = "https://partner-api.grab.com";
            }
        } else {
            if (this.country.equals("VN")) {
                this.url = "https://stg-paysi.moca.vn";
            } else {
                this.url = "https://partner-api.stg-myteksi.com";
            }
        }
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getStaging() {
        return staging;
    }

    public String getCountry() {
        return country;
    }

    public String getUrl() {
        return url;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public String getPartner_secret() {
        return partner_secret;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getGrab_id() {
        return grab_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setStaging(String staging) {
        this.staging = staging;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public void setPartner_secret(String partner_secret) {
        this.partner_secret = partner_secret;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public void setGrab_id(String grab_id) {
        this.grab_id = grab_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getSdkSignature() {
        return sdkSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Config config = (Config) o;
        return Objects.equals(staging, config.staging) && Objects.equals(country, config.country)
                && Objects.equals(url, config.url) && Objects.equals(partner_id, config.partner_id)
                && Objects.equals(partner_secret, config.partner_secret)
                && Objects.equals(merchant_id, config.merchant_id) && Objects.equals(store_id, config.store_id)
                && Objects.equals(client_id, config.client_id) && Objects.equals(client_secret, config.client_secret)
                && Objects.equals(redirect_uri, config.redirect_uri) && Objects.equals(grab_id, config.grab_id)
                && Objects.equals(terminal_id, config.terminal_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staging, country, url, partner_id, partner_secret, merchant_id, store_id, client_id,
                client_secret, redirect_uri, grab_id, terminal_id);
    }
}
