package unit;

import com.merchantsdk.payment.MerchantIntegrationOnline;
import com.merchantsdk.payment.service.AuthorizationService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class TestMockMerchantOnline {

    private static MerchantIntegrationOnline merchantIntegrationOnline;
    private static AuthorizationService authorizationService;

    private static String staging = "STG";
    private static String country = "VN";
    private static String partner_id = "partner-id";
    private static String partner_secret = "partner-secret";
    private static String merchant_id = "merchant-id";
    private static String client_id = "client-id";
    private static String client_secret = "client-secret";
    private static String redirect_uri = "redirect-url";

    private static JSONObject metaInfo;
    private static JSONObject shipDetails;
    private static JSONObject[] items;

    private CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    @BeforeAll
    public static void setUp() {
        merchantIntegrationOnline = new MerchantIntegrationOnline(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                client_id,
                client_secret,
                redirect_uri);
        authorizationService = new AuthorizationService();

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

    @Mock
    CloseableHttpClient clientMock;

    @BeforeEach
    public void createMock() {
        merchantIntegrationOnline.getOnlineTransaction().getTransaction().setClient(clientMock);

        when(response.getCode()).thenReturn(200, 300, 401);
        when(response.getEntity())
                .thenReturn(new StringEntity("{\"Body\":\"You are in right place\",\"request\":\"This_is_request\"}"));
    }

    @Test
    public void testOnAChargeInit() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String partnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount,
                currency, description, null, items, null, null);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        // test metainfo, shipping != null, different currency
        currency = "SG";
        jsonResponse = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency,
                description, metaInfo, items, shipDetails, null);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testOnaOAuth2Token() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerTxID = authorizationService.getRandomString(32);
        String code = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaOAuth2Token(partnerTxID, code);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testOnaChargeComplete() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerTxID = authorizationService.getRandomString(32);
        String access_token = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaChargeComplete(partnerTxID, access_token);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testOnaGetChargeStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = authorizationService.getRandomString(32);
        String access_token = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaGetChargeStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.onaGetChargeStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testOnaRefund() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        long amount = 2000;
        String currency = "VND";
        String description = "this is testing";
        String refundPartnerTxID = authorizationService.getRandomString(32);
        String partnerGroupTxID = authorizationService.getRandomString(32);
        String txID = authorizationService.getRandomString(14);
        String access_token = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaRefund(refundPartnerTxID, partnerGroupTxID, amount,
                currency, txID, description, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));

        // different currency
        currency = "SG";
        amount = 21341234;
        jsonResponse = merchantIntegrationOnline.onaRefund(refundPartnerTxID, partnerGroupTxID, amount, currency, txID,
                description, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals("This_is_request", jsonResponse.get("request"));
    }

    @Test
    public void testOnaRefundStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = authorizationService.getRandomString(32);
        String access_token = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaGetRefundStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.onaGetRefundStatus(partnerTxID, currency, access_token);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals(2, jsonResponse.length());
    }

    @Test
    public void testOnaGetOTCStatus() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String currency = "VND";
        String partnerTxID = authorizationService.getRandomString(32);

        JSONObject jsonResponse = merchantIntegrationOnline.onaGetOTCStatus(partnerTxID, currency);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        currency = "SG";
        jsonResponse = merchantIntegrationOnline.onaGetOTCStatus(partnerTxID, currency);
        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

}
