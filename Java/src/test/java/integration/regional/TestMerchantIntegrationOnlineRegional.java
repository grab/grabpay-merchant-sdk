package integration.regional;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOnlineRegional {
    private static MerchantIntegrationOnline merchantIntegrationOnline;

    @BeforeAll
    public static void setUp() {
        final EnvironmentType staging = EnvironmentType.STAGING;
        final Country country = Country.MALAYSIA;
        final String partnerId = System.getenv("MY_STG_OTC_PARTNER_ID");
        final String partnerSecret = System.getenv("MY_STG_OTC_PARTNER_SECRET");
        final String merchantId = System.getenv("MY_STG_OTC_MERCHANT_ID");
        final String clientId = System.getenv("MY_STG_OTC_CLIENT_ID");
        final String clientSecret = System.getenv("MY_STG_OTC_CLIENT_SECRET");
        final String redirectUri = "http://localhost:8888/welcome";

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
    public void testOnaChargeInit() {
        long amount = 20;
        String currency = "MYR";
        String description = "this is testing";

        String partnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);
        JSONObject response = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, null, null, null, null);

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
