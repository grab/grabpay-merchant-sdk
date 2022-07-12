using System;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace Net.Public
{
    public static class Utils
    {

        public static string Sha256(string payload)
        {
            var sha256 = SHA256.Create();
            return Convert.ToBase64String(sha256.ComputeHash(Encoding.UTF8.GetBytes(payload)));
        }

        public static string Base64URLEncode(string data)
        {
            return data.Replace("=", "").Replace("+", "-").Replace("/", "_");
        }

        private static Random random = new Random();

        public static string RandomString(int length)
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghyklmnopqrstuwxyz0123456789";
            return new string(Enumerable.Repeat(chars, length)
              .Select(s => s[random.Next(s.Length)]).ToArray());
        }

    }
}
