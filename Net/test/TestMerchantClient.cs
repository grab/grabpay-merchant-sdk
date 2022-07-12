using System;
using Xunit;
using Net.Public;
using System.Linq;
using Newtonsoft.Json;
using Moq;
using System.Net.Http;
using System.Text;

namespace NetTest
{
    public class TestMerchantClient
    {
        private Random random = new Random();
        MerchantIntegrationOnline MerchantClientOnline;
        MerchantIntegrationOffline MerchantClientOffline;
        private string partnerId = "partnerId";
        private string partnerSecret = "partnerSecret";
        private string merchantId = "merchantId";
        private string terminalId = "terminalId";
        private string clientId = "clientId";
        private string clientSecret = "clientSecret";
        private string env = "STG";
        private string country = "VN";
        private string redirectUrl = "http://localhost:8888/result";

        public TestMerchantClient()
        {
            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl);
        }

        [Fact]
        public void TestChargeInit()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            var partnerTxId = RandomString(32);

            var responseBody = new
            {
                partnerTxID = partnerTxId
            };
            responseMessage.Content = CreateStringContent(JsonConvert.SerializeObject(responseBody));
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);

            var response = this.MerchantClientOnline.OnaChargeInit(partnerTxId, RandomString(32), 2000, "VND");

            if (response.StatusCode == System.Net.HttpStatusCode.OK)
            {
                var bodyResp = JsonConvert.DeserializeObject<dynamic>(response.Content.ReadAsStringAsync().Result);
                Assert.Equal(partnerTxId, bodyResp.partnerTxID.ToString());
            }
        }

        [Fact]
        public void TestBNPL()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            var partnerTxId = RandomString(32);

            var responseBody = new
            {
                partnerTxID = partnerTxId
            };
            responseMessage.Content = CreateStringContent(JsonConvert.SerializeObject(responseBody));
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            
            string[] arr = { "INSTALMENT" };
            var response = this.MerchantClientOnline.OnaChargeInit(partnerTxId, RandomString(32), 2000, "VND",hidePaymentMethods: arr);

            if (response.StatusCode == System.Net.HttpStatusCode.OK)
            {
                var bodyResp = JsonConvert.DeserializeObject<dynamic>(response.Content.ReadAsStringAsync().Result);
                Assert.Equal(partnerTxId, bodyResp.partnerTxID.ToString());
            }
        }

        [Fact]
        public void TestOnaCreateWebUrl()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            var responseBody = new {
                request = "request"
            };
            responseMessage.Content = CreateStringContent(JsonConvert.SerializeObject(responseBody));
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var partnerTxId = RandomString(32);
            var response = this.MerchantClientOnline.OnaCreateWebUrl(partnerTxId, RandomString(32), 2000, "VND", "codeVerifier", null, null, null, null,null, RandomString(7));

            if (response.StatusCode == System.Net.HttpStatusCode.OK)
            {
                var bodyResp = response.Content.ReadAsStringAsync().Result;
                Assert.IsType<String>(bodyResp);
            }
        }

        [Fact]
        public void TestOnaOAuth2Token()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaOAuth2Token(RandomString(32), "11d1c38206b84f43a3bc3f3509943584");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestOnaChargeComplete()
        {

            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaChargeComplete(RandomString(32), "accessToken");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestOnaGetChargeStatus()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaGetChargeStatus(RandomString(32), "VND", "accessToken");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestOnaGetRefundStatus()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaGetRefundStatus(RandomString(32), "VND", "accessToken");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestOnaRefund()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaRefund(RandomString(32), RandomString(32), 2000, "VND", RandomString(32),"description", "accessToken");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestOnaGetOTCStatus()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOnline = new MerchantIntegrationOnline(env, country, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl, httpClientMock.Object);
            var response = this.MerchantClientOnline.OnaGetOTCStatus(RandomString(32), "VND");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosCreateQRCode()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            
            var response = this.MerchantClientOffline.PosCreateQRCode(RandomString(32), RandomString(32), 2000, "VND");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosPerformQRCode()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            var response = this.MerchantClientOffline.PosPerformQRCode(RandomString(32), RandomString(32), 2000, "VND","code");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosCancel()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            var response = this.MerchantClientOffline.PosCancel(RandomString(32), RandomString(32), RandomString(32), RandomString(32), "VND");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosRefund()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            var response = this.MerchantClientOffline.PosRefund(RandomString(32), 2000,"VND", RandomString(32), "reason");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosGetTxnDetails()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            var response = this.MerchantClientOffline.PosGetTxnStatus(RandomString(32), RandomString(32), "VND");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        [Fact]
        public void TestPosGetRefundDetails()
        {
            var httpClientMock = new Mock<IHttpClient>();
            HttpResponseMessage responseMessage = new HttpResponseMessage(System.Net.HttpStatusCode.OK);
            httpClientMock.Setup(p => p.SendRequest(It.IsAny<MerchantRequest>())).Returns(responseMessage);

            MerchantClientOffline = new MerchantIntegrationOffline(env, country, partnerId, partnerSecret, merchantId, terminalId, httpClientMock.Object);
            var response = this.MerchantClientOffline.PosGetRefundDetails(RandomString(32), RandomString(32), "VND");
            Assert.Equal(System.Net.HttpStatusCode.OK, response.StatusCode);
        }

        public string RandomString(int length)
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghyklmnopqrstuwxyz0123456789";
            return new string(Enumerable.Repeat(chars, length)
              .Select(s => s[random.Next(s.Length)]).ToArray());
        }

        private static StringContent CreateStringContent(string json)
        {
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            content.Headers.ContentType.CharSet = "";
            return content;
        }

    }
}
