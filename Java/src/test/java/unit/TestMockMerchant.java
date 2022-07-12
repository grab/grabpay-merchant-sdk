package unit;

import com.merchantsdk.payment.MerchantIntegration;
import com.merchantsdk.payment.MerchantIntegrationOnline;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMockMerchant {
    private static MerchantIntegration merchantIntegration;

    private static String staging = "STG";
    private static String country = "VN";
    private static String partner_id = "partner-id";
    private static String partner_secret = "partner-secret";
    private static String merchant_id = "merchant-id";
    private static String client_id = "client-id";
    private static String client_secret = "client-secret";
    private static String redirect_uri = "redirect-url";

    private CloseableHttpResponse response = mock(CloseableHttpResponse.class);

    @BeforeAll
    public static void setUp() {
        merchantIntegration = new MerchantIntegrationOnline(
                staging,
                country,
                partner_id,
                partner_secret,
                merchant_id,
                client_id,
                client_secret,
                redirect_uri);
    }

    @Mock
    CloseableHttpClient clientMock;

    @BeforeEach
    public void createMock() {
        merchantIntegration.getOnlineTransaction().getTransaction().setClient(clientMock);

        when(response.getCode()).thenReturn(200, 300, 401);
        when(response.getEntity())
                .thenReturn(new StringEntity("{\"Body\":\"You are in right place\",\"request\":\"This_is_request\"}"));
    }

    @Test
    public void testProcessResponse() {

        JSONObject jsonResponse = merchantIntegration.processResponse(response);

        assertNotNull(jsonResponse);
        assertEquals("You are in right place", jsonResponse.get("Body"));
        assertEquals(2, jsonResponse.length());

        jsonResponse = merchantIntegration.processResponse(response);
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.has("request"));

        jsonResponse = merchantIntegration.processResponse(response);
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.has("Error"));
    }
}
