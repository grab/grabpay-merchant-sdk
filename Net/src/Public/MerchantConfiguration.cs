﻿using System;
using System.Collections.Generic;

namespace Net.Public
{
    public class MerchantConfiguration
    {

        public string PartnerId { get; }
        public string PartnerSecret { get; }
        public string MerchantId { get; }
        public string ClientId { get; }
        public string ClientSecret { get; }
        public string TerminalId { get; }
        public string RedirectUri { get; }
        public string Environment { get; }
        public string Country { get; }
        public string Domain { get; }
        public Dictionary<string, string> PathDictionary { get; }
        public string SdkVersion { get; } = "2.0.0";
        public string SdkSignature { get; } = "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7";
        private string HostVnStg { get; } = "https://stg-paysi.moca.vn";
        private string HostVnPrd { get; } = "https://partner-gw.moca.vn";
        private string HostRegionalStg { get; } = "https://partner-api.stg-myteksi.com";
        private string HostRegionalPrd { get; } = "https://partner-api.grab.com";

        public MerchantConfiguration(
            string partnerId = null,
            string partnerSecret = null,
            string merchantId = null,
            string clientId = null,
            string clientSecret = null,
            string terminalId = null,
            string redirectUri = null,
            string env = null,
            string country = null
            )
        {
            this.PartnerId = partnerId;
            this.PartnerSecret = partnerSecret;
            this.MerchantId = merchantId;
            this.ClientId = clientId;
            this.ClientSecret = clientSecret;
            this.TerminalId = terminalId;
            this.RedirectUri = redirectUri;
            this.Environment = env;
            this.Country = country;
            this.Domain = ChooseDomain(env, country);
            this.PathDictionary = ChoosePath(country);
        }

        public Uri BuildUri(string pathName, object[] args = null, string parameters = "")
        {

            string path = this.PathDictionary[pathName] + parameters;

            if (args != null)
            {
                return new Uri(this.Domain + string.Format(path, args));
            }

    
            return new Uri(this.Domain + path);
        }

        private Dictionary<string, string> ChoosePath(string country)
        {
            var result = new Dictionary<string, string>();
            if (country.ToUpper() == "VN")
            {

                // online path
                result.Add(PathName.ChargeInit, "/mocapay/partner/v2/charge/init");
                result.Add(PathName.OAuth2Token, "/grabid/v1/oauth2/token");
                result.Add(PathName.ChargeComplete, "/mocapay/partner/v2/charge/complete");
                result.Add(PathName.OnaChargeStatus, "/mocapay/partner/v2/charge/{0}/status?currency={1}");
                result.Add(PathName.OnaRefundTxn, "/mocapay/partner/v2/refund");
                result.Add(PathName.OnaCheckRefundTxn, "/mocapay/partner/v2/refund/{0}/status?currency={1}");
                result.Add(PathName.OneTimeChargeStatus, "/mocapay/partner/v2/one-time-charge/{0}/status?currency={1}");
                // offline path
                result.Add(PathName.CreateQrCode, "/mocapay/partners/v1/terminal/qrcode/create");
                result.Add(PathName.CancelQrTxn, "/mocapay/partners/v1/terminal/transaction/{0}/cancel");
                result.Add(PathName.PosRefundTxn, "/mocapay/partners/v1/terminal/transaction/{0}/refund");
                result.Add(PathName.PerformTxn, "/mocapay/partners/v1/terminal/transaction/perform");
                result.Add(PathName.PosChargeStatus, "/mocapay/partners/v1/terminal/transaction/{0}?msgID={1}&currency={2}&grabID={3}&terminalID={4}&txType={5}");
            }
            else
            {
                // online path
                result.Add(PathName.ChargeInit, "/grabpay/partner/v2/charge/init");
                result.Add(PathName.OAuth2Token, "/grabid/v1/oauth2/token");
                result.Add(PathName.ChargeComplete, "/grabpay/partner/v2/charge/complete");
                result.Add(PathName.OnaChargeStatus, "/grabpay/partner/v2/charge/{0}/status?currency={1}");
                result.Add(PathName.OnaRefundTxn, "/grabpay/partner/v2/refund");
                result.Add(PathName.OnaCheckRefundTxn, "/grabpay/partner/v2/refund/{0}/status?currency={1}");
                result.Add(PathName.OneTimeChargeStatus, "/grabpay/partner/v2/one-time-charge/{0}/status?currency={1}");
                // offline path
                result.Add(PathName.CreateQrCode, "/grabpay/partner/v1/terminal/qrcode/create");
                result.Add(PathName.CancelQrTxn, "/grabpay/partner/v1/terminal/transaction/{0}/cancel");
                result.Add(PathName.PosRefundTxn, "/grabpay/partner/v1/terminal/transaction/{0}/refund");
                result.Add(PathName.PerformTxn, "/grabpay/partner/v1/terminal/transaction/perform");
                result.Add(PathName.PosChargeStatus, "/grabpay/partner/v1/terminal/transaction/{0}?msgID={1}&currency={2}&txType=P2M&grabID={3}&terminalID={4}");

                // offline v3 path
                result.Add(PathName.V3PosPaymentInit, "/grabpay/partner/v3/payment/init");
                result.Add(PathName.V3PosPaymentInquiry, "/grabpay/partner/v3/payment/inquiry");
                result.Add(PathName.V3PosCancel, "/grabpay/partner/v3/payment/cancellation");
                result.Add(PathName.V3PosRefund, "/grabpay/partner/v3/payment/refund");
            }

            return result;
        }

        private string ChooseDomain(string env, string country)
        {
            if (env.ToUpper() == "PRD")
            {
                if (country.ToUpper() == "VN")
                {
                    return this.HostVnPrd;
                }
                else
                {
                    return this.HostRegionalPrd;
                }
            }
            else
            {
                if (country.ToUpper() == "VN")
                {
                    return this.HostVnStg;
                }
                else
                {
                    return this.HostRegionalStg;
                }

            }
        }
    }
}
