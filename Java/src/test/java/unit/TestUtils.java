package unit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.json.JSONObject;

import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.exception.SDKRuntimeException;

public class TestUtils {

    @Test
    public void testMd5Hex() {
        String input = "";
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Utils.md5Hex(input));

        assertThrows(IllegalArgumentException.class, () -> {
            Utils.md5Hex(null);
        });

        input = "lakjshf123-089.klsajdfh";
        assertEquals("2da5bfaf7de739e4b2517e6162357fe5", Utils.md5Hex(input));
    }

    @Test
    public void testBase64URLEncode() {
        String input = "+++";
        String encode = Utils.base64URLEncode(input);
        assertEquals("---", encode);

        encode = Utils.base64URLEncode("abs");
        assertEquals("abs", encode);

        try {
            encode = Utils.base64URLEncode(null);
        } catch (NullPointerException e) {
            encode = "";
        }
        assertEquals("", encode);

        encode = Utils.base64URLEncode("poiuqwe+lkjasdfn12=234-//asfdjh12+");
        assertEquals("poiuqwe-lkjasdfn12234-__asfdjh12-", encode);
    }

    @Test
    public void testSha256Base64() {
        String encode = Utils.sha256Base64("abc");
        assertEquals("ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=", encode);

        encode = Utils.sha256Base64("");
        assertEquals("47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", encode);

        encode = Utils.sha256Base64("{name: john}");
        assertEquals("aEVXvRaiF6i4hftN0kZJMlJthLi62Q6DyO5NVliApKE=", encode);

        encode = Utils.sha256Base64(Utils.getRandomString(100));
        // why 44? 6 bit/char, 256/6 = 42,6 + pading ~ 44
        assert encode.length() == 44;

        try {
            encode = Utils.sha256Base64(null);
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

        assertTrue(actual.similar(Utils.mergeJsonObjects(o1, o2)));

        o1.clear();
        actual.clear();
        actual.put("work", "engineer");
        assertTrue(actual.similar(Utils.mergeJsonObjects(o1, o2)));

        o1.clear();
        o2.clear();
        actual.clear();
        assertTrue(actual.similar(Utils.mergeJsonObjects(o1, o2)));

        o1.put("name", "john");
        o1.put("work", "engineer");
        o1.put("company", "moca");

        o2.put("year", "20 years old");
        o2.put("company", "grab");

        actual.put("name", "john");
        actual.put("work", "engineer");
        actual.put("company", "grab");
        actual.put("year", "20 years old");

        assertTrue(actual.similar(Utils.mergeJsonObjects(o1, o2)));

        assertFalse(actual.similar(Utils.mergeJsonObjects(o2, o1)));

        o1 = null;
        actual = o2;
        assertTrue(actual.similar(Utils.mergeJsonObjects(o1, o2)));

        assertTrue(actual.similar(Utils.mergeJsonObjects(o2, o1)));
    }

    @Test
    public void testGetRandomString() {
        assert 32 == Utils.getRandomString(32).length();
        String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String rd = Utils.getRandomString(500);
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
    public void testFormattedDateTime() {
        Instant now = Instant.now();
        String formatted = Utils.getFormattedDateTime(now);
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

}
