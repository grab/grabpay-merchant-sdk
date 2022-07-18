package integration.vn;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOnlineVN {
    private static MerchantIntegrationOnline merchantIntegrationOnline;
    private static EnvironmentType staging = EnvironmentType.STAGING;
    private static Country country = Country.VIETNAM;
    private static String partnerId = System.getenv("VN_STG_OTC_PARTNER_ID");
    private static String partnerSecret = System.getenv("VN_STG_OTC_PARTNER_SECRET");
    private static String merchantId = System.getenv("VN_STG_OTC_MERCHANT_ID");
    private static String clientId = System.getenv("VN_STG_OTC_CLIENT_ID");
    private static String clientSecret = System.getenv("VN_STG_OTC_CLIENT_SECRET");
    private static String redirectUri = "http://localhost:8888/result";

    @BeforeAll
    public static void setUp() {
        merchantIntegrationOnline = new MerchantIntegrationOnline(
                staging,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                clientId,
                clientSecret,
                redirectUri);
    }

    @Test
    public void testApiChargeInit() {
        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String partnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);

        JSONObject[] items = new JSONObject[1];
        JSONObject item1 = new JSONObject();
        item1.put("itemName", "D&G hand bag1");
        item1.put("quantity", 140);
        items[0] = item1;

        JSONObject response = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, items, null, null);
        assert response.length() == 2;
        assertEquals(partnerTxID, response.get("partnerTxID"), "partnerTxID in response should equals in request");
        assertNotNull(response.get("request"));

        String[] hidePaymentMethods = { "POSTPAID" };
        partnerTxID = Utils.getRandomString(32);
        partnerGroupTxID = Utils.getRandomString(32);
        response = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, null, null, hidePaymentMethods);
        assert response.length() == 2;
        assertEquals(partnerTxID, response.get("partnerTxID"), "partnerTxID in response should equals in request");
        assertNotNull(response.get("request"));

    }
}
