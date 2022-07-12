package integration.vn;

import com.merchantsdk.payment.config.ApiUrl;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.service.AuthorizationService;
import com.merchantsdk.payment.service.OfflineTransaction;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class TestOfflineTransaction {
    private static AuthorizationService authorizationService;
    private static OfflineTransaction offlineTransaction;
    private static Config config;

    private static final String staging = "STG";
    private static final String country = "VN";
    private static final String partner_id = System.getenv("VN_STG_POS_PARTNER_ID");
    private static final String partner_secret = System.getenv("VN_STG_POS_PARTNER_SECRET");
    private static final String merchant_id = System.getenv("VN_STG_POS_MERCHANT_ID");
    private static final String terminal_id = System.getenv("VN_STG_POS_TERMINAL_ID");

    @BeforeAll
    public static void setUp() {
        config = new Config(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                terminal_id,
                "",
                "",
                "");
        config.setGrab_id(merchant_id);
        offlineTransaction = new OfflineTransaction(config, new ApiUrl().getURL(config.getCountry()));
        authorizationService = new AuthorizationService();
    }

    @Test
    public void testOfflineTransaction() throws Exception {
        String partnerTxID = "partner-" + authorizationService.getRandomString(24);
        String msg = authorizationService.getRandomString(32);
        String msgID = authorizationService.generateMD5(msg);

        long amount = 10000;
        CloseableHttpResponse qrCode = offlineTransaction.apiCreateQrCode(msgID, partnerTxID, amount, "VND");

        assert qrCode.getCode() == 200;

        HttpEntity responseEntity = qrCode.getEntity();
        String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        JSONObject responseBody = new JSONObject(responseString);
        Set<String> keys = responseBody.keySet();
        assert keys.contains("msgID");
        assert keys.contains("qrcode");
        assert keys.contains("txID");
        assert keys.contains("qrid");
        assert keys.contains("expiryTime");

        CloseableHttpResponse getTx = offlineTransaction.apiPosGetTxnStatus(msgID, partnerTxID, "VND");
        assert getTx.getCode() >= 400;

        CloseableHttpResponse refundGetTx = offlineTransaction.apiPosGetTxnStatus(msgID, partnerTxID, "VND");
        assert refundGetTx.getCode() >= 400;
    }
}
