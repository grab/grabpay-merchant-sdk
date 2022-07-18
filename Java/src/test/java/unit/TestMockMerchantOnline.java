package unit;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class TestMockMerchantOnline {

    private static MerchantIntegrationOnline merchantIntegrationOnline;

    private static EnvironmentType env = EnvironmentType.STAGING;
    private static Country country = Country.VIETNAM;
    private static String partnerId = "partner-id";
    private static String partnerSecret = "partner-secret";
    private static String merchantId = "merchant-id";
    private static String clientId = "client-id";
    private static String clientSecret = "client-secret";
    private static String redirectUri = "redirect-url";

    private static JSONObject metaInfo;
    private static JSONObject shipDetails;
    private static JSONObject[] items;

    private CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    @Mock
    CloseableHttpClient clientMock;

    MockedStatic<HttpClients> httpClientsMock;

    @BeforeEach
    public void createMock() {
        httpClientsMock = mockStatic(HttpClients.class);
        when(HttpClients.createDefault()).thenReturn(clientMock);

        when(response.getCode()).thenReturn(200, 300, 401);
        when(response.getEntity())
                .thenReturn(new StringEntity("{\"Body\":\"You are in right place\",\"request\":\"This_is_request\"}"));

        merchantIntegrationOnline = new MerchantIntegrationOnline(
                env,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                clientId,
                clientSecret,
                redirectUri);

        shipDetails = new JSONObject();
        shipDetails.put("shippingDetails", "Some country");

        items = new JSONObject[1];
        JSONObject item1 = new JSONObject();
        item1.put("itemName", "D&G hand bag1");
        item1.put("quantity", 140);
        items[0] = item1;

        metaInfo = new JSONObject();
        metaInfo.put("shippingDetails", "Some country");
        metaInfo.put("items", "clothes");
        metaInfo.put("metaInfo", new JSONObject[] { shipDetails, item1 });
    }

    @AfterEach
    public void tearDown() {
        httpClientsMock.close();
    }

    @Test
    public void testChargeInit() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String partnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, null, items, null, null);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        // test metainfo, shipping != null, different currency
        currency = "SG";
        jsonResponse = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, metaInfo, items, shipDetails, null);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testGetOAuth2Token() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String code = Utils.getRandomString(32);
        String codeVerifier = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.getOAuth2Token(code, codeVerifier);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testChargeComplete() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerTxID = Utils.getRandomString(32);
        String access_token = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.chargeComplete(partnerTxID, access_token);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testGetChargeStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = Utils.getRandomString(32);
        String access_token = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.getChargeStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.getChargeStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testRefund() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String refundPartnerTxID = Utils.getRandomString(32);
        String partnerGroupTxID = Utils.getRandomString(32);
        String txID = Utils.getRandomString(14);
        String access_token = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.refund(refundPartnerTxID, partnerGroupTxID, amount,
                currency, txID, description, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));

        // different currency
        currency = "SG";
        amount = 21341234;
        jsonResponse = merchantIntegrationOnline.refund(refundPartnerTxID, partnerGroupTxID, amount, currency, txID,
                description, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testGetRefundStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = Utils.getRandomString(32);
        String access_token = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.getRefundStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.getRefundStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals(2, jsonResponse.length());
    }

    @Test
    public void testGetOtcStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = Utils.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.getOtcStatus(partnerTxID, currency);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.getOtcStatus(partnerTxID, currency);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

}
