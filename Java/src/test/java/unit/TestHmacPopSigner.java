package unit;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.OtcConfig;
import com.merchantsdk.payment.service.HmacPopSigner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

public class TestHmacPopSigner {
    @Test
    public void testBuildPopSignature() {
        OtcConfig config = new OtcConfig(
                EnvironmentType.STAGING,
                Country.VIETNAM,
                "partner-id",
                "partner-secret",
                "merchant-id",
                "client-id",
                "client-secret",
                "http://localhost:8888");
        HmacPopSigner signer = new HmacPopSigner(config);
        String accessToken = "some-access-token";
        Instant timestamp = Instant.ofEpochSecond(1640000000);

        String popSignature = signer.buildPopSignature(accessToken, timestamp);

        assertEquals(popSignature,
                "eyJzaWciOiJYOW95MXpaRWJKVzlzNEJZNG93U09uOGlLUEhVUGJMMGt6aUFwWGM1YzM4IiwidGltZV9zaW5jZV9lcG9jaCI6MTY0MDAwMDAwMH0");
        assertEquals(111, popSignature.length());
    }
}
