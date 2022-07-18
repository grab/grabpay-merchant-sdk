package unit;

import com.merchantsdk.payment.exception.SDKRuntimeException;

import com.merchantsdk.payment.Country;
import com.merchantsdk.payment.config.EnvironmentType;
import com.merchantsdk.payment.config.PosConfig;
import com.merchantsdk.payment.config.SdkEnvironment;
import com.merchantsdk.payment.service.Requester;
import com.merchantsdk.payment.service.Requester.RequestParams;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TestMockTransaction {
    private PosConfig config;
    private Requester requester;

    @Mock
    CloseableHttpClient clientMock;

    MockedStatic<HttpClients> httpClientsMock;

    @BeforeEach
    public void setup() {
        httpClientsMock = mockStatic(HttpClients.class);
        when(HttpClients.createDefault()).thenReturn(clientMock);

        config = new PosConfig(
                EnvironmentType.STAGING,
                Country.VIETNAM,
                "partner-id",
                "partner-secret",
                "merchant-id",
                "terminal-id");
        requester = new Requester(config);
    }

    @AfterEach
    public void tearDown() {
        httpClientsMock.close();
    }

    private static ClassicHttpRequest verifyAndCaptureExecutedRequest(CloseableHttpClient client) throws IOException {
        ArgumentCaptor<ClassicHttpRequest> httpRequestArgumentCaptor = ArgumentCaptor
                .forClass(ClassicHttpRequest.class);
        verify(client).execute(httpRequestArgumentCaptor.capture());
        return httpRequestArgumentCaptor.getValue();
    }

    @Test
    public void testGet() throws Exception {
        String path = "https://grab.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer This_is_OAuth_code");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");
        HttpGet httpGet = new HttpGet(path);
        headers.forEach(httpGet::addHeader);

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(entity.toString()).thenReturn("You are in right place");
        when(response.getEntity()).thenReturn(entity);
        when(response.getCode()).thenReturn(200);
        when(clientMock.execute(isA(HttpGet.class))).thenReturn(response);

        CloseableHttpResponse actualResponse = requester.get(new RequestParams(path, headers, null));

        ClassicHttpRequest request = verifyAndCaptureExecutedRequest(clientMock);
        assertEquals(request.getHeaders().length, headers.size());
        for (Map.Entry<String, String> header : headers.entrySet()) {
            assertTrue(request.containsHeader(header.getKey()));
            assertEquals(request.getHeader(header.getKey()).getValue(), header.getValue());
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("You are in right place", actualResponse.getEntity().toString());

        assertThrows(NullPointerException.class, () -> {
            requester.get(new RequestParams(null, headers, null));
        });
    }

    @Test
    public void testPost() throws Exception {
        String path = "https://grab.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer This_is_OAuth_code");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");

        String body = "{\"code\":\"799d5ec3bf964940abe9c1d47dda9e56\",\"grant_type\":\"authorization_code\",\"client_secret\":\"BDGSPQYYUqLXNkmy\",\"redirect_uri\":\"http://localhost:8888/result\",\"client_id\":\"e9b5560b0be844a2ad55c6afa8b23fbb\",\"code_verifier\":\"eZS7brPF35UXmtzk8ElSGde0fw9WhYieeZS7brPF35UXmtzk8ElSGde0fw9WhYie\"}";

        HttpPost httpPost = new HttpPost(path);
        headers.forEach(httpPost::addHeader);
        httpPost.setEntity(new StringEntity(body));

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(entity.toString()).thenReturn("You are in right place");
        when(response.getEntity()).thenReturn(entity);
        when(response.getCode()).thenReturn(200);
        when(clientMock.execute(isA(HttpPost.class))).thenReturn(response);

        CloseableHttpResponse actualResponse = requester.post(new RequestParams(path, headers, body));

        ClassicHttpRequest request = verifyAndCaptureExecutedRequest(clientMock);
        assertEquals(request.getHeaders().length, headers.size());
        for (Map.Entry<String, String> header : headers.entrySet()) {
            assertTrue(request.containsHeader(header.getKey()));
            assertEquals(request.getHeader(header.getKey()).getValue(), header.getValue());
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("You are in right place", actualResponse.getEntity().toString());

        assertThrows(NullPointerException.class, () -> {
            requester.post(new RequestParams(null, headers, null));
        });
    }

    @Test
    public void testPut() throws Exception {
        String path = "https://grab.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer This_is_OAuth_code");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "Wed, 24 Nov 2000 06:22:00 GMT");
        String body = "{\"code\":\"799d5ec3bf964940abe9c1d47dda9e56\",\"grant_type\":\"authorization_code\",\"client_secret\":\"BDGSPQYYUqLXNkmy\",\"redirect_uri\":\"http://localhost:8888/result\",\"client_id\":\"e9b5560b0be844a2ad55c6afa8b23fbb\",\"code_verifier\":\"eZS7brPF35UXmtzk8ElSGde0fw9WhYieeZS7brPF35UXmtzk8ElSGde0fw9WhYie\"}";

        HttpPut httpPut = new HttpPut(path);
        headers.forEach(httpPut::addHeader);
        httpPut.setEntity(new StringEntity(body));

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(entity.toString()).thenReturn("You are in right place");
        when(response.getEntity()).thenReturn(entity);
        when(response.getCode()).thenReturn(200);
        when(clientMock.execute(isA(HttpPut.class))).thenReturn(response);

        CloseableHttpResponse actualResponse = requester.put(new RequestParams(path, headers, body));

        ClassicHttpRequest request = verifyAndCaptureExecutedRequest(clientMock);
        assertEquals(request.getHeaders().length, headers.size());
        for (Map.Entry<String, String> header : headers.entrySet()) {
            assertTrue(request.containsHeader(header.getKey()));
            assertEquals(request.getHeader(header.getKey()).getValue(), header.getValue());
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("You are in right place", actualResponse.getEntity().toString());

        assertThrows(NullPointerException.class, () -> {
            requester.put(new RequestParams(null, headers, null));
        });
    }

    @Test
    public void testDelete() throws Exception {
        String path = "https://grab.com";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer This_is_OAuth_code");
        headers.put("Content-Type", "application/json");
        headers.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");

        HttpDelete httpDelete = new HttpDelete(path);
        headers.forEach(httpDelete::addHeader);

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(entity.toString()).thenReturn("You are in right place");
        when(response.getEntity()).thenReturn(entity);
        when(response.getCode()).thenReturn(200);
        when(clientMock.execute(isA(HttpDelete.class))).thenReturn(response);

        CloseableHttpResponse actualResponse = requester.delete(new RequestParams(path, headers, null));

        ClassicHttpRequest request = verifyAndCaptureExecutedRequest(clientMock);
        assertEquals(request.getHeaders().length, headers.size());
        for (Map.Entry<String, String> header : headers.entrySet()) {
            assertTrue(request.containsHeader(header.getKey()));
            assertEquals(request.getHeader(header.getKey()).getValue(), header.getValue());
        }

        assertNotNull(actualResponse);
        assertEquals(200, actualResponse.getCode());
        assertEquals("You are in right place", actualResponse.getEntity().toString());

        assertThrows(NullPointerException.class, () -> {
            requester.delete(new RequestParams(null, headers, null));
        });
    }

    @Test
    public void testSendRequest() throws Exception {
        Requester mockedRequester = spy(new Requester(config));
        CloseableHttpResponse responseGet = mock(CloseableHttpResponse.class);
        CloseableHttpResponse responsePost = mock(CloseableHttpResponse.class);
        CloseableHttpResponse responseDelete = mock(CloseableHttpResponse.class);
        CloseableHttpResponse responsePut = mock(CloseableHttpResponse.class);
        HttpEntity entityGet = mock(HttpEntity.class);
        HttpEntity entityPost = mock(HttpEntity.class);
        HttpEntity entityPut = mock(HttpEntity.class);
        HttpEntity entityDelete = mock(HttpEntity.class);

        when(entityGet.toString()).thenReturn("Get Body");
        when(responseGet.getCode()).thenReturn(200);
        when(responseGet.getEntity()).thenReturn(entityGet);

        when(entityPost.toString()).thenReturn("Post Body");
        when(responsePost.getCode()).thenReturn(201);
        when(responsePost.getEntity()).thenReturn(entityPost);

        when(entityPut.toString()).thenReturn("Put Body");
        when(responsePut.getCode()).thenReturn(202);
        when(responsePut.getEntity()).thenReturn(entityPut);

        when(entityDelete.toString()).thenReturn("Delete Body");
        when(responseDelete.getCode()).thenReturn(203);
        when(responseDelete.getEntity()).thenReturn(entityDelete);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json");
        String body = "This is body";
        String url = "https://grab.com";
        JSONObject bodyReq = new JSONObject();
        bodyReq.put("body", body);

        String httpMethod = "GET";
        String content = "application/json";
        String accessToken = "access_token";
        String type = "ONLINE";
        String msgID = "msgID";

        doReturn(responseGet).when(mockedRequester).get(isA(RequestParams.class));
        doReturn(responsePost).when(mockedRequester).post(isA(RequestParams.class));
        doReturn(responsePut).when(mockedRequester).put(isA(RequestParams.class));
        doReturn(responseDelete).when(mockedRequester).delete(isA(RequestParams.class));

        assertNotNull(config);
        assertNotNull(httpMethod);
        assertNotNull(url);
        assertNotNull(content);
        assertNotNull(bodyReq);
        assertNotNull(accessToken);
        assertNotNull(type);
        assertNotNull(msgID);

        // get method
        CloseableHttpResponse response = mockedRequester.sendRequest(httpMethod, url, null, null, bodyReq);
        assert response.getCode() == 200;
        assert response.getEntity().toString().equals("Get Body");

        // post method
        httpMethod = "POST";
        response = mockedRequester.sendRequest(httpMethod, url, null, null, bodyReq);
        assert response.getCode() == 201;
        assert response.getEntity().toString().equals("Post Body");

        // put method
        httpMethod = "PUT";
        response = mockedRequester.sendRequest(httpMethod, url, null, null, bodyReq);
        assert response.getCode() == 202;
        assert response.getEntity().toString().equals("Put Body");

        // delete method
        httpMethod = "DELETE";
        response = mockedRequester.sendRequest(httpMethod, url, null, null, bodyReq);
        assert response.getCode() == 203;
        assert response.getEntity().toString().equals("Delete Body");

        // test lower case method
        // post method
        httpMethod = "post";
        response = mockedRequester.sendRequest(httpMethod, url, null, null, bodyReq);
        assert response.getCode() == 201;
        assert response.getEntity().toString().equals("Post Body");

        // test exception - wrong method
        assertThrows(SDKRuntimeException.class, () -> {
            mockedRequester.sendRequest("gets", url, null, null, bodyReq);
        });
    }

    @Test
    public void testPrepareRequest() {
        Requester mockedRequester = spy(new Requester(config));
        String httpMethod = "POST";
        String path = "/some/example/paths/here";

        JSONObject requestBody = new JSONObject();
        requestBody.put("body", "this_is_body");
        String requestBodyStr = requestBody.toString();

        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-Custom-Header", "value");

        // test post method online
        RequestParams request;

        request = mockedRequester.prepareRequest(httpMethod, path, null, null, requestBodyStr);
        assertEquals(requestBodyStr, request.getBody());
        assertTrue(request.getHeaders().containsKey("Date"));
        assertTrue(request.getHeaders().containsKey("Accept"));
        assertTrue(request.getHeaders().containsKey("Content-Type"));
        assertTrue(request.getHeaders().containsKey("X-Sdk-Country"));
        assertEquals(config.getCountry().toString(), request.getHeaders().get("X-Sdk-Country"));
        assertTrue(request.getHeaders().containsKey("X-Sdk-Language"));
        assertEquals("java", request.getHeaders().get("X-Sdk-Language"));
        assertTrue(request.getHeaders().containsKey("X-Sdk-Version"));
        assertEquals(SdkEnvironment.SDK_VERSION, request.getHeaders().get("X-Sdk-Version"));
        assertTrue(request.getHeaders().containsKey("X-Sdk-Signature"));
        assertEquals(SdkEnvironment.SDK_SIGNATURE, request.getHeaders().get("X-Sdk-Signature"));
        assertTrue(request.getUrl().contains(path));

        request = mockedRequester.prepareRequest(httpMethod, path, null, customHeaders, requestBodyStr);
        assertTrue(request.getHeaders().containsKey("X-Custom-Header"));
        assertEquals("value", request.getHeaders().get("X-Custom-Header"));
    }

}
