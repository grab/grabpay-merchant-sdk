package integration.regional;

import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.service.AuthorizationService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOnlineRegional {
    private static AuthorizationService authorizationService;
    private static MerchantIntegrationOnline merchantIntegrationOnline;

    @BeforeAll
    public static void setUp() {
        final String staging = "STG";
        final String country = "MY";
        final String partner_id = System.getenv("MY_STG_OTC_PARTNER_ID");
        final String partner_secret = System.getenv("MY_STG_OTC_PARTNER_SECRET");
        final String merchant_id = System.getenv("MY_STG_OTC_MERCHANT_ID");
        final String client_id = System.getenv("MY_STG_OTC_CLIENT_ID");
        final String client_secret = System.getenv("MY_STG_OTC_CLIENT_SECRET");
        final String redirect_uri = "http://localhost:8888/welcome";

        merchantIntegrationOnline = new MerchantIntegrationOnline(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                client_id,
                client_secret,
                redirect_uri);
        authorizationService = new AuthorizationService();
    }

    @Test
    public void testOnaChargeInit() {
        long amount = 20;
        String currency = "MYR";
        String description = "this is testing";

        String partnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);
        JSONObject response = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, null, null, null);

        assert response.length() == 2;
        assertEquals(partnerTxID, response.get("partnerTxID"), "partnerTxID in response should equals in request");
        assertNotNull(response.get("request"));

        String[] hidePaymentMethods = { "POSTPAID" };
        partnerTxID = authorizationService.getRandomString(32);
        partnerGroupTxID = authorizationService.getRandomString(32);
        response = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, null, null, hidePaymentMethods);
        assert response.length() == 2;
        assertEquals(partnerTxID, response.get("partnerTxID"), "partnerTxID in response should equals in request");
        assertNotNull(response.get("request"));
    }
}
