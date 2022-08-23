using System;
using System.Collections.Generic;
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

        public static string BuildQuery(Dictionary<string, dynamic> parameters)
        {
            Dictionary<string, dynamic> outputParams = new Dictionary<string, dynamic>();
            // flat params

            FlatParams(parameters, outputParams, "");

            var result = "?";
            foreach (KeyValuePair<string, dynamic> kvp in outputParams)
            {
                result += result.Length == 1 ? "" : "&";
                result += kvp.Key + "=" + kvp.Value;
            }
            return result;
        }

        private static void FlatParams(Dictionary<string, dynamic> parameters, Dictionary<string, dynamic> outputParams, string keyStr)
        {
            foreach (KeyValuePair<string, dynamic> kvp in parameters)
            {
                if (kvp.Value is Dictionary<string, dynamic>)
                {
                    FlatParams(kvp.Value, outputParams, keyStr + kvp.Key + ".");
                } else
                {
                    outputParams.Add(keyStr + kvp.Key, kvp.Value);
                }
            }
        }

    }
}
