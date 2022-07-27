package unit;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.MerchantIntegrationOffline;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.models.Currency;
import com.merchantsdk.payment.models.PaymentMethod;
import com.merchantsdk.payment.models.PosCancelRequest;
import com.merchantsdk.payment.models.PosCancelResponse;
import com.merchantsdk.payment.models.PosInitiateRequest;
import com.merchantsdk.payment.models.PosInitiateResponse;
import com.merchantsdk.payment.models.PosInquiryRequest;
import com.merchantsdk.payment.models.PosInquiryResponse;
import com.merchantsdk.payment.models.PosPaymentChannel;
import com.merchantsdk.payment.models.PosRefundRequest;
import com.merchantsdk.payment.models.PosRefundResponse;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class TestMockMerchantOffline {
    private static MerchantIntegrationOffline merchantIntegrationOffline;

    private static EnvironmentType env = EnvironmentType.STAGING;
    private static Country country = Country.SINGAPORE;
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

    MockedStatic<Instant> instantMock;

    @BeforeEach
    public void setup() throws Exception {
        Instant nowValue = Instant.ofEpochSecond(1640000000);
        instantMock = mockStatic(Instant.class);
        instantMock.when(Instant::now).thenReturn(nowValue);

        httpClientsMock = mockStatic(HttpClients.class);
        when(HttpClients.createDefault()).thenReturn(clientMock);

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
        instantMock.close();
        httpClientsMock.close();
    }

    private static ClassicHttpRequest verifyAndCaptureExecutedRequest(CloseableHttpClient client) throws IOException {
        ArgumentCaptor<ClassicHttpRequest> httpRequestArgumentCaptor = ArgumentCaptor
                .forClass(ClassicHttpRequest.class);
        verify(client).execute(httpRequestArgumentCaptor.capture());
        return httpRequestArgumentCaptor.getValue();
    }

    @Test
    public void testInitiate() throws Exception {
        when(clientMock.execute(any(HttpPost.class))).thenReturn(response);

        String mockResponse = "".concat("{")
                .concat("\"transactionDetails\": {")
                .concat("\"paymentChannel\": \"MPQR\",")
                .concat("\"storeGrabID\": \"merchant-id\",")
                .concat("\"partnerTxID\": \"abc\",")
                .concat("\"partnerGroupTxID\": \"abc123\",")
                .concat("\"billRefNumber\": \"123\",")
                .concat("\"amount\": 1000,")
                .concat("\"currency\": \"SGD\",")
                .concat("\"paymentExpiryTime\": \"0\"")
                .concat("},")
                .concat("\"POSDetails\": {")
                .concat("\"terminalID\": \"terminal-id\"")
                .concat("},")
                .concat("\"statusDetails\": {")
                .concat("\"status\": \"status\",")
                .concat("\"statusCode\": \"200\",")
                .concat("\"statusReason\": \"\"")
                .concat("}")
                .concat("}");

        when(response.getCode()).thenReturn(200);
        when(response.getEntity())
                .thenReturn(new StringEntity(mockResponse));

        PosInitiateRequest request = new PosInitiateRequest() {
            {
                setTransactionDetails(new PosInitiateRequest.TransactionDetails() {
                    {
                        setAmount(1000L);
                        setCurrency(Currency.SGD);
                        setBillRefNumber("123");
                        setPartnerGroupTxID("abc123");
                        setPaymentChannel(PosPaymentChannel.MPQR);
                        setPartnerTxID("123");
                        setPaymentExpiryTime(0L);
                    }
                });
                setPaymentMethod(new PaymentMethod() {
                    {
                        setMinAmtPostpaid(100);
                    }
                });
            }
        };
        PosInitiateResponse response = merchantIntegrationOffline.initiate(request);

        assertNotNull(response);
        assertNotNull(response.getTransactionDetails());
        assertNotNull(response.getStatusDetails());
        assertNotNull(response.getPosDetails());
        assertEquals(PosPaymentChannel.MPQR, response.getTransactionDetails().getPaymentChannel());
        assertEquals("terminal-id", response.getPosDetails().getTerminalID());

        ClassicHttpRequest receivedRequest = verifyAndCaptureExecutedRequest(clientMock);

        String receivedRequestBody = EntityUtils.toString(receivedRequest.getEntity());

        assertEquals(
                "{\"transactionDetails\":{\"amount\":1000,\"currency\":\"SGD\",\"paymentExpiryTime\":0,\"billRefNumber\":\"123\",\"partnerTxID\":\"123\",\"partnerGroupTxID\":\"abc123\",\"paymentChannel\":\"MPQR\",\"storeGrabID\":\"merchant-id\"},\"paymentMethod\":{\"minAmtPostpaid\":100},\"POSDetails\":{\"terminalID\":\"terminal-id\"}}",
                receivedRequestBody);

        assertEquals(receivedRequest.getMethod(), "POST");
        assertTrue(receivedRequest.getPath().endsWith("/partner/v3/payment/init"));

        Map<String, String> expectedHeaders = new HashMap<String, String>() {
            {
                put("Authorization", "partner-id:PnPfyyvsU462bh1BRJ+lGX/IvUO9U7OtdOSxgdcEiZw=");
                put("Accept", "application/json");
                put("X-Sdk-Country", "SG");
                put("X-Sdk-Version", "2.0.0");
                put("X-Sdk-Signature", "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7");
                put("X-Sdk-Language", "java");
                put("Date", "Mon, 20 Dec 2021 11:33:20 GMT");
                put("Content-Type", "application/json");
            }
        };
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertTrue(receivedRequest.containsHeader(entry.getKey()));
            assertEquals(entry.getValue(), receivedRequest.getHeader(entry.getKey()).getValue());
        }
    }

    @Test
    public void testInquire() throws Exception {
        when(clientMock.execute(any(HttpGet.class))).thenReturn(response);

        String mockResponse = "".concat("{")
                .concat("\"transactionDetails\": {")
                .concat("\"paymentChannel\": \"MPQR\",")
                .concat("\"storeGrabID\": \"merchant-id\",")
                .concat("\"txType\": \"PAYMENT\",")
                .concat("\"partnerTxID\": \"abc\",")
                .concat("\"partnerGroupTxID\": \"abc123\",")
                .concat("\"billRefNumber\": \"123\",")
                .concat("\"amount\": 1000,")
                .concat("\"currency\": \"SGD\",")
                .concat("\"paymentMethod\": \"GPWALLET\",")
                .concat("\"paymentExpiryTime\": \"0\"")
                .concat("},")
                .concat("\"POSDetails\": {")
                .concat("\"terminalID\": \"terminal-id\"")
                .concat("},")
                .concat("\"statusDetails\": {")
                .concat("\"status\": \"status\",")
                .concat("\"statusCode\": \"200\",")
                .concat("\"statusReason\": \"\"")
                .concat("},")
                .concat("\"metadata\": {")
                .concat("\"offusTxID\": \"123\",")
                .concat("\"originOffusTxID\": \"123\"")
                .concat("}")
                .concat("}");

        when(response.getCode()).thenReturn(200);
        when(response.getEntity())
                .thenReturn(new StringEntity(mockResponse));

        PosInquiryRequest request = new PosInquiryRequest() {
            {
                setTransactionDetails(new PosInquiryRequest.TransactionDetails() {
                    {
                        setCurrency(Currency.SGD);
                        setPaymentChannel(PosPaymentChannel.MPQR);
                        setTxID("txID123");
                        setTxRefType(TxRefType.GRABTXID);
                        setTxType(TxType.PAYMENT);
                    }
                });
            }
        };
        PosInquiryResponse response = merchantIntegrationOffline.inquire(request);

        assertNotNull(response);
        assertNotNull(response.getTransactionDetails());
        assertNotNull(response.getStatusDetails());
        assertNotNull(response.getPosDetails());
        assertEquals(PosPaymentChannel.MPQR, response.getTransactionDetails().getPaymentChannel());
        assertEquals("terminal-id", response.getPosDetails().getTerminalID());

        ClassicHttpRequest receivedRequest = verifyAndCaptureExecutedRequest(clientMock);

        assertNull(receivedRequest.getEntity());

        // assertEquals(
        // "{\"transactionDetails\":{\"txRefType\":\"GRABTXID\",\"txID\":\"txID123\",\"currency\":\"SGD\",\"txType\":\"PAYMENT\",\"paymentChannel\":\"MPQR\"}}",
        // receivedRequestBody);
        assertEquals(receivedRequest.getMethod(), "GET");
        assertTrue(receivedRequest.getPath().contains("/partner/v3/payment/inquiry?"));
        assertTrue(receivedRequest.getPath().endsWith(
                "transactionDetails.txType=PAYMENT&transactionDetails.txRefType=GRABTXID&transactionDetails.txID=txID123&transactionDetails.paymentChannel=MPQR&transactionDetails.currency=SGD"));

        Map<String, String> expectedHeaders = new HashMap<String, String>() {
            {
                put("Authorization", "partner-id:I/IcmLYiHlcxzC/M8A3YdIhdmVoAQpItFk5A6fD8TE4=");
                put("Accept", "application/json");
                put("X-Sdk-Country", "SG");
                put("X-Sdk-Version", "2.0.0");
                put("X-Sdk-Signature", "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7");
                put("X-Sdk-Language", "java");
                put("Date", "Mon, 20 Dec 2021 11:33:20 GMT");
                put("Content-Type", "application/json");
            }
        };
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertTrue(receivedRequest.containsHeader(entry.getKey()));
            assertEquals(entry.getValue(), receivedRequest.getHeader(entry.getKey()).getValue());
        }
    }

    @Test
    public void testRefund() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String mockResponse = "".concat("{")
                .concat("\"transactionDetails\": {")
                .concat("\"paymentChannel\": \"MPQR\",")
                .concat("\"storeGrabID\": \"merchant-id\",")
                .concat("\"partnerTxID\": \"abc\",")
                .concat("\"partnerGroupTxID\": \"abc123\",")
                .concat("\"updatedTime\": 0,")
                .concat("\"amount\": 1000,")
                .concat("\"currency\": \"SGD\",")
                .concat("\"paymentMethod\": \"GPWALLET\"")
                .concat("},")
                .concat("\"originTxDetails\": {")
                .concat("\"originGrabTxID\": \"origin-grab-txID\",")
                .concat("\"originPartnerTxID\": \"origin-partner-txID\",")
                .concat("\"originPartnerGroupTxID\": \"origin-partner-group-txID\",")
                .concat("\"originAmount\": 5000")
                .concat("},")
                .concat("\"statusDetails\": {")
                .concat("\"status\": \"status\",")
                .concat("\"statusCode\": \"200\",")
                .concat("\"statusReason\": \"\"")
                .concat("},")
                .concat("\"metadata\": {")
                .concat("\"offusTxID\": \"123\",")
                .concat("\"originOffusTxID\": \"123\"")
                .concat("}")
                .concat("}");

        when(response.getCode()).thenReturn(200);
        when(response.getEntity())
                .thenReturn(new StringEntity(mockResponse));

        PosRefundRequest request = new PosRefundRequest() {
            {
                setTransactionDetails(new PosRefundRequest.TransactionDetails() {
                    {
                        setAmount(1000L);
                        setCurrency(Currency.SGD);
                        setBillRefNumber("123");
                        setPartnerGroupTxID("abc123");
                        setPaymentChannel(PosPaymentChannel.MPQR);
                        setPartnerTxID("123");
                    }
                });
            }
        };
        PosRefundResponse response = merchantIntegrationOffline.refund(request);

        assertNotNull(response);
        assertNotNull(response.getTransactionDetails());
        assertNotNull(response.getStatusDetails());
        assertNotNull(response.getOriginTxDetails());
        assertNotNull(response.getMetadata());
        assertEquals(PosPaymentChannel.MPQR, response.getTransactionDetails().getPaymentChannel());

        ClassicHttpRequest receivedRequest = verifyAndCaptureExecutedRequest(clientMock);

        String receivedRequestBody = EntityUtils.toString(receivedRequest.getEntity());

        assertEquals(
                "{\"transactionDetails\":{\"amount\":1000,\"currency\":\"SGD\",\"billRefNumber\":\"123\",\"partnerTxID\":\"123\",\"partnerGroupTxID\":\"abc123\",\"paymentChannel\":\"MPQR\"}}",
                receivedRequestBody);

        assertEquals(receivedRequest.getMethod(), "PUT");
        assertTrue(receivedRequest.getPath().endsWith("/partner/v3/payment/refund"));

        Map<String, String> expectedHeaders = new HashMap<String, String>() {
            {
                put("Authorization", "partner-id:p0KT3mpFDQtmsS2kUAnqPdM7oGIDzywJ6mvWhG5nMho=");
                put("Accept", "application/json");
                put("X-Sdk-Country", "SG");
                put("X-Sdk-Version", "2.0.0");
                put("X-Sdk-Signature", "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7");
                put("X-Sdk-Language", "java");
                put("Date", "Mon, 20 Dec 2021 11:33:20 GMT");
                put("Content-Type", "application/json");
            }
        };
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertTrue(receivedRequest.containsHeader(entry.getKey()));
            assertEquals(entry.getValue(), receivedRequest.getHeader(entry.getKey()).getValue());
        }
    }

    @Test
    public void testCancel() throws Exception {
        when(clientMock.execute(any(HttpPut.class))).thenReturn(response);

        String mockResponse = "".concat("{")
                .concat("\"transactionDetails\": {")
                .concat("\"paymentChannel\": \"MPQR\",")
                .concat("\"storeGrabID\": \"merchant-id\",")
                .concat("\"originPartnerTxID\": \"abc\",")
                .concat("\"currency\": \"SGD\"")
                .concat("},")
                .concat("\"statusDetails\": {")
                .concat("\"status\": \"status\",")
                .concat("\"statusCode\": \"200\",")
                .concat("\"statusReason\": \"\"")
                .concat("}")
                .concat("}");

        when(response.getCode()).thenReturn(200);
        when(response.getEntity())
                .thenReturn(new StringEntity(mockResponse));

        PosCancelRequest request = new PosCancelRequest() {
            {
                setTransactionDetails(new PosCancelRequest.TransactionDetails() {
                    {
                        setCurrency(Currency.SGD);
                        setPaymentChannel(PosPaymentChannel.MPQR);
                        setOriginPartnerTxID("abc");
                    }
                });
            }
        };
        PosCancelResponse response = merchantIntegrationOffline.cancel(request);

        assertNotNull(response);
        assertNotNull(response.getTransactionDetails());
        assertNotNull(response.getStatusDetails());
        assertEquals(PosPaymentChannel.MPQR, response.getTransactionDetails().getPaymentChannel());

        ClassicHttpRequest receivedRequest = verifyAndCaptureExecutedRequest(clientMock);

        String receivedRequestBody = EntityUtils.toString(receivedRequest.getEntity());

        assertEquals(
                "{\"transactionDetails\":{\"currency\":\"SGD\",\"originPartnerTxID\":\"abc\",\"paymentChannel\":\"MPQR\",\"storeGrabID\":\"merchant-id\"}}",
                receivedRequestBody);

        assertEquals(receivedRequest.getMethod(), "PUT");
        assertTrue(receivedRequest.getPath().endsWith("/partner/v3/payment/cancellation"));

        Map<String, String> expectedHeaders = new HashMap<String, String>() {
            {
                put("Authorization", "partner-id:kYY67JyX9aa5bvsynFFa+GDu3FtPvstL3Vt4rJg+EA4=");
                put("Accept", "application/json");
                put("X-Sdk-Country", "SG");
                put("X-Sdk-Version", "2.0.0");
                put("X-Sdk-Signature", "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7");
                put("X-Sdk-Language", "java");
                put("Date", "Mon, 20 Dec 2021 11:33:20 GMT");
                put("Content-Type", "application/json");
            }
        };
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertTrue(receivedRequest.containsHeader(entry.getKey()));
            assertEquals(entry.getValue(), receivedRequest.getHeader(entry.getKey()).getValue());
        }
    }
}