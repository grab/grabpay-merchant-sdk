using System;
using System.Collections.Generic;
using Net.Public;
using Xunit;

namespace NetTest
{
    public class TestUtils
    {
        [Fact]
        public void TestRandomStringFunc()
        {
            var rs1 = Utils.RandomString(10);
            var rs2 = Utils.RandomString(10);
            Assert.NotEqual(rs1, rs2);
        }

        [Fact]
        public void TestBase64URLEncode()
        {
            var input = "uU0nuZNNPgilLlLX2n2r+sSE7+N6U4DukIj3rOLvzek=";
            var expected = "uU0nuZNNPgilLlLX2n2r-sSE7-N6U4DukIj3rOLvzek";
            Assert.Equal(expected, Utils.Base64URLEncode(input));
        }

        [Fact]
        public void TestSha256()
        {
            var expected = "uU0nuZNNPgilLlLX2n2r+sSE7+N6U4DukIj3rOLvzek=";
            Assert.Equal(expected, Utils.Sha256("hello world"));
        }

        [Fact]
        public void TestBuildQuery()
        {
            var transactionDetails = new Dictionary<string, dynamic>();
            transactionDetails.Add("paymentChannel", "MPQR");
            transactionDetails.Add("storeGrabID", "ABCD");
            transactionDetails.Add("currency", "SGD");

            var input = new Dictionary<string, dynamic>();
            input.Add("msgID", "8f0b481d7cb54d3081c61492f2ce78c2");
            input.Add("transactionDetails", transactionDetails);
            
            var expected = "?msgID=8f0b481d7cb54d3081c61492f2ce78c2&transactionDetails.paymentChannel=MPQR&transactionDetails.storeGrabID=ABCD&transactionDetails.currency=SGD";
            Assert.Equal(expected, Utils.BuildQuery(input));
        }
    }


}