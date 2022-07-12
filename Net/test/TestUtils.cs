using System;
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
    }
}