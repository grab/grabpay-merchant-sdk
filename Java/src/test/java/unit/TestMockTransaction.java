package unit;

import com.merchantsdk.payment.exception.SDKRuntimeException;
import connection.CustomHttpGet;
import connection.CustomHttpPost;
import connection.CustomHttpPut;
import connection.CustomHttpDelete;

import com.merchantsdk.payment.config.ApiUrl;
import com.merchantsdk.payment.config.Config;
import com.merchantsdk.payment.config.PathUtility;
import com.merchantsdk.payment.service.AuthorizationService;
import com.merchantsdk.payment.service.Transaction;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;

import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMockTransaction {
        private static Config config;
        private static Transaction transaction;

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
                transaction = new Transaction();
        }

        @Mock
        CloseableHttpClient clientMock;

        @Test
        public void testGet() throws Exception {
                MockitoAnnotations.openMocks(this);
                String path = "https://grab.com";
                ConcurrentHashMap<String, String> header = new ConcurrentHashMap<>();
                header.put("Authorization", "Bearer This_is_OAuth_code");
                header.put("Content-Type", "application/json");
                header.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");
                CustomHttpGet httpGet = new CustomHttpGet(path);
                header.forEach(httpGet::addHeader);

                CloseableHttpResponse response = mock(CloseableHttpResponse.class);
                HttpEntity entity = mock(HttpEntity.class);

                when(entity.toString()).thenReturn("You are in right place");
                when(response.getEntity()).thenReturn(entity);
                when(response.getCode()).thenReturn(200);
                when(clientMock.execute(eq(httpGet))).thenReturn(response);

                CloseableHttpResponse actualResponse = transaction.get(path, null, header, clientMock);

                verify(clientMock).execute(eq(httpGet));
                assertNotNull(actualResponse);
                assertEquals(200, actualResponse.getCode());
                assertEquals("You are in right place", actualResponse.getEntity().toString());

                actualResponse = transaction.get(null, null, header, clientMock);
                assertNull(actualResponse);

                actualResponse = transaction.get(path, null, null, clientMock);
                assertNull(actualResponse);
        }

        @Test
        public void testPost() throws Exception {
                MockitoAnnotations.openMocks(this);

                String path = "https://grab.com";
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer This_is_OAuth_code");
                header.put("Content-Type", "application/json");
                header.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");

                String body = "{\"code\":\"799d5ec3bf964940abe9c1d47dda9e56\",\"grant_type\":\"authorization_code\",\"client_secret\":\"BDGSPQYYUqLXNkmy\",\"redirect_uri\":\"http://localhost:8888/result\",\"client_id\":\"e9b5560b0be844a2ad55c6afa8b23fbb\",\"code_verifier\":\"eZS7brPF35UXmtzk8ElSGde0fw9WhYieeZS7brPF35UXmtzk8ElSGde0fw9WhYie\"}";

                CustomHttpPost httpPost = new CustomHttpPost(path);
                header.forEach(httpPost::addHeader);
                httpPost.setEntity(new StringEntity(body));

                CloseableHttpClient clientMock = mock(CloseableHttpClient.class);
                CloseableHttpResponse response = mock(CloseableHttpResponse.class);
                HttpEntity entity = mock(HttpEntity.class);

                when(entity.toString()).thenReturn("You are in right place");
                when(response.getEntity()).thenReturn(entity);
                when(response.getCode()).thenReturn(200);
                when(clientMock.execute(eq(httpPost))).thenReturn(response);

                CloseableHttpResponse actualResponse = transaction.post(path, body, header, clientMock);

                verify(clientMock).execute(eq(httpPost));
                assertNotNull(actualResponse);
                assertEquals(200, actualResponse.getCode());
                assertEquals("You are in right place", actualResponse.getEntity().toString());

                actualResponse = transaction.post(null, null, header, clientMock);
                assertNull(actualResponse);

                actualResponse = transaction.post(path, null, null, clientMock);
                assertNull(actualResponse);
        }

        @Test
        public void testPut() throws Exception {
                MockitoAnnotations.openMocks(this);
                String path = "https://grab.com";
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer This_is_OAuth_code");
                header.put("Content-Type", "application/json");
                header.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");
                String body = "{\"code\":\"799d5ec3bf964940abe9c1d47dda9e56\",\"grant_type\":\"authorization_code\",\"client_secret\":\"BDGSPQYYUqLXNkmy\",\"redirect_uri\":\"http://localhost:8888/result\",\"client_id\":\"e9b5560b0be844a2ad55c6afa8b23fbb\",\"code_verifier\":\"eZS7brPF35UXmtzk8ElSGde0fw9WhYieeZS7brPF35UXmtzk8ElSGde0fw9WhYie\"}";

                CustomHttpPut httpPut = new CustomHttpPut(path);
                header.forEach(httpPut::addHeader);
                httpPut.setEntity(new StringEntity(body));

                CloseableHttpClient clientMock = mock(CloseableHttpClient.class);
                CloseableHttpResponse response = mock(CloseableHttpResponse.class);
                HttpEntity entity = mock(HttpEntity.class);

                when(entity.toString()).thenReturn("You are in right place");
                when(response.getEntity()).thenReturn(entity);
                when(response.getCode()).thenReturn(200);
                when(clientMock.execute(eq(httpPut))).thenReturn(response);

                CloseableHttpResponse actualResponse = transaction.put(path, body, header, clientMock);

                verify(clientMock).execute(eq(httpPut));
                assertNotNull(actualResponse);
                assertEquals(200, actualResponse.getCode());
                assertEquals("You are in right place", actualResponse.getEntity().toString());

                actualResponse = transaction.put(null, null, header, clientMock);
                assertNull(actualResponse);

                actualResponse = transaction.put(path, null, null, clientMock);
                assertNull(actualResponse);
        }

        @Test
        public void testDelete() throws Exception {
                MockitoAnnotations.openMocks(this);
                String path = "https://grab.com";
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer This_is_OAuth_code");
                header.put("Content-Type", "application/json");
                header.put("Date", "Wed, 24 Nov 2021 06:22:08 GMT");

                CustomHttpDelete httpDelete = new CustomHttpDelete(path);
                header.forEach(httpDelete::addHeader);

                CloseableHttpClient clientMock = mock(CloseableHttpClient.class);
                CloseableHttpResponse response = mock(CloseableHttpResponse.class);
                HttpEntity entity = mock(HttpEntity.class);

                when(entity.toString()).thenReturn("You are in right place");
                when(response.getEntity()).thenReturn(entity);
                when(response.getCode()).thenReturn(200);
                when(clientMock.execute(eq(httpDelete))).thenReturn(response);

                CloseableHttpResponse actualResponse = transaction.delete(path, null, header, clientMock);

                verify(clientMock).execute(httpDelete);
                assertNotNull(actualResponse);
                assertEquals(200, actualResponse.getCode());
                assertEquals("You are in right place", actualResponse.getEntity().toString());

                actualResponse = transaction.delete(null, null, header, clientMock);
                assertNull(actualResponse);

                actualResponse = transaction.delete(path, null, null, clientMock);
                assertNull(actualResponse);
        }

        @InjectMocks
        @Spy
        Transaction mockedTrans;

        @Test
        public void testSendRequest() throws Exception {
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

                ConcurrentHashMap<String, String> header = new ConcurrentHashMap<>();
                header.put("Content-type", "application/json");
                String body = "This is body";
                String url = "https://grab.com";
                JSONObject bodyReq = new JSONObject();
                bodyReq.put("body", "test");

                String httpMethod = "GET";
                String content = "application/json";
                String accessToken = "access_token";
                String type = "ONLINE";
                String msgID = "msgID";

                HashMap<String, Object> params = new HashMap<>();
                params.put("body", bodyReq);

                doReturn(params).when(mockedTrans).prepareRequest(
                                any(AuthorizationService.class),
                                any(Config.class),
                                any(String.class),
                                any(String.class),
                                any(String.class),
                                any(JSONObject.class),
                                any(String.class),
                                any(String.class),
                                any(String.class));

                doReturn(header).when(mockedTrans).getHeaderFromRequest(isA(HashMap.class));
                doReturn(body).when(mockedTrans).getBodyFromRequest(isA(HashMap.class));
                doReturn(url).when(mockedTrans).getUrlFromRequest(isA(HashMap.class));

                doReturn(responseGet).when(mockedTrans).get(isA(String.class), isA(String.class),
                                isA(ConcurrentHashMap.class),
                                isA(CloseableHttpClient.class));
                doReturn(responsePost).when(mockedTrans).post(isA(String.class), isA(String.class),
                                isA(ConcurrentHashMap.class), isA(CloseableHttpClient.class));
                doReturn(responsePut).when(mockedTrans).put(isA(String.class), isA(String.class),
                                isA(ConcurrentHashMap.class),
                                isA(CloseableHttpClient.class));
                doReturn(responseDelete).when(mockedTrans).delete(isA(String.class), isA(String.class),
                                isA(ConcurrentHashMap.class), isA(CloseableHttpClient.class));

                assertNotNull(config);
                assertNotNull(httpMethod);
                assertNotNull(url);
                assertNotNull(content);
                assertNotNull(bodyReq);
                assertNotNull(accessToken);
                assertNotNull(type);
                assertNotNull(msgID);

                // get method
                CloseableHttpResponse response = mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq,
                                accessToken,
                                type, msgID);
                assert response.getCode() == 200;
                assert response.getEntity().toString().equals("Get Body");

                // post method
                httpMethod = "POST";
                response = mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq, accessToken, type, msgID);
                assert response.getCode() == 201;
                assert response.getEntity().toString().equals("Post Body");

                // put method
                httpMethod = "PUT";
                response = mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq, accessToken, type, msgID);
                assert response.getCode() == 202;
                assert response.getEntity().toString().equals("Put Body");

                // delete method
                httpMethod = "DELETE";
                response = mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq, accessToken, type, msgID);
                assert response.getCode() == 203;
                assert response.getEntity().toString().equals("Delete Body");

                // test lower case method
                // post method
                httpMethod = "post";
                response = mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq, accessToken, type, msgID);
                assert response.getCode() == 201;
                assert response.getEntity().toString().equals("Post Body");

                // test exception - wrong method
                httpMethod = "gets";
                Exception e = null;
                try {
                        mockedTrans.sendRequest(config, httpMethod, url, content, bodyReq, accessToken, type, msgID);
                } catch (Exception ex) {
                        e = ex;
                }
                assertTrue(e instanceof SDKRuntimeException);
        }

        @Test
        public void testGenerateHeader() {
                AuthorizationService mockedAuthorizationService = mock(AuthorizationService.class);
                String timeStamp = "Mon, 29 Nov 2021 12:00:00 GMT";
                String pops = "POP_signature_is_true";
                doReturn(timeStamp).when(mockedAuthorizationService).getFormattedDateTime(any(Instant.class));
                doReturn(pops).when(mockedAuthorizationService).generatePOPSig(
                                isA(Config.class),
                                isA(String.class),
                                isA(Instant.class));
                Map<PathUtility, String> api = new ApiUrl().getURL(config.getCountry());
                String httpMethod = "POST";
                String contentType = "application/json";
                String type = "ONLINE";
                String access_token = "access_token";
                String hmac = "HMAC_SIGNATURE";
                Instant time = Instant.now();
                String msgID = "msgID";
                // test oauth
                String oauthPath = api.get(PathUtility.PATH_OAUTH_TOKEN);
                Map<String, String> header = mockedTrans.generateHeaders(mockedAuthorizationService, config, httpMethod,
                                contentType, oauthPath, type, access_token, hmac, time, msgID);

                assertTrue(header instanceof ConcurrentHashMap);
                assertEquals(7, header.size());
                assertNull(header.get("Authorization"));

                // test chargeInit
                String chargeInitPath = api.get(PathUtility.PATH_ONA_CHARGE_INIT);
                header = mockedTrans.generateHeaders(mockedAuthorizationService, config, httpMethod, contentType,
                                chargeInitPath, type, access_token, hmac, time, msgID);
                assertTrue(header instanceof ConcurrentHashMap);
                assertEquals(9, header.size());
                assertTrue(header.get("Authorization").contains(hmac));

                // test offline
                String createQRPath = api.get(PathUtility.PATH_POS_CREATE_QR_CODE);
                type = "OFFLINE";
                header = mockedTrans.generateHeaders(mockedAuthorizationService, config, httpMethod, contentType,
                                createQRPath,
                                type, access_token, hmac, time, msgID);
                assertTrue(header instanceof ConcurrentHashMap);
                assertEquals(9, header.size());
                assertTrue(header.get("Authorization").contains(hmac));

                // test other api
                String defaultPath = api.get(PathUtility.PATH_ONA_REFUND_STATUS);
                type = "ONLINE";
                header = mockedTrans.generateHeaders(mockedAuthorizationService, config, httpMethod, contentType,
                                defaultPath,
                                type, access_token, hmac, time, msgID);
                assertTrue(header instanceof ConcurrentHashMap);
                assertEquals(10, header.size());
                assertTrue(header.get("X-GID-AUX-POP").contains(pops));
                assertTrue(header.get("Authorization").contains(access_token));

                // test access_token null
                access_token = null;
                defaultPath = api.get(PathUtility.PATH_ONA_CHARGE_COMPLETE);
                header = mockedTrans.generateHeaders(mockedAuthorizationService, config, httpMethod, contentType,
                                defaultPath,
                                type, access_token, hmac, time, msgID);
                assertEquals(header.get("Authorization"), "Bearer ");
        }

        @Test
        public void testPrepareRequest() {
                AuthorizationService mockedAuthorizationService = mock(AuthorizationService.class);
                String timeStamp = "Mon, 29 Nov 2021 12:00:00 GMT";
                String httpMethod = "POST";
                String contentType = "application/json";
                String type = "ONLINE";
                String access_token = "access_token";
                String hmac = "HMAC_SIGNATURE";
                String msgID = "msgID";
                String path = "/some/example/paths/here";

                JSONObject requestBody = new JSONObject();
                requestBody.put("body", "this_is_body");

                JSONObject mergedBody = new JSONObject();
                mergedBody.put("body", "this_is_body");
                mergedBody.put("msgID", msgID);
                mergedBody.put("grabID", config.getMerchant_id());
                mergedBody.put("terminalID", config.getTerminal_id());

                doReturn(hmac).when(mockedAuthorizationService).generateHmac(
                                isA(Config.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(Instant.class));
                doReturn(msgID).when(mockedAuthorizationService).generateMsgID(any(String.class));

                String buildedUrl = "https://grab.com?abc=xyz";
                doReturn(buildedUrl).when(mockedTrans).buildHttpGetUrl(isA(JSONObject.class), isA(String.class));

                Map<String, String> header = new ConcurrentHashMap<>();
                header.put("Date", timeStamp);
                doReturn(header).when(mockedTrans).generateHeaders(
                                isA(AuthorizationService.class),
                                isA(Config.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(String.class),
                                isA(Instant.class),
                                isA(String.class));

                // test post method online
                HashMap<String, Object> request = mockedTrans.prepareRequest(mockedAuthorizationService, config,
                                httpMethod,
                                path, contentType, requestBody, access_token, type, msgID);
                assertEquals(3, request.size());
                assertEquals(requestBody.toString(), (request.get("body")));
                assertEquals(header, request.get("header"));
                assertTrue(request.get("apiUrl").toString().contains(path));

                // test post offline
                type = "offline";
                request = mockedTrans.prepareRequest(mockedAuthorizationService, config, httpMethod, path, contentType,
                                requestBody, access_token, type, msgID);
                assertEquals(3, request.size());
                assertEquals(mergedBody.toString(), request.get("body"));
                assertEquals(header, request.get("header"));
                assertTrue(request.get("apiUrl").toString().contains(path));

                // test get offline
                type = "offline";
                httpMethod = "get";
                request = mockedTrans.prepareRequest(mockedAuthorizationService, config, httpMethod, path, contentType,
                                requestBody, access_token, type, msgID);
                assertEquals(3, request.size());
                assertTrue(request.get("apiUrl").toString().contains(buildedUrl));
                assertEquals(header, request.get("header"));

        }

}
