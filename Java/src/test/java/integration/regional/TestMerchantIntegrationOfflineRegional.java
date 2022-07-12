package integration.regional;

import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.service.AuthorizationService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOfflineRegional {
    private static AuthorizationService authorizationService;
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    @BeforeAll
    public static void setUp() {
        final String staging = "STG";
        final String country = "PH";
        final String partner_id = System.getenv("PH_STG_POS_PARTNER_ID");
        final String partner_secret = System.getenv("PH_STG_POS_PARTNER_SECRET");
        final String merchant_id = System.getenv("PH_STG_POS_MERCHANT_ID");
        final String terminal_id = System.getenv("PH_STG_POS_TERMINAL_ID");

        merchantIntegrationOffline = new MerchantIntegrationOffline(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                terminal_id);
        authorizationService = new AuthorizationService();
    }

    @Test
    public void testCreateQrCode() {
        String partnerTxID = "partner-" + authorizationService.getRandomString(24);
        String msg = authorizationService.getRandomString(32);
        String msgID = authorizationService.generateMD5(msg);

        long amount = 10000;
        String currency = "PHP";
        JSONObject qrCode = merchantIntegrationOffline.posCreateQRCode(msgID, partnerTxID, amount, currency);

        // assertEquals(6,qrCode.length());
        assertNotNull(qrCode.get("msgID"));
        assertEquals(msgID, qrCode.get("msgID"), "msgId should be same in request and response");
        assertNotNull(qrCode.get("qrcode"));
        assertNotNull(qrCode.get("txID"));
        assertNotNull(qrCode.get("qrid"));
        assertNotNull(qrCode.get("expiryTime"));

        String key = "Error";
        JSONObject getTx = merchantIntegrationOffline.posGetTxnDetails(msgID, partnerTxID, currency);
        assertTrue((Integer) getTx.get(key) >= 400);

        JSONObject refundGetTx = merchantIntegrationOffline.posGetRefundDetails(msgID, partnerTxID, currency);
        assertTrue((Integer) refundGetTx.get(key) >= 400);
    }
}
