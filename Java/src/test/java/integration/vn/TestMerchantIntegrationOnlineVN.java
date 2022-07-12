package integration.vn;

import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.service.AuthorizationService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOnlineVN {
    private static AuthorizationService authorizationService;
    private static MerchantIntegrationOnline merchantIntegrationOnline;
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
    public void testApiChargeInit() {
        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String partnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);

        JSONObject[] items = new JSONObject[1];
        JSONObject item1 = new JSONObject();
        item1.put("itemName", "D&G hand bag1");
        item1.put("quantity", 140);
        items[0] = item1;

        JSONObject response = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, items, null, null);
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
