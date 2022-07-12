using System;
using Xunit;
using Net.Public;
using System.Net.Http;
using Newtonsoft.Json;
using System.Text;

namespace NetTest
{
    public class TestMerchantRequest
    {
        [Fact]
        public void TestConstructor()
        {
            var expectHttpMethod = HttpMethod.Post;
            var now = DateTime.Now;
            Uri uri = new Uri("https://partner-gw.moca.vn");

            var requestBody = new
            {
                hello= "world"
            };
            HttpContent content = CreateStringContent(JsonConvert.SerializeObject(requestBody));

            MerchantRequest mocaRequest = new MerchantRequest(uri, expectHttpMethod, content, date:now);

            Assert.Equal(expectHttpMethod, mocaRequest.Method);
            Assert.Equal(mocaRequest.ContentStr, content.ReadAsStringAsync().Result);
        }


        private StringContent CreateStringContent(string json)
        {
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            content.Headers.ContentType.CharSet = "";
            return content;
        }
    }
}
