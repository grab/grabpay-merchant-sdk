package integration.vn;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMerchantIntegrationOfflineVN {
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    @BeforeAll
    public static void setUp() {
        final EnvironmentType staging = EnvironmentType.STAGING;
        final Country country = Country.VIETNAM;
        final String partnerId = System.getenv("VN_STG_POS_PARTNER_ID");
        final String partnerSecret = System.getenv("VN_STG_POS_PARTNER_SECRET");
        final String merchantId = System.getenv("VN_STG_POS_MERCHANT_ID");
        final String terminalId = System.getenv("VN_STG_POS_TERMINAL_ID");

        merchantIntegrationOffline = new MerchantIntegrationOffline(
                staging,
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
        String currency = "VND";
        JSONObject qrCode = merchantIntegrationOffline.createQrCode(msgID, partnerTxID, amount, currency);

        assertNotNull(qrCode.get("msgID"));
        assertEquals(msgID, qrCode.get("msgID"), "msgId should be same in request and response");
        assertNotNull(qrCode.get("qrcode"));
        assertNotNull(qrCode.get("txID"));
        assertNotNull(qrCode.get("qrid"));
        assertNotNull(qrCode.get("expiryTime"));

        String key = "error";
        JSONObject getTx = merchantIntegrationOffline.getTxnDetails(msgID, partnerTxID, currency);
        // will get 401 because payer has not scanned QR code
        assertTrue(getTx.has(key));
        assertTrue(getTx.getInt(key) >= 400);

        JSONObject refundGetTx = merchantIntegrationOffline.getRefundDetails(msgID, partnerTxID, currency);
        assertTrue(refundGetTx.getInt(key) >= 400);
    }
}
