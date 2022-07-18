package integration.regional;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOfflineRegional {
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    @BeforeAll
    public static void setUp() {
        final EnvironmentType env = EnvironmentType.STAGING;
        final Country country = Country.PHILIPPINES;
        final String partnerId = System.getenv("PH_STG_POS_PARTNER_ID");
        final String partnerSecret = System.getenv("PH_STG_POS_PARTNER_SECRET");
        final String merchantId = System.getenv("PH_STG_POS_MERCHANT_ID");
        final String terminalId = System.getenv("PH_STG_POS_TERMINAL_ID");

        merchantIntegrationOffline = new MerchantIntegrationOffline(
                env,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                terminalId);
    }

    @Test
    public void testCreateQrCode() {
        String partnerTxID = "partner-" + Utils.getRandomString(24);
        String msgID = Utils.getRandomString(32);

        long amount = 10000;
        String currency = "PHP";
        JSONObject qrCode = merchantIntegrationOffline.createQrCode(msgID, partnerTxID, amount, currency);

        // assertEquals(6,qrCode.length());
        assertNotNull(qrCode.get("msgID"));
        assertEquals(msgID, qrCode.get("msgID"), "msgId should be same in request and response");
        assertNotNull(qrCode.get("qrcode"));
        assertNotNull(qrCode.get("txID"));
        assertNotNull(qrCode.get("qrid"));
        assertNotNull(qrCode.get("expiryTime"));

        String key = "error";
        JSONObject getTx = merchantIntegrationOffline.getTxnDetails(msgID, partnerTxID, currency);
        assertTrue(getTx.has(key));
        assertTrue(getTx.getInt(key) >= 400);

        JSONObject refundGetTx = merchantIntegrationOffline.getRefundDetails(msgID, partnerTxID, currency);
        assertTrue(refundGetTx.getInt(key) >= 400);
    }
}
