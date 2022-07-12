package integration.vn;

import com.merchantsdk.payment.config.ApiUrl;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.service.AuthorizationService;
import com.merchantsdk.payment.service.OnlineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestOnlineTransaction {
    private static AuthorizationService authorizationService;
    private static OnlineTransaction onlineTransaction;
    private static Config config;

    private static String staging = "STG";
    private static String country = "VN";
    private static String partner_id = System.getenv("VN_STG_OTC_PARTNER_ID");
    private static String partner_secret = System.getenv("VN_STG_OTC_PARTNER_SECRET");
    private static String merchant_id = System.getenv("VN_STG_OTC_MERCHANT_ID");
    private static String client_id = System.getenv("VN_STG_OTC_CLIENT_ID");
    private static String client_secret = System.getenv("VN_STG_OTC_CLIENT_SECRET");
    private static String redirect_uri = "http://localhost:8888/result";

    @BeforeAll
    public static void setUp() {
        config = new Config(
            staging,
            country,
            partner_id,
            partner_secret,
            merchant_id,
            "",
            client_id,
            client_secret,
            redirect_uri);
        onlineTransaction = new OnlineTransaction(config, new ApiUrl().getURL(config.getCountry()));
        authorizationService = new AuthorizationService();
    }

    @Test
    public void testApiChargeInit() {
        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";

        String partnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);

        CloseableHttpResponse response = onlineTransaction.apiChargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, null, null, null, null);

        assert response.getCode() == 200;

        // test hidPaymentMethods
        String[] hidePaymentMethods = { "INSTALMENT" };
        partnerTxID = authorizationService.getRandomString(32);
        partnerGroupTxID = authorizationService.getRandomString(32);
        response = onlineTransaction.apiChargeInit(partnerTxID, partnerGroupTxID, amount, currency, description,
                null, null, null, hidePaymentMethods);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testOnlineTrancsaction() {
        long amount = 2000;
        String currency = "VND";
        String codeVerifier = "codeVerifier";
        String description = "this is testing";
        String partnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);

        String respWebURL = onlineTransaction.apiGenerateWebUrl(partnerTxID, partnerGroupTxID, amount, currency,
                codeVerifier,
                description, null, null, null, null, null);
        assertNotNull(respWebURL);
        assertFalse(respWebURL.isEmpty());
    }
}