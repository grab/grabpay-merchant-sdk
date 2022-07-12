package com.merchantsdk.payment.config;

import java.util.HashMap;
import java.util.Map;

public class ApiUrl {
    private static final Map<String, Map<PathUtility, String>> urls = new HashMap<String, Map<PathUtility, String>>() {
        {
            Map<PathUtility, String> vn = new HashMap<PathUtility, String>() {
                {
                    put(PathUtility.PATH_OAUTH_TOKEN, "/grabid/v1/oauth2/token");

                    put(PathUtility.PATH_POS_CREATE_QR_CODE, "/mocapay/partners/v1/terminal/qrcode/create");
                    put(PathUtility.PATH_POS_CANCEL_TRANSACTION,
                            "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/cancel");
                    put(PathUtility.PATH_POS_REFUND_TRANSACTION,
                            "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/refund");
                    put(PathUtility.PATH_POS_PERFORM_TRANSACTION, "/mocapay/partners/v1/terminal/transaction/perform");
                    put(PathUtility.PATH_POS_TRANSACTION_DETAILS,
                            "/mocapay/partners/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}");

                    put(PathUtility.PATH_ONA_CHARGE_INIT, "/mocapay/partner/v2/charge/init");
                    put(PathUtility.PATH_ONA_CHARGE_COMPLETE, "/mocapay/partner/v2/charge/complete");
                    put(PathUtility.PATH_ONA_CHARGE_STATUS,
                            "/mocapay/partner/v2/charge/{partnerTxID}/status?currency={currency}");
                    put(PathUtility.PATH_ONA_REFUND, "/mocapay/partner/v2/refund");
                    put(PathUtility.PATH_ONA_REFUND_STATUS,
                            "/mocapay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}");
                    put(PathUtility.PATH_ONA_ONE_TIME_CHARGE_STATUS,
                            "/mocapay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}");
                }
            };
            Map<PathUtility, String> regional = new HashMap<PathUtility, String>() {
                {
                    put(PathUtility.PATH_OAUTH_TOKEN, "/grabid/v1/oauth2/token");

                    put(PathUtility.PATH_POS_CREATE_QR_CODE, "/grabpay/partner/v1/terminal/qrcode/create");
                    put(PathUtility.PATH_POS_CANCEL_TRANSACTION,
                            "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/cancel");
                    put(PathUtility.PATH_POS_REFUND_TRANSACTION,
                            "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/refund");
                    put(PathUtility.PATH_POS_PERFORM_TRANSACTION, "/grabpay/partner/v1/terminal/transaction/perform");
                    put(PathUtility.PATH_POS_TRANSACTION_DETAILS,
                            "/grabpay/partner/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}");

                    put(PathUtility.PATH_ONA_CHARGE_INIT, "/grabpay/partner/v2/charge/init");
                    put(PathUtility.PATH_ONA_CHARGE_COMPLETE, "/grabpay/partner/v2/charge/complete");
                    put(PathUtility.PATH_ONA_CHARGE_STATUS,
                            "/grabpay/partner/v2/charge/{partnerTxID}/status?currency={currency}");
                    put(PathUtility.PATH_ONA_REFUND, "/grabpay/partner/v2/refund");
                    put(PathUtility.PATH_ONA_REFUND_STATUS,
                            "/grabpay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}");
                    put(PathUtility.PATH_ONA_ONE_TIME_CHARGE_STATUS,
                            "/grabpay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}");
                }
            };
            put("VN", vn);
            put("REGIONAL", regional);
        }
    };

    public Map<String, Map<PathUtility, String>> getURL() {
        return urls;
    }

    public Map<PathUtility, String> getURL(String country) {
        if (country.equals("VN"))
            return urls.get(country);
        return urls.get("REGIONAL");
    }
}