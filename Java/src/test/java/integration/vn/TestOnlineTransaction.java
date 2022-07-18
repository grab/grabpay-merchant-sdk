package integration.vn;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.OtcConfig;
import com.merchantsdk.payment.service.OnlineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestOnlineTransaction {
    private static OnlineTransaction onlineTransaction;
    private static OtcConfig config;

    private static EnvironmentType env = EnvironmentType.STAGING;
    private static Country country = Country.VIETNAM;
    private static String partnerId = System.getenv("VN_STG_OTC_PARTNER_ID");
    private static String partnerSecret = System.getenv("VN_STG_OTC_PARTNER_SECRET");
    private static String merchantId = System.getenv("VN_STG_OTC_MERCHANT_ID");
    private static String clientId = System.getenv("VN_STG_OTC_CLIENT_ID");
    private static String clientSecret = System.getenv("VN_STG_OTC_CLIENT_SECRET");
    private static String redirectUri = "http://localhost:8888/result";

    @BeforeAll
    public static void setUp() {
        config = new OtcConfig(
                env,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                clientId,
                clientSecret,
                redirectUri);
        onlineTransaction = new OnlineTransaction(config);
    }

    @Test
    public void testApiChargeInit() {
        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";

        String partnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);

        CloseableHttpResponse response = onlineTransaction.chargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, null, null, null, null);

        assertEquals(response.getCode(), 200);

        // test hidPaymentMethods
        String[] hidePaymentMethods = { "INSTALMENT" };
        partnerTxID = Utils.getRandomString(32);
        partnerGroupTxID = Utils.getRandomString(32);
        response = onlineTransaction.chargeInit(partnerTxID, partnerGroupTxID, amount, currency, description,
                null, null, null, hidePaymentMethods);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testOnlineTrancsaction() throws Exception {
        long amount = 2000;
        String currency = "VND";
        String codeVerifier = "codeVerifier";
        String description = "this is testing";
        String partnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);

        String respWebURL = onlineTransaction.generateWebUrl(partnerTxID, partnerGroupTxID, amount, currency,
                codeVerifier,
                description, null, null, null, null, null);
        assertNotNull(respWebURL);
        assertFalse(respWebURL.isEmpty());
    }
}