// Package config provides constants and functions to get correct paths for APIs
package config

import (
	"errors"
	"fmt"
	"net/url"
	"strings"

	v3 "github.com/grab/grabpay-merchant-sdk/dto/v3"
)

const (
	sdkSignature = "6EB66646DBF103DC114E34AE6E01C261A217A820357C3B08F3D4E7D4475853C7"
	sdkVersion   = "2.0.0"
)
const (
	// domains
	vnProdDomain = "https://partner-gw.moca.vn"
	vnStgDomain  = "https://stg-paysi.moca.vn"

	// online path
	vnOnaChargeInitPath     = "/mocapay/partner/v2/charge/init"
	vnOnaOAuth2TokenPath    = "/grabid/v1/oauth2/token"
	vnOnaChargeCompletePath = "/mocapay/partner/v2/charge/complete"
	vnOnaChargeStatusPath   = "/mocapay/partner/v2/charge/{partnerTxID}/status?currency={currency}"
	vnOnaRefundPath         = "/mocapay/partner/v2/refund"
	vnOnaRefundStatusPath   = "/mocapay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}"
	vnOnaOTCStatusPath      = "/mocapay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}"
	// offline path
	vnPosCreateQRCodePath  = "/mocapay/partners/v1/terminal/qrcode/create"
	vnPosPerformQRCodePath = "/mocapay/partners/v1/terminal/transaction/perform"
	vnPosCancelPath        = "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/cancel"
	vnPosRefundPath        = "/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/refund"
	vnPosTxnDetailsPath    = "/mocapay/partners/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}&grabID={grabID}&terminalID={terminalID}"
)

const (
	// domains
	globalProdDomain = "https://partner-api.grab.com"
	globalStgDomain  = "https://partner-api.stg-myteksi.com"

	// online path
	globalOnaChargeInitPath     = "/grabpay/partner/v2/charge/init"
	globalOnaOAuth2TokenPath    = "/grabid/v1/oauth2/token"
	globalOnaChargeCompletePath = "/grabpay/partner/v2/charge/complete"
	globalOnaChargeStatusPath   = "/grabpay/partner/v2/charge/{partnerTxID}/status?currency={currency}"
	globalOnaRefundPath         = "/grabpay/partner/v2/refund"
	globalOnaRefundStatusPath   = "/grabpay/partner/v2/refund/{refundPartnerTxID}/status?currency={currency}"
	globalOnaOTCStatusPath      = "/grabpay/partner/v2/one-time-charge/{partnerTxID}/status?currency={currency}"

	// offline path
	globalPosCreateQRCodePath  = "/grabpay/partner/v1/terminal/qrcode/create"
	globalPosPerformQRCodePath = "/grabpay/partner/v1/terminal/transaction/perform"
	globalPosCancelPath        = "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/cancel"
	globalPosRefundPath        = "/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/refund"
	globalPosTxnDetailsPath    = "/grabpay/partner/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType={posTxType}&grabID={grabID}&terminalID={terminalID}"

	// offline path v3
	v3GlobalPOSInitPath    = "/grabpay/partner/v3/payment/init"
	v3GlobalPOSInquirePath = "/grabpay/partner/v3/payment/inquiry"
	v3GlobalPOSCancelPath  = "/grabpay/partner/v3/payment/cancellation"
	v3GlobalPOSRefundPath  = "/grabpay/partner/v3/payment/refund"
)

const (
	onaInit         = "OnaInit"
	onaOauth        = "OnaOauth"
	onaComplete     = "OnaComplete"
	onaStatus       = "OnaStatus"
	onaRefund       = "OnaRefund"
	onaRefundStatus = "OnaRefundStatus"
	onaOTCStatus    = "OnaOTCStatus"

	posQRCreate   = "PosQRCreate"
	posQRPerform  = "PosQRPerform"
	posCancel     = "PosCancel"
	posRefund     = "PosRefund"
	posTxnDetails = "PosTxDetails"
)

const (
	EnvPRD = "PRD"
	EnvSTG = "STG"

	CountryGlobal = "RG"
	CountryVN     = "VN"
	CountryMY     = "MY"
	CountrySG     = "SG"
	CountryTH     = "TH"
)

var (
	ErrFailedParseUrl = errors.New("cannot parse url")
)

type ApiPath struct {
	OnaChargeInit      string
	OnaOAuth2Token     string
	OnaChargeComplete  string
	OnaChargeStatus    string
	OnaRefund          string
	OnaGetRefundStatus string
	OnaOTCStatus       string
	PosCreateQRCode    string
	PosPerformQRCode   string
	PosCancel          string
	PosRefund          string
	PosTxnDetails      string

	POSInit    string
	POSInquire string
	POSCancel  string
	POSRefund  string
}

func (a ApiPath) GetPosTxnDetails(msgID, grabID, terminalID, currency, txType, partnerTxID string, additionalInfo []string) string {
	path := a.PosTxnDetails
	path = strings.Replace(path, "{partnerTxID}", partnerTxID, -1)
	path = strings.Replace(path, "{currency}", currency, -1)
	path = strings.Replace(path, "{posTxType}", txType, -1)
	path = strings.Replace(path, "{msgID}", msgID, -1)
	path = strings.Replace(path, "{grabID}", grabID, -1)
	path = strings.Replace(path, "{terminalID}", terminalID, -1)
	if len(additionalInfo) > 0 {
		path += "&additionalinfo=" + strings.Join(additionalInfo, ",")
	}
	return path
}

func (a ApiPath) GetOnaGetRefundStatus(refundPartnerTxID, currency string) string {
	path := a.OnaGetRefundStatus
	path = strings.Replace(path, "{refundPartnerTxID}", refundPartnerTxID, -1)
	path = strings.Replace(path, "{currency}", currency, -1)
	return path
}

func (a ApiPath) GetOnaChargeStatus(partnerTxID, currency string) string {
	path := a.OnaChargeStatus
	path = strings.Replace(path, "{partnerTxID}", partnerTxID, -1)
	path = strings.Replace(path, "{currency}", currency, -1)
	return path
}
func (a ApiPath) GetOnaGetOTCStatus(partnerTxID, currency string) string {
	path := a.OnaOTCStatus
	path = strings.Replace(path, "{partnerTxID}", partnerTxID, -1)
	path = strings.Replace(path, "{currency}", currency, -1)
	return path
}

func (a ApiPath) GetPOSInquire(msgID string, params *v3.POSInquireQRPaymentParams) (string, error) {
	path := a.POSInquire
	relativeUrl, err := url.Parse(path)
	if err != nil {
		return "", ErrFailedParseUrl
	}

	transactionDetails := params.TransactionDetails

	queryString := relativeUrl.Query()
	queryString.Set("msgID", msgID)
	if transactionDetails.PaymentChannel != "" {
		queryString.Set("transactionDetails.paymentChannel", transactionDetails.PaymentChannel)
	}
	if transactionDetails.StoreGrabID != "" {
		queryString.Set("transactionDetails.storeGrabID", transactionDetails.StoreGrabID)
	}
	if transactionDetails.Currency != "" {
		queryString.Set("transactionDetails.currency", transactionDetails.Currency)
	}
	if transactionDetails.TxType != "" {
		queryString.Set("transactionDetails.txType", transactionDetails.TxType)
	}
	if transactionDetails.TxRefType != "" {
		queryString.Set("transactionDetails.txRefType", transactionDetails.TxRefType)
	}
	if transactionDetails.TxRefID != "" {
		queryString.Set("transactionDetails.txRefID", transactionDetails.TxRefID)
	}

	relativeUrl.RawQuery = queryString.Encode()
	return fmt.Sprintf("%s", relativeUrl.String()), nil
}

type Config struct {
	Environment   string
	Country       string
	PartnerID     string
	PartnerSecret string
	MerchantID    string
	TerminalID    string
	ClientID      string
	ClientSecret  string
	RedirectUri   string
	SdkVersion    string
	SdkSignature  string
	Domain        string
	Path          ApiPath
}

func (config *Config) Init(env string, country string, partnerID string, partnerSecret string, merchantID string, terminalID string,
	clientID string, clientSecret string, redirectUri string) {
	config.SdkSignature = sdkSignature
	config.SdkVersion = sdkVersion

	config.Environment = env
	config.Country = country
	config.PartnerID = partnerID
	config.PartnerSecret = partnerSecret
	config.MerchantID = merchantID
	config.TerminalID = terminalID
	config.ClientID = clientID
	config.ClientSecret = clientSecret
	config.RedirectUri = redirectUri

	config.Environment = strings.ToUpper(config.Environment)
	config.Country = strings.ToUpper(config.Country)

	if config.Environment == EnvPRD {
		if config.Country == CountryVN {
			config.Domain = vnProdDomain
		} else {
			config.Domain = globalProdDomain
		}
	} else {
		if config.Country == CountryVN {
			config.Domain = vnStgDomain
		} else {
			config.Domain = globalStgDomain
		}
	}

	if config.Country == CountryVN {
		// online path
		config.Path.OnaChargeInit = vnOnaChargeInitPath
		config.Path.OnaOAuth2Token = vnOnaOAuth2TokenPath
		config.Path.OnaChargeComplete = vnOnaChargeCompletePath
		config.Path.OnaChargeStatus = vnOnaChargeStatusPath
		config.Path.OnaRefund = vnOnaRefundPath
		config.Path.OnaGetRefundStatus = vnOnaRefundStatusPath
		config.Path.OnaOTCStatus = vnOnaOTCStatusPath
		// offline path
		config.Path.PosCreateQRCode = vnPosCreateQRCodePath
		config.Path.PosPerformQRCode = vnPosPerformQRCodePath
		config.Path.PosCancel = vnPosCancelPath
		config.Path.PosRefund = vnPosRefundPath
		config.Path.PosTxnDetails = vnPosTxnDetailsPath
	} else {
		// online path
		config.Path.OnaChargeInit = globalOnaChargeInitPath
		config.Path.OnaOAuth2Token = globalOnaOAuth2TokenPath
		config.Path.OnaChargeComplete = globalOnaChargeCompletePath
		config.Path.OnaChargeStatus = globalOnaChargeStatusPath
		config.Path.OnaRefund = globalOnaRefundPath
		config.Path.OnaGetRefundStatus = globalOnaRefundStatusPath
		config.Path.OnaOTCStatus = globalOnaOTCStatusPath
		// offline path
		config.Path.PosCreateQRCode = globalPosCreateQRCodePath
		config.Path.PosPerformQRCode = globalPosPerformQRCodePath
		config.Path.PosCancel = globalPosCancelPath
		config.Path.PosRefund = globalPosRefundPath
		config.Path.PosTxnDetails = globalPosTxnDetailsPath

		config.Path.POSInit = v3GlobalPOSInitPath
		config.Path.POSInquire = v3GlobalPOSInquirePath
		config.Path.POSCancel = v3GlobalPOSCancelPath
		config.Path.POSRefund = v3GlobalPOSRefundPath
	}
}
