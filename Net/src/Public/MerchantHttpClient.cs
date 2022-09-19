using System;
using System.IO;
using System.Net.Http;
using System.Security.Cryptography;
using System.Text;
using Newtonsoft.Json;

namespace Net.Public
{
    public class MerchantHttpClient : IHttpClient
    {

        private readonly MerchantConfiguration MerchantConfiguration;
        private HttpClient httpClient = new HttpClient();

        public MerchantHttpClient(MerchantConfiguration merchantConfiguration)
        {
            this.MerchantConfiguration = merchantConfiguration;
            httpClient.DefaultRequestHeaders.TryAddWithoutValidation("Content-Type", "application/x-www-form-urlencoded");
        }

        public HttpResponseMessage SendRequest(MerchantRequest request)
        {
            var httpRequest = BuildRequestMessage(request);
            var response = this.httpClient.SendAsync(httpRequest).GetAwaiter().GetResult();
            return response;
        }

        public HttpRequestMessage BuildRequestMessage(MerchantRequest request)
        {
            var requestMessage = new HttpRequestMessage(request.Method, request.Uri);
            requestMessage.Headers.Add("X-Sdk-Country", MerchantConfiguration.Country);
            requestMessage.Headers.Add("X-Sdk-Version", MerchantConfiguration.SdkVersion);
            requestMessage.Headers.Add("X-Sdk-Language", "DOTNET");
            requestMessage.Headers.Add("X-Sdk-Signature", MerchantConfiguration.SdkSignature);
            // HEADERS
            requestMessage.Headers.Add("Date", request.DateStr);

            if (request.PathName == PathName.ChargeInit
                || request.PathName == PathName.OneTimeChargeStatus
                || request.PathName == PathName.CreateQrCode
                || request.PathName == PathName.PerformTxn
                || request.PathName == PathName.CancelQrTxn
                || request.PathName == PathName.PosRefundTxn
                || request.PathName == PathName.PosChargeStatus
                || request.PathName == PathName.PosRefundTxn
                || request.PathName == PathName.V3PosPaymentInit
                || request.PathName == PathName.V3PosCancel
                || request.PathName == PathName.V3PosRefund
                || request.PathName == PathName.V3PosPaymentInquiry)
            {
                var authorizationHeader = string.Format("{0}:{1}", MerchantConfiguration.PartnerId, GenerateHmacSignature(request));
                requestMessage.Headers.TryAddWithoutValidation("Authorization", authorizationHeader);
            }
            else if (request.PathName == PathName.ChargeComplete
                || request.PathName == PathName.OnaChargeStatus
                || request.PathName == PathName.OnaRefundTxn
                || request.PathName == PathName.OnaCheckRefundTxn)
            {
                requestMessage.Headers.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", request.AccessToken);
                requestMessage.Headers.Add("X-GID-AUX-POP", GeneratePOPSignature(request.AccessToken, ((DateTimeOffset)request.Date).ToUnixTimeSeconds()));
            }

            // CONTENTS
            requestMessage.Content = request.Content;
            return requestMessage;
        }

        public string GeneratePOPSignature(string accessToken, long timestampUnix)
        {
            var message = timestampUnix.ToString() + accessToken;
            var signature = Convert.ToBase64String(HMACSHA256Sign(MerchantConfiguration.ClientSecret, message));
            var sub = Utils.Base64URLEncode(signature);

            var payload = new
            {
                time_since_epoch = timestampUnix,
                sig = sub
            };

            var payloadBytes = JsonConvert.SerializeObject(payload);
            var result = btoa(payloadBytes).Replace("=", "").Replace("+", "-").Replace("/", "_");
            return result;
        }


        public string GenerateHmacSignature(MerchantRequest request)
        {
            string contentDigest;
            contentDigest = request.ContentStr == "" ? "" : Utils.Sha256(request.ContentStr);
            //contentDigest = Utils.Sha256(request.ContentStr);
            var requestDataArray = new string[]
            {
                request.Method.ToString(),
                request.ContentType,
                request.DateStr,
                request.Uri.AbsolutePath + request.Uri.Query,
                contentDigest,
                ""
            };
            var requestDataString = string.Join("\n", requestDataArray);
            var hmacString = Convert.ToBase64String(HMACSHA256Sign(this.MerchantConfiguration.PartnerSecret, requestDataString));
            return hmacString;
        }

        public static string btoa(string toEncode)
        {
            byte[] bytes = Encoding.GetEncoding(28591).GetBytes(toEncode);
            string toReturn = System.Convert.ToBase64String(bytes);
            return toReturn;
        }

        public byte[] HMACSHA256Sign(string key, string content)
        {
            var keyBytes = Encoding.UTF8.GetBytes(key);
            var contentBytes = Encoding.UTF8.GetBytes(content);
            using (var hmac = new HMACSHA256(keyBytes))
            {
                using (var contentStream = new MemoryStream(contentBytes))
                {
                    return hmac.ComputeHash(contentStream);
                }
            }
        }

    }
}
