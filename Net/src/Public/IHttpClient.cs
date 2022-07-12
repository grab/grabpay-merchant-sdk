using System;
using System.Net.Http;

namespace Net.Public
{
    public interface IHttpClient
    {
        HttpResponseMessage SendRequest(MerchantRequest request);
    }
}
