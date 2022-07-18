package unit;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.Utils;
import com.merchantsdk.payment.config.EnvironmentType;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
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
public class TestMockMerchantOffline {
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    private static EnvironmentType env = EnvironmentType.STAGING;
    private static Country country = Country.VIETNAM;
    private static String partnerId = "partner-id";
    private static String partnerSecret = "partner-secret";
    private static String merchantId = "merchant-id";
    private static String terminalId = "terminal-id";

    private static JSONObject metaInfo;
    private static JSONObject shipDetails;
    private static JSONObject items;

    private CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    @Mock
    CloseableHttpClient clientMock;

    MockedStatic<HttpClients> httpClientsMock;

    @BeforeEach
    public void setup() throws Exception {
        httpClientsMock = mockStatic(HttpClients.class);
        when(HttpClients.createDefault()).thenReturn(clientMock);

        when(response.getCode()).thenReturn(200, 300, 401);
        when(response.getEntity())
                .thenReturn(new StringEntity("{\"Body\":\"You are in right place\",\"request\":\"This_is_request\"}"));

        merchantIntegrationOffline = new MerchantIntegrationOffline(
                env,
                country,
                partnerId,
                partnerSecret,
                merchantId,
                terminalId);

        shipDetails = new JSONObject();
        shipDetails.put("shippingDetails", "Some country");
        items = new JSONObject();
        items.put("items", "clothes");

        metaInfo = new JSONObject();
        metaInfo.put("shippingDetails", "Some country");
        metaInfo.put("items", "clothes");
        metaInfo.put("metaInfo", new JSONObject[] { shipDetails, items });
    }

    @AfterEach
    public void tearDown() {
        httpClientsMock.close();
    }

    @Test
    public void testCreateQrCode() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerId = Utils.getRandomString(32);
        long amount = 2000;
        String currency = "VND";

        // test null msgID
        JSONObject jsonResponse = merchantIntegrationOffline.createQrCode(null, partnerId, amount, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = Utils.getRandomString(32);
        jsonResponse = merchantIntegrationOffline.createQrCode(msgID, partnerId, amount, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testPerformQrCode() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String partnerTxId = Utils.getRandomString(32);
        long amount = 2000;
        String currency = "VND";
        String code = "code";
        JSONObject jsonResponse = merchantIntegrationOffline.performQrCode(null, partnerTxId, amount, currency,
                code);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = Utils.getRandomString(32);
        jsonResponse = merchantIntegrationOffline.performQrCode(msgID, partnerTxId, amount, currency, code);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testCancel() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String partnerTxId = Utils.getRandomString(32);
        String origPartnerTxID = "origPartnerTxID";
        String origTxID = "origTxID";
        String currency = "VND";
        JSONObject jsonResponse = merchantIntegrationOffline.cancel(null, partnerTxId, origPartnerTxID, origTxID,
                currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));

        String msgID = Utils.getRandomString(32);
        jsonResponse = merchantIntegrationOffline.cancel(msgID, partnerTxId, origPartnerTxID, origTxID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testRefund() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String refundPartnerTxID = "refundPartnerTxID";
        String origPartnerTxID = "origPartnerTxID";
        long amount = 2000;
        String currency = "VND";
        String msgID = Utils.getRandomString(32);
        JSONObject jsonResponse = merchantIntegrationOffline.refund(msgID, refundPartnerTxID, amount, currency,
                origPartnerTxID, null);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testGetTxnDetails() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String partnerID = Utils.getRandomString(32);
        String currency = "VND";
        String msgID = Utils.getRandomString(32);
        JSONObject jsonResponse = merchantIntegrationOffline.getTxnDetails(msgID, partnerID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }

    @Test
    public void testGetRefundDetails() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String refundPartnerTxID = Utils.getRandomString(32);
        String currency = "VND";
        String msgID = Utils.getRandomString(32);
        JSONObject jsonResponse = merchantIntegrationOffline.getRefundDetails(msgID, refundPartnerTxID, currency);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
    }
}
