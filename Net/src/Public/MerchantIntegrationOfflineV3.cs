using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using Newtonsoft.Json;

namespace Net.Public
{
    public class MerchantIntegrationOfflineV3
    {

        public MerchantConfiguration MerchantConfiguration { get; }
        public IHttpClient HttpClient { get; }

        public MerchantIntegrationOfflineV3(
            string env = null,
            string country = null,
            string partnerId = null,
            string partnerSecret = null,
            string merchantId = null,
            string terminalId = null,
            IHttpClient httpClient = null
            )
        {
            MerchantConfiguration = new MerchantConfiguration(partnerId, partnerSecret, merchantId, "", "", terminalId, "", env, country);
            HttpClient = httpClient ?? new MerchantHttpClient(MerchantConfiguration);
        }

        public HttpResponseMessage PosInitate(Dictionary<string, dynamic> transactionDetails, Dictionary<string, dynamic> paymentMethod, Dictionary<string, dynamic> POSDetails)
        {

            string msgID = Utils.RandomString(32);

            var requestBody = new
            {
                msgID,
                transactionDetails,
                paymentMethod,
                POSDetails
            };

            var content = CreateStringContent(JsonConvert.SerializeObject(requestBody));
            var uri = MerchantConfiguration.BuildUri(PathName.V3PosPaymentInit);
            var request = new MerchantRequest(uri, HttpMethod.Post, content, pathName: PathName.V3PosPaymentInit);
            return HttpClient.SendRequest(request);

        }


        public HttpResponseMessage PosInquire(Dictionary<string, dynamic> transactionDetails)
        {

            string msgID = Utils.RandomString(32);

            var parametersDictionary = new Dictionary<string, dynamic>();
            parametersDictionary.Add("msgID", msgID);
            parametersDictionary.Add("transactionDetails", transactionDetails);
            var paramStr = Utils.BuildQuery(parametersDictionary);
            var uri = MerchantConfiguration.BuildUri(PathName.V3PosPaymentInquiry, parameters: paramStr);
            var content = CreateStringContent("");
            content.Headers.ContentType = new MediaTypeHeaderValue("application/x-www-form-urlencoded");
            var request = new MerchantRequest(uri, HttpMethod.Get, content, contentType: "application/x-www-form-urlencoded", pathName: PathName.V3PosPaymentInquiry);
            return HttpClient.SendRequest(request);
        }

        public HttpResponseMessage PosCancel(Dictionary<string, dynamic> transactionDetails)
        {
            string msgID = Utils.RandomString(32);
            var requestBody = new
            {
                msgID,
                transactionDetails
            };

            var content = CreateStringContent(JsonConvert.SerializeObject(requestBody));
            var uri = MerchantConfiguration.BuildUri(PathName.V3PosCancel);
            var request = new MerchantRequest(uri, HttpMethod.Put, content, pathName: PathName.V3PosCancel);
            return HttpClient.SendRequest(request);
        }

        public HttpResponseMessage PosRefund(Dictionary<string, dynamic> transactionDetails)
        {
            string msgID = Utils.RandomString(32);
            var requestBody = new
            {
                msgID,
                transactionDetails
            };

            var content = CreateStringContent(JsonConvert.SerializeObject(requestBody));
            var uri = MerchantConfiguration.BuildUri(PathName.V3PosRefund);
            var request = new MerchantRequest(uri, HttpMethod.Put, content, pathName: PathName.V3PosRefund);
            return HttpClient.SendRequest(request);
        }

        /// <summary>
        /// Return StringContent object without CharSet
        /// </summary>
        /// <param name="json">json string</param>
        /// <returns></returns>
        private static StringContent CreateStringContent(string json)
        {
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            content.Headers.ContentType.CharSet = "";
            return content;
        }
    }
}
