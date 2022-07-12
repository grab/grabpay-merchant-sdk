using System;
using System.Net.Http;

namespace Net.Public
{
    public class MerchantRequest
    {
        public Uri Uri { get; }
        public HttpMethod Method { get; }
        public string ContentType { get; }
        public HttpContent Content { get; }
        public string AccessToken { get;}
        public string ContentStr {
            get {
                if (Content == null) return "";
                return Content.ReadAsStringAsync().Result;
            }
        }
        public DateTime Date { get; }
        public string DateStr { get; }
        public string PathName { get; }

        public MerchantRequest(
            Uri uri,
            HttpMethod method,
            HttpContent content = null,
            string accessToken = null,
            string contentType = "application/json",
            DateTime? date = null,
            string pathName = null
            )
        {
            this.Uri = uri;
            this.Method = method;
            this.Content = content;
            this.AccessToken = accessToken;
            this.ContentType = contentType;
            this.Date = date ?? DateTime.UtcNow;
            this.PathName = pathName;

            this.DateStr = string.Format("{0:r}", Date);
        }
    }
}
