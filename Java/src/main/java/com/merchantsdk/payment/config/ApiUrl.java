package com.merchantsdk.payment.config;

import java.util.HashMap;
import java.util.Map;

import com.merchantsdk.payment.Country;

public class ApiUrl {
        public static enum ApiEndpoint {
                OAUTH_TOKEN,
                POS_CREATE_QR_CODE,
                POS_CANCEL_TRANSACTION,
                POS_REFUND_TRANSACTION,
                POS_PERFORM_TRANSACTION,
                POS_TRANSACTION_DETAILS,

                ONA_CHARGE_INIT,
                ONA_CHARGE_COMPLETE,
                ONA_CHARGE_STATUS,
                ONA_REFUND,
                ONA_REFUND_STATUS,
                ONA_ONE_TIME_CHARGE_STATUS
        }

        private static final Map<Country, Map<ApiEndpoint, String>> countryUrls = new HashMap<Country, Map<ApiEndpoint, String>>() {
                {
                        put(Country.VIETNAM, new HashMap<ApiEndpoint, String>() {
                                {
                                        put(ApiEndpoint.OAUTH_TOKEN, "/grabid/v1/oauth2/token");

                                        put(ApiEndpoint.POS_CREATE_QR_CODE,
                                                        "/mocapay/partners/v1/terminal/qrcode/create");
                                        put(ApiEndpoint.POS_CANCEL_TRANSACTION,
                                                        "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/cancel");
                                        put(ApiEndpoint.POS_REFUND_TRANSACTION,
                                                        "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/refund");
                                        put(ApiEndpoint.POS_PERFORM_TRANSACTION,
                                                        "/mocapay/partners/v1/terminal/transaction/perform");
                                        put(ApiEndpoint.POS_TRANSACTION_DETAILS,
                                                        "/mocapay/partners/v1/terminal/transaction/{partnerTxID}");

                                        put(ApiEndpoint.ONA_CHARGE_INIT, "/mocapay/partner/v2/charge/init");
                                        put(ApiEndpoint.ONA_CHARGE_COMPLETE, "/mocapay/partner/v2/charge/complete");
                                        put(ApiEndpoint.ONA_CHARGE_STATUS,
                                                        "/mocapay/partner/v2/charge/{partnerTxID}/status");
                                        put(ApiEndpoint.ONA_REFUND, "/mocapay/partner/v2/refund");
                                        put(ApiEndpoint.ONA_REFUND_STATUS,
                                                        "/mocapay/partner/v2/refund/{partnerTxID}/status");
                                        put(ApiEndpoint.ONA_ONE_TIME_CHARGE_STATUS,
                                                        "/mocapay/partner/v2/one-time-charge/{partnerTxID}/status");
                                }
                        });
                }
        };

        private static final Map<ApiEndpoint, String> fallbackApiUrlSet = new HashMap<ApiEndpoint, String>() {
                {
                        put(ApiEndpoint.OAUTH_TOKEN, "/grabid/v1/oauth2/token");

                        put(ApiEndpoint.POS_CREATE_QR_CODE,
                                        "/grabpay/partner/v1/terminal/qrcode/create");
                        put(ApiEndpoint.POS_CANCEL_TRANSACTION,
                                        "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/cancel");
                        put(ApiEndpoint.POS_REFUND_TRANSACTION,
                                        "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/refund");
                        put(ApiEndpoint.POS_PERFORM_TRANSACTION,
                                        "/grabpay/partner/v1/terminal/transaction/perform");
                        put(ApiEndpoint.POS_TRANSACTION_DETAILS,
                                        "/grabpay/partner/v1/terminal/transaction/{partnerTxID}");

                        put(ApiEndpoint.ONA_CHARGE_INIT, "/grabpay/partner/v2/charge/init");
                        put(ApiEndpoint.ONA_CHARGE_COMPLETE,
                                        "/grabpay/partner/v2/charge/complete");
                        put(ApiEndpoint.ONA_CHARGE_STATUS,
                                        "/grabpay/partner/v2/charge/{partnerTxID}/status");
                        put(ApiEndpoint.ONA_REFUND, "/grabpay/partner/v2/refund");
                        put(ApiEndpoint.ONA_REFUND_STATUS,
                                        "/grabpay/partner/v2/refund/{partnerTxID}/status");
                        put(ApiEndpoint.ONA_ONE_TIME_CHARGE_STATUS,
                                        "/grabpay/partner/v2/one-time-charge/{partnerTxID}/status");
                }
        };

        public final static Map<ApiEndpoint, String> getUrlSet(Country country) {
                if (countryUrls.containsKey(country)) {
                        return countryUrls.get(country);
                }
                return fallbackApiUrlSet;
        }
}