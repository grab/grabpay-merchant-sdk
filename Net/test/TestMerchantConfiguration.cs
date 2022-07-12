using System;
using Xunit;
using Net.Public;
using System.Net.Http;
using Newtonsoft.Json;
using System.Text;

namespace NetTest
{
    public class TestMerchantConfiguration
    {
        [Fact]
        public void TestDomain()
        {

            var vn_prd_domain = "https://partner-gw.moca.vn";
            var vn_stg_domain = "https://stg-paysi.moca.vn";
            var oth_prd_domain = "https://partner-api.grab.com";
            var oth_stg_domain = "https://partner-api.stg-myteksi.com";

            MerchantConfiguration configuration = new MerchantConfiguration(env: "STG", country: "VN");
            Assert.Equal(vn_stg_domain, configuration.Domain);

            configuration = new MerchantConfiguration(env: "STG", country: "SG");
            Assert.Equal(oth_stg_domain, configuration.Domain);

            configuration = new MerchantConfiguration(env: "PRD", country: "VN");
            Assert.Equal(vn_prd_domain, configuration.Domain);

            configuration = new MerchantConfiguration(env: "PRD", country: "MY");
            Assert.Equal(oth_prd_domain, configuration.Domain);
        }

        [Fact]
        public void TestBuildUri()
        {
            MerchantConfiguration configuration = new MerchantConfiguration(env: "STG", country: "VN");

            var expectedResult = configuration.Domain + "/mocapay/partner/v2/charge/init";
            Assert.Equal(expectedResult, configuration.BuildUri(PathName.ChargeInit).ToString());


            expectedResult = configuration.Domain + "/mocapay/partner/v2/charge/hello/status?currency=world";
            object[] requestParams = {"hello","world"};
            Assert.Equal(expectedResult, configuration.BuildUri(PathName.OnaChargeStatus, requestParams).ToString());
        }
    }
}