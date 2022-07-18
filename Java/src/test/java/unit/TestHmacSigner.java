package unit;

import org.json.JSONObject;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.service.HmacSigner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

public class TestHmacSigner {
    @Test
    public void testGenerateHmac() {
        PosConfig config = new PosConfig(
                EnvironmentType.STAGING,
                Country.VIETNAM,
                "partner-id",
                "partner-secret",
                "merchant-id",
                "terminal-id");
        HmacSigner signer = new HmacSigner(config);
        JSONObject reqBody = new JSONObject();
        reqBody.put("john", "wick");
        String jsonBody = reqBody.toString();
        // as a snapshot for regression
        String hmac = signer.buildHmacSignature("POST", "", "application/json", jsonBody,
                Instant.ofEpochSecond(1640000000));

        assertEquals(hmac, "WohtaZ1XOf/mTCDx1ncJsS4R8h+f0bfzKagkesQB1Qg=");
        assertEquals(44, hmac.length());
    }
}
