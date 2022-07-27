package com.merchantsdk.payment;

import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.models.PosCancelRequest;
import com.merchantsdk.payment.models.PosCancelResponse;
import com.merchantsdk.payment.models.PosInitiateRequest;
import com.merchantsdk.payment.models.PosInitiateResponse;
import com.merchantsdk.payment.models.PosInquiryRequest;
import com.merchantsdk.payment.models.PosInquiryResponse;
import com.merchantsdk.payment.models.PosRefundRequest;
import com.merchantsdk.payment.models.PosRefundResponse;
import com.merchantsdk.payment.service.OfflineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.json.JSONObject;

public class MerchantIntegrationOffline extends MerchantIntegration<PosConfig> {

    private final OfflineTransaction offlineTransaction;

    public MerchantIntegrationOffline(EnvironmentType env, Country country, String partnerId, String partnerSecret,
            String merchantId, String terminalId) {
        super(new PosConfig(env, country, partnerId, partnerSecret, merchantId, terminalId));
        this.offlineTransaction = new OfflineTransaction((PosConfig) this.getConfig());
    }

    protected void populateTerminalID(JSONObject requestBody) {
        if (requestBody.has("POSDetails")) {
            requestBody.getJSONObject("POSDetails").put("terminalID", this.getConfig().getTerminalId());
        } else {
            requestBody.put("POSDetails", new JSONObject().put("terminalID", this.getConfig().getTerminalId()));
        }
    }

    protected void populateStoreGrabId(JSONObject requestBody) {
        if (requestBody.has("transactionDetails")) {
            requestBody.getJSONObject("transactionDetails").put("storeGrabID", this.getConfig().getMerchantId());
        } else {
            requestBody.put("transactionDetails",
                    new JSONObject().put("storeGrabID", this.getConfig().getMerchantId()));
        }
    }

    public PosInitiateResponse initiate(PosInitiateRequest request) {
        JSONObject requestBody = new JSONObject(request);
        populateTerminalID(requestBody);
        populateStoreGrabId(requestBody);
        CloseableHttpResponse response = this.offlineTransaction.initiate(requestBody);
        return convertResponseToObj(response, PosInitiateResponse.class);
    }

    public PosCancelResponse cancel(PosCancelRequest request) {
        JSONObject requestBody = new JSONObject(request);
        populateStoreGrabId(requestBody);
        CloseableHttpResponse response = this.offlineTransaction.cancel(requestBody);
        return convertResponseToObj(response, PosCancelResponse.class);
    }

    public PosRefundResponse refund(PosRefundRequest request) {
        JSONObject requestBody = new JSONObject(request);
        CloseableHttpResponse response = this.offlineTransaction.refund(requestBody);
        return convertResponseToObj(response, PosRefundResponse.class);
    }

    public PosInquiryResponse inquire(PosInquiryRequest request) {
        JSONObject requestBody = new JSONObject(request);
        CloseableHttpResponse response = this.offlineTransaction.inquire(requestBody);
        return convertResponseToObj(response, PosInquiryResponse.class);
    }

}
