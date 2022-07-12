using System;
using Xunit;
using Net.Public;
using System.Net.Http;
using Newtonsoft.Json;
using System.Text;

namespace NetTest
{
    public class TestMerchantHttpClient
    {

        MerchantConfiguration MocaConfiguration;
        MerchantHttpClient  HttpClient;
        public TestMerchantHttpClient()
        {
            var partnerId = "partner-id";
            var partnerSecret = "partner-secret";
            var merchantId = "merchant-id";
            var clientId = "client-id";
            var clientSecret = "client-secret";
            var env = "STG";
            var country = "VN";
            var redirectUrl = "http://localhost:8888/result";

            this.MocaConfiguration = new MerchantConfiguration(partnerId, partnerSecret, merchantId, clientId, clientSecret, "", redirectUrl, env, country);
            this.HttpClient = new MerchantHttpClient(this.MocaConfiguration);

        }

        [Fact]
        public void TestGeneratePOPSignature()
        {
            string expected = "eyJ0aW1lX3NpbmNlX2Vwb2NoIjoxMjMzOTI3MjIsInNpZyI6IlRFNHNUUnF5VEljTnpWdDB6MUtKMG1uMUNnQ05KT1dMZkNDZkVzRjIyZ2MifQ";
            Assert.Equal(expected, HttpClient.GeneratePOPSignature("hello", 123392722));
        }

        [Fact]
        public void TestGenerateHmacSignature()
        {
            var requestBody = new
            {
                partnerTxID = "2342sahu358dsk34234sfsfd"
            };

            Uri uri = MocaConfiguration.BuildUri(PathName.ChargeInit);
            HttpContent content = CreateStringContent(JsonConvert.SerializeObject(requestBody));

            DateTime myDate = new DateTime(2018, 8, 18, 7, 22, 16);
            MerchantRequest request = new MerchantRequest(uri, HttpMethod.Post, content, pathName: PathName.ChargeInit,date:myDate);

            var result = HttpClient.GenerateHmacSignature(request);
            var expected = "Q0NTYdEq5Z+W0DMAOQBtk+6wwa76o/jg56UK90Eh/AQ=";
            Assert.Equal(expected, result);
        }

        [Fact]
        public void TestHMACSHA256Sign()
        {
            string expected = "8ayXAutfryPKKRpNxG3t3u4qeMza8KQSvtdxTP/7HMQ=";
            var result = HttpClient.HMACSHA256Sign("hello", "world");
            Assert.Equal(expected, Convert.ToBase64String(result));
            
        }

        private StringContent CreateStringContent(string json)
        {
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            content.Headers.ContentType.CharSet = "";
            return content;
        }
    }
}
