package integration.vn;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.service.OfflineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class TestOfflineTransaction {
    private static OfflineTransaction offlineTransaction;
    private static PosConfig config;

    private static final EnvironmentType staging = EnvironmentType.STAGING;
    private static final Country country = Country.VIETNAM;
    private static final String partner_id = System.getenv("VN_STG_POS_PARTNER_ID");
    private static final String partner_secret = System.getenv("VN_STG_POS_PARTNER_SECRET");
    private static final String merchant_id = System.getenv("VN_STG_POS_MERCHANT_ID");
    private static final String terminal_id = System.getenv("VN_STG_POS_TERMINAL_ID");

    @BeforeAll
    public static void setUp() {
        config = new PosConfig(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                terminal_id);
        offlineTransaction = new OfflineTransaction(config);
    }

    @Test
    public void testOfflineTransaction() throws Exception {
        String partnerTxID = "partner-" + Utils.getRandomString(24);
        String msgID = Utils.getRandomString(32);

        long amount = 10000;
        CloseableHttpResponse qrCode = offlineTransaction.createQrCode(msgID, partnerTxID, amount, "VND");

        assertEquals(qrCode.getCode(), 200);

        HttpEntity responseEntity = qrCode.getEntity();
        String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        JSONObject responseBody = new JSONObject(responseString);
        Set<String> keys = responseBody.keySet();
        assertTrue(keys.contains("msgID"));
        assertTrue(keys.contains("qrcode"));
        assertTrue(keys.contains("txID"));
        assertTrue(keys.contains("qrid"));
        assertTrue(keys.contains("expiryTime"));

        CloseableHttpResponse getTx = offlineTransaction.getTxnStatus(msgID, partnerTxID, "VND");
        assert getTx.getCode() >= 400;

        CloseableHttpResponse refundGetTx = offlineTransaction.getTxnStatus(msgID, partnerTxID, "VND");
        assert refundGetTx.getCode() >= 400;
    }
}
