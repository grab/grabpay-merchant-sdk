package config

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func Test_Init(t *testing.T) {
	t.Parallel()

	expected := &Config{
		SdkSignature:  "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7",
		SdkVersion:    "2.0.0",
		Environment:   "",
		Country:       "",
		PartnerID:     "569c95f1-1234-1234-1234-541d8f2af81e",
		PartnerSecret: "1234",
		MerchantID:    "merchantID",
		TerminalID:    "terminalID",
		ClientID:      "clientID",
		ClientSecret:  "clientSecret",
		RedirectUri:   "redirectUri",
		Domain:        "",
	}
	globalPaths := ApiPath{
		OnaChargeInit:      globalOnaChargeInitPath,
		OnaOAuth2Token:     globalOnaOAuth2TokenPath,
		OnaChargeComplete:  globalOnaChargeCompletePath,
		OnaChargeStatus:    globalOnaChargeStatusPath,
		OnaRefund:          globalOnaRefundPath,
		OnaGetRefundStatus: globalOnaRefundStatusPath,
		OnaOTCStatus:       globalOnaOTCStatusPath,
		PosCreateQRCode:    globalPosCreateQRCodePath,
		PosPerformQRCode:   globalPosPerformQRCodePath,
		PosCancel:          globalPosCancelPath,
		PosRefund:          globalPosRefundPath,
		PosTxnDetails:      globalPosTxnDetailsPath,
		POSInit:            v3GlobalPOSInitPath,
		POSRefund:          v3GlobalPOSRefundPath,
		POSCancel:          v3GlobalPOSCancelPath,
		POSInquire:         v3GlobalPOSInquirePath,
	}

	vnPaths := ApiPath{
		OnaChargeInit:      vnOnaChargeInitPath,
		OnaOAuth2Token:     vnOnaOAuth2TokenPath,
		OnaChargeComplete:  vnOnaChargeCompletePath,
		OnaChargeStatus:    vnOnaChargeStatusPath,
		OnaRefund:          vnOnaRefundPath,
		OnaGetRefundStatus: vnOnaRefundStatusPath,
		OnaOTCStatus:       vnOnaOTCStatusPath,
		PosCreateQRCode:    vnPosCreateQRCodePath,
		PosPerformQRCode:   vnPosPerformQRCodePath,
		PosCancel:          vnPosCancelPath,
		PosRefund:          vnPosRefundPath,
		PosTxnDetails:      vnPosTxnDetailsPath,
	}
	tests := []struct {
		desc       string
		env        string
		country    string
		expCountry string
		expEnv     string
		expDomain  string
		expPaths   ApiPath
	}{
		{
			desc:       "stg - unknown country",
			env:        "sTg",
			country:    "countryX",
			expCountry: "COUNTRYX",
			expEnv:     "STG",
			expDomain:  "https://partner-api.stg-myteksi.com",
			expPaths:   globalPaths,
		},
		{
			desc:       "prd - unknown country",
			env:        "prD",
			country:    "countryX",
			expCountry: "COUNTRYX",
			expEnv:     "PRD",
			expDomain:  "https://partner-api.grab.com",
			expPaths:   globalPaths,
		},
		{
			desc:       "stg - VN",
			env:        "sTg",
			country:    "vn",
			expCountry: "VN",
			expEnv:     "STG",
			expDomain:  "https://stg-paysi.moca.vn",
			expPaths:   vnPaths,
		},
		{
			desc:       "prd - VN",
			env:        "prD",
			country:    "vN",
			expCountry: "VN",
			expEnv:     "PRD",
			expDomain:  "https://partner-gw.moca.vn",
			expPaths:   vnPaths,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			conf := &Config{}
			conf.Init(test.env, test.country, "569c95f1-1234-1234-1234-541d8f2af81e", "1234", "merchantID", "terminalID", "clientID", "clientSecret", "redirectUri")

			expected.Country = test.expCountry
			expected.Environment = test.expEnv
			expected.Domain = test.expDomain
			expected.Path = test.expPaths
			assert.Equal(t, conf, expected)
		})
	}
}

func Test_GetOnaGetRefundStatus(t *testing.T) {
	t.Parallel()

	// VN
	apiPath := ApiPath{
		OnaGetRefundStatus: vnOnaRefundStatusPath,
	}
	path := apiPath.GetOnaGetRefundStatus("rtxnID123", "VND")
	assert.Equal(t, "/mocapay/partner/v2/refund/rtxnID123/status?currency=VND", path)

	path = apiPath.GetOnaGetRefundStatus("", "VND")
	assert.Equal(t, "/mocapay/partner/v2/refund//status?currency=VND", path)

	path = apiPath.GetOnaGetRefundStatus("x", "-")
	assert.Equal(t, "/mocapay/partner/v2/refund/x/status?currency=-", path)

	// Global
	apiPath.OnaGetRefundStatus = globalOnaRefundStatusPath
	path = apiPath.GetOnaGetRefundStatus("rtxnID123", "SGD")
	assert.Equal(t, "/grabpay/partner/v2/refund/rtxnID123/status?currency=SGD", path)
}

func Test_GetOnaChargeStatus(t *testing.T) {
	t.Parallel()

	// VN
	apiPath := ApiPath{
		OnaChargeStatus: vnOnaChargeStatusPath,
	}
	path := apiPath.GetOnaChargeStatus("rtxnID123", "VND")
	assert.Equal(t, "/mocapay/partner/v2/charge/rtxnID123/status?currency=VND", path)

	path = apiPath.GetOnaChargeStatus("", "VND")
	assert.Equal(t, "/mocapay/partner/v2/charge//status?currency=VND", path)

	path = apiPath.GetOnaChargeStatus("x", "-")
	assert.Equal(t, "/mocapay/partner/v2/charge/x/status?currency=-", path)

	// Global
	apiPath.OnaChargeStatus = globalOnaChargeStatusPath
	path = apiPath.GetOnaChargeStatus("rtxnID123", "SGD")
	assert.Equal(t, "/grabpay/partner/v2/charge/rtxnID123/status?currency=SGD", path)
}

func Test_GetOnaGetOTCStatus(t *testing.T) {
	t.Parallel()

	// VN
	apiPath := ApiPath{
		OnaOTCStatus: vnOnaOTCStatusPath,
	}
	path := apiPath.GetOnaGetOTCStatus("rtxnID123", "VND")
	assert.Equal(t, "/mocapay/partner/v2/one-time-charge/rtxnID123/status?currency=VND", path)

	path = apiPath.GetOnaGetOTCStatus("", "VND")
	assert.Equal(t, "/mocapay/partner/v2/one-time-charge//status?currency=VND", path)

	path = apiPath.GetOnaGetOTCStatus("x", "-")
	assert.Equal(t, "/mocapay/partner/v2/one-time-charge/x/status?currency=-", path)

	// Global
	apiPath.OnaOTCStatus = globalOnaOTCStatusPath
	path = apiPath.GetOnaGetOTCStatus("rtxnID123", "SGD")
	assert.Equal(t, "/grabpay/partner/v2/one-time-charge/rtxnID123/status?currency=SGD", path)
}

func Test_GetPosTxnDetails(t *testing.T) {
	t.Parallel()

	apiPath := ApiPath{
		PosTxnDetails: vnPosTxnDetailsPath,
	}
	path := apiPath.GetPosTxnDetails("msg123", "gid123", "tnl123", "VND", "P2M", "prtID123", []string{"amountBreakdown"})
	assert.Equal(t, "/mocapay/partners/v1/terminal/transaction/prtID123?currency=VND&msgID=msg123&txType=P2M&grabID=gid123&terminalID=tnl123&additionalinfo=amountBreakdown", path)
	path = apiPath.GetPosTxnDetails("msg123", "gid123", "tnl123", "VND", "P2M", "prtID123", []string{"amountBreakdown", "someitem"})
	assert.Equal(t, "/mocapay/partners/v1/terminal/transaction/prtID123?currency=VND&msgID=msg123&txType=P2M&grabID=gid123&terminalID=tnl123&additionalinfo=amountBreakdown,someitem", path)
	path = apiPath.GetPosTxnDetails("msg123", "gid123", "tnl123", "VND", "P2Mabc", "prtID123", []string{})
	assert.Equal(t, "/mocapay/partners/v1/terminal/transaction/prtID123?currency=VND&msgID=msg123&txType=P2Mabc&grabID=gid123&terminalID=tnl123", path)

	apiPath.PosTxnDetails = globalPosTxnDetailsPath
	path = apiPath.GetPosTxnDetails("msg123", "gid123", "tnl123", "USD", "P2M", "prtID123", []string{"amountBreakdown"})
	assert.Equal(t, "/grabpay/partner/v1/terminal/transaction/prtID123?currency=USD&msgID=msg123&txType=P2M&grabID=gid123&terminalID=tnl123&additionalinfo=amountBreakdown", path)
	path = apiPath.GetPosTxnDetails("msg123", "gid123", "tnl123", "SGD", "P2M", "prtID123", []string{})
	assert.Equal(t, "/grabpay/partner/v1/terminal/transaction/prtID123?currency=SGD&msgID=msg123&txType=P2M&grabID=gid123&terminalID=tnl123", path)
}
