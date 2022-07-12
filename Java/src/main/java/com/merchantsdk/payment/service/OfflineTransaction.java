package com.merchantsdk.payment.service;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.PathUtility;
import org.json.JSONObject;

import java.util.Map;

public class OfflineTransaction {
    private static final String type = "OFFLINE";
    private Config config;
    private Transaction transaction;
    private Map<PathUtility, String> countryURL;

    public OfflineTransaction() {
    }

    public OfflineTransaction(Config config, Map<PathUtility, String> countryURL) {
        this();
        this.config = config;
        this.countryURL = countryURL;
        this.transaction = new Transaction();
    }

    /**
     * This method is used to create new QR code
     * 
     * @param
     * @return json object of response
     */
    public CloseableHttpResponse apiCreateQrCode(String msgID, String partnerTxID, long amount, String currency) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("partnerTxID", partnerTxID);

        String path = this.countryURL.get(PathUtility.PATH_POS_CREATE_QR_CODE);
        return this.transaction.sendRequest(this.config, "POST", path, "application/json", requestBody, null, type,
                msgID);
    }

    public CloseableHttpResponse apiPosPerformTxn(String msgID, String partnerTxID, long amount, String currency,
            String code) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("partnerTxID", partnerTxID);
        requestBody.put("code", code);

        String path = this.countryURL.get(PathUtility.PATH_POS_PERFORM_TRANSACTION);
        return this.transaction.sendRequest(this.config, "POST", path, "application/json", requestBody, null, type,
                msgID);
    }

    public CloseableHttpResponse apiPosCancel(String msgID, String partnerTxID, String origPartnerTxID, String origTxID,
            String currency) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("currency", currency);
        requestBody.put("origTxID", origTxID);
        requestBody.put("partnerTxID", partnerTxID);

        String path = this.countryURL.get(PathUtility.PATH_POS_CANCEL_TRANSACTION);
        path = path.replace("{origPartnerTxID}", origPartnerTxID);
        return this.transaction.sendRequest(this.config, "PUT", path, "application/json", requestBody, null, type,
                msgID);
    }

    public CloseableHttpResponse apiPosRefund(String msgID, String refundPartnerTxID, long amount, String currency,
            String origPartnerTxID, String description) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("currency", currency);
        requestBody.put("amount", amount);
        requestBody.put("reason", description);
        requestBody.put("partnerTxID", refundPartnerTxID);

        String path = this.countryURL.get(PathUtility.PATH_POS_REFUND_TRANSACTION);
        path = path.replace("{origPartnerTxID}", origPartnerTxID);
        return this.transaction.sendRequest(this.config, "PUT", path, "application/json", requestBody, null, type,
                msgID);
    }

    public CloseableHttpResponse apiPosGetTxnStatus(String msgID, String partnerTxID, String currency) {
        String path = this.countryURL.get(PathUtility.PATH_POS_TRANSACTION_DETAILS);
        path = path.replace("{partnerTxID}", partnerTxID).replace("{currency}", currency).replace("{posTxType}", "P2M")
                .replace("{msgID}", msgID);
        return this.transaction.sendRequest(this.config, "GET", path, "application/json", null, null, type, msgID);
    }

    public CloseableHttpResponse apiPosGetRefundStatus(String msgID, String refundPartnerTxID, String currency) {
        String path = this.countryURL.get(PathUtility.PATH_POS_TRANSACTION_DETAILS);
        path = path.replace("{partnerTxID}", refundPartnerTxID).replace("{currency}", currency)
                .replace("{posTxType}", "Refund").replace("{msgID}", msgID);
        return this.transaction.sendRequest(this.config, "GET", path, "application/json", null, null, type, msgID);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Map<PathUtility, String> getCountryURL() {
        return countryURL;
    }

    public void setCountryURL(Map<PathUtility, String> countryURL) {
        this.countryURL = countryURL;
    }
}
