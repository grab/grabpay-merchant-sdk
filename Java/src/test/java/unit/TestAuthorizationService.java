package unit;

import java.time.Instant;

import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.exception.SDKRuntimeException;
import com.merchantsdk.payment.service.AuthorizationService;

import org.json.JSONObject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestAuthorizationService {
    private static AuthorizationService authorizationService;
    private static Config config;

    @BeforeAll
    public static void setup() {
        config = new Config(
                "STG",
                "VN",
                "partner-id",
                "partner-secret",
                "merchant-id",
                "terminal-id",
                "client-id",
                "client-secret",
                "http://localhost:8888/result");
        authorizationService = new AuthorizationService();
    }

    @Test
    public void testBase64URLEncode() {
        String input = "+++";
        String encode = authorizationService.base64URLEncode(input);
        assertEquals("---", encode);

        encode = authorizationService.base64URLEncode("abs");
        assertEquals("abs", encode);

        try {
            encode = authorizationService.base64URLEncode(null);
        } catch (NullPointerException e) {
            encode = "";
        }
        assertEquals("", encode);

        encode = authorizationService.base64URLEncode("poiuqwe+lkjasdfn12=234-//asfdjh12+");
        assertEquals("poiuqwe-lkjasdfn12234-__asfdjh12-", encode);
    }

    @Test
    public void testSha265() {
        String encode = authorizationService._sha265("abc");
        assertEquals("ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=", encode);

        encode = authorizationService._sha265("");
        assertEquals("47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", encode);

        encode = authorizationService._sha265("{name: john}");
        assertEquals("aEVXvRaiF6i4hftN0kZJMlJthLi62Q6DyO5NVliApKE=", encode);

        encode = authorizationService._sha265(authorizationService.getRandomString(100));
        // why 44? 6 bit/char, 256/6 = 42,6 + pading ~ 44
        assert encode.length() == 44;

        try {
            encode = authorizationService._sha265(null);
            assertTrue(encode.isEmpty());
        } catch (Exception e) {
            assertTrue(e instanceof SDKRuntimeException);
        }
    }

    @Test
    public void testMerge2JSONObject() {
        JSONObject o1 = new JSONObject();
        JSONObject o2 = new JSONObject();
        o1.put("name", "john");
        o2.put("work", "engineer");

        JSONObject actual = new JSONObject();
        actual.put("name", "john");
        actual.put("work", "engineer");

        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o1, o2)));

        o1.clear();
        actual.clear();
        actual.put("work", "engineer");
        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o1, o2)));

        o1.clear();
        o2.clear();
        actual.clear();
        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o1, o2)));

        o1.put("name", "john");
        o1.put("work", "engineer");
        o1.put("company", "moca");

        o2.put("year", "20 years old");
        o2.put("company", "grab");

        actual.put("name", "john");
        actual.put("work", "engineer");
        actual.put("company", "grab");
        actual.put("year", "20 years old");

        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o1, o2)));

        assertFalse(actual.similar(AuthorizationService.merge_jsonObj(o2, o1)));

        o1 = null;
        actual = o2;
        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o1, o2)));

        assertTrue(actual.similar(AuthorizationService.merge_jsonObj(o2, o1)));
    }

    @Test
    public void testGetRandomString() {
        assert 32 == authorizationService.getRandomString(32).length();
        String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String rd = authorizationService.getRandomString(500);
        assert 500 == rd.length();
        boolean ok = true;
        for (int i = 0; i < rd.length(); i++) {
            if (!charSet.contains(rd.substring(i, i + 1))) {
                ok = false;
                break;
            }
        }
        assertTrue(ok);
    }

    @Test
    public void testGenerateMD5() {
        String input = "";
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", authorizationService.generateMD5(input));

        assertEquals("", authorizationService.generateMD5(null));

        input = "lakjshf123-089.klsajdfh";
        assertEquals("2da5bfaf7de739e4b2517e6162357fe5", authorizationService.generateMD5(input));
    }

    @Test
    public void testFormattedDateTime() {
        Instant now = Instant.now();
        String formatted = authorizationService.getFormattedDateTime(now);
        assertTrue(formatted.contains("GMT"));
        assert formatted.length() == 29;

        String[] part = formatted.split("[ ]");
        assertEquals(6, part.length);
        assertEquals(4, part[0].length());

        assertEquals(2, part[1].length());
        assertEquals(3, part[2].length());
        assertEquals(4, part[3].length());
        assertEquals(8, part[4].length());
    }

    @Test
    public void testGeneratePOPSig() {
        String access_token = "some-access-token";
        Instant timestamp = Instant.now();

        String pops = authorizationService.generatePOPSig(config, access_token, timestamp);

        assertEquals(111, pops.length());
    }

    @Test
    public void testGenerateHmac() {
        JSONObject reqBody = new JSONObject();
        reqBody.put("john", "wick");
        String jsonBody = reqBody.toString();
        String hmac = authorizationService.generateHmac(config, "POST", "", "application/json", jsonBody,
                Instant.now());
        assertEquals(44, hmac.length());
    }

    @Test
    public void testGenerateMsgID() {
        String msgId = "qwertyuiopasdfghjklzxcvbnm123456";
        String actual = authorizationService.generateMsgID(msgId);
        assertEquals(msgId, actual);

        msgId = "abxy";
        actual = authorizationService.generateMsgID(msgId);
        assertNotEquals(msgId, actual);
        assertEquals(32, actual.length());

        msgId = null;
        actual = authorizationService.generateMsgID(msgId);
        assertEquals(32, actual.length());
    }

    @Test
    public void testGenerateCodeVerifier() {
        String codeVerifier = "haha";
        String actual = authorizationService.generateCodeVerifier(codeVerifier);
        assertTrue(actual.length() >= 43 && actual.length() <= 128,
                "actual codeVerifier should have valid length[43-128]");

        codeVerifier = "xyx-xyx-xyx-xyx-xyx-xyx-xyx-xyx-xyx-xyx-xyx";
        actual = authorizationService.generateCodeVerifier(codeVerifier);
        assertTrue(actual.length() >= 43 && actual.length() <= 128,
                "actual codeVerifier should have valid length[43-128]");
        assertEquals(codeVerifier, actual, "actual codeVerifier should keep the same if valid length");
    }
}