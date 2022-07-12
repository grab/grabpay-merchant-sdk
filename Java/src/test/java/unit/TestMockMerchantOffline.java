package unit;

import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.service.AuthorizationService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
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
public class TestMockMerchantOffline {

    private static MerchantIntegrationOffline merchantIntegrationOffline;
    private static AuthorizationService authorizationService;

    private static String staging = "STG";
    private static String country = "VN";
    private static String partner_id = "partner-id";
    private static String partner_secret = "partner-secret";
    private static String merchant_id = "merchant-id";
    private static String terminal_id = "terminal-id";

    private static JSONObject metaInfo;
    private static JSONObject shipDetails;
    private static JSONObject items;

    private CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    @BeforeAll
    public static void setUp() {
        merchantIntegrationOffline = new MerchantIntegrationOffline(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                terminal_id);
        authorizationService = new AuthorizationService();

        shipDetails = new JSONObject();
        shipDetails.put("shippingDetails", "Some country");
        items = new JSONObject();
        items.put("items", "clothes");

        metaInfo = new JSONObject();
        metaInfo.put("shippingDetails", "Some country");
        metaInfo.put("items", "clothes");
        metaInfo.put("metaInfo", new JSONObject[] { shipDetails, items });
    }

    @Mock
    CloseableHttpClient clientMock;

    @BeforeEach
    public void createMock() throws Exception {
        merchantIntegrationOffline.getOfflineTransaction().getTransaction().setClient(clientMock);

        when(response.getCode()).thenReturn(200, 300, 401);
        when(response.getEntity())
                .thenReturn(new StringEntity("{\"Body\":\"You are in right place\",\"request\":\"This_is_request\"}"));
    }

    @Test
    public void testPosCreateQRCode() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerId = authorizationService.getRandomString(32);
        long amount = 2000;
        String currency = "VND";

        // test null msgID
        JSONObject jsonResponse = merchantIntegrationOffline.posCreateQRCode(null, partnerId, amount, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = authorizationService.generateMsgID(null);
        jsonResponse = merchantIntegrationOffline.posCreateQRCode(msgID, partnerId, amount, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPosPerformQRCode() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerTxId = authorizationService.getRandomString(32);
        long amount = 2000;
        String currency = "VND";
        String code = "code";
        JSONObject jsonResponse = merchantIntegrationOffline.posPerformQRCode(null, partnerTxId, amount, currency,
                code);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = authorizationService.generateMsgID(null);
        jsonResponse = merchantIntegrationOffline.posPerformQRCode(msgID, partnerTxId, amount, currency, code);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPosCancel() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String partnerTxId = authorizationService.getRandomString(32);
        String origPartnerTxID = "origPartnerTxID";
        String origTxID = "origTxID";
        String currency = "VND";
        JSONObject jsonResponse = merchantIntegrationOffline.posCancel(null, partnerTxId, origPartnerTxID, origTxID,
                currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = authorizationService.generateMsgID(null);
        jsonResponse = merchantIntegrationOffline.posCancel(msgID, partnerTxId, origPartnerTxID, origTxID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPosRefund() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String refundPartnerTxID = "refundPartnerTxID";
        String origPartnerTxID = "origPartnerTxID";
        long amount = 2000;
        String currency = "VND";
        String msgID = authorizationService.generateMsgID(null);
        JSONObject jsonResponse = merchantIntegrationOffline.posRefund(msgID, refundPartnerTxID, amount, currency,
                origPartnerTxID, null);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPosGetTxnDetails() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String partnerID = authorizationService.getRandomString(32);
        String currency = "VND";
        String msgID = authorizationService.generateMsgID(null);
        JSONObject jsonResponse = merchantIntegrationOffline.posGetTxnDetails(msgID, partnerID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPosGetRefundDetails() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String refundPartnerTxID = authorizationService.getRandomString(32);
        String currency = "VND";
        String msgID = authorizationService.generateMsgID(null);
        JSONObject jsonResponse = merchantIntegrationOffline.posGetRefundDetails(msgID, refundPartnerTxID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }
}
