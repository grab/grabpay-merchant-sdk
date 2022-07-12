package dto

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

const (
	partnerTxID      = "partner-tx-id"
	partnerGroupTxID = "partner-group-tx-id"
	amount           = int64(2000)
	description      = "description"
	currency         = "currency"
)

func initParams() ([]ItemInfo, *[]string, *ChargeMetaInfo, *ShippingDetails) {
	var items = []ItemInfo{
		{
			ItemName: "Checking",
			Price:    1234,
		},
	}
	var hidePaymentMethods = []string{"INSTALMENT", "POSTPAID"}
	chargeMetaInfo := &ChargeMetaInfo{BrandName: "test"}
	shippingDetails := &ShippingDetails{Address: "Somewhere"}
	return items, &hidePaymentMethods, chargeMetaInfo, shippingDetails
}

func TestConvertToOnaChargeInitRequest(t *testing.T) {
	items, hidePaymentMethods, metaInfo, shippingDetails := initParams()
	o := &OnaChargeInitParams{
		PartnerTxID:        partnerTxID,
		PartnerGroupTxID:   partnerGroupTxID,
		Amount:             amount,
		Description:        description,
		Currency:           currency,
		MetaInfo:           metaInfo,
		Items:              items,
		ShippingDetails:    shippingDetails,
		HidePaymentMethods: hidePaymentMethods,
	}
	req := o.ConvertToOnaChargeInitRequest("merchant-id")

	assert.Equal(t, partnerTxID, req.PartnerTxID, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, partnerGroupTxID, req.PartnerGroupTxID, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, amount, req.Amount, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, description, req.Description, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, currency, req.Currency, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, metaInfo, req.MetaInfo, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, items, req.Items, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, shippingDetails, req.ShippingDetails, "actual and expected result is different when convertToOnaChargeInitRequest")
	assert.Equal(t, hidePaymentMethods, req.HidePaymentMethods, "actual and expected result is different when convertToOnaChargeInitRequest")
}

func TestConvertToOnaOAuth2TokenRequest(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc         string
		params       []string
		receiver     *OnaOAuth2TokenParams
		codeVerified string
	}{
		{
			desc: "first",
			params: []string{
				"client-id", "client-secret", "redirect-url",
			},
			receiver: &OnaOAuth2TokenParams{
				CodeVerifier: "123",
				Code:         "valid Code",
			},
			codeVerified: "123",
		},
		{
			desc: "second",
			params: []string{
				"client-id", "client-secret", "redirect-url",
			},
			receiver: &OnaOAuth2TokenParams{
				CodeVerifier: "123",
				Code:         "valid Code",
			},
			codeVerified: "123",
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToOnaOAuth2TokenRequest(test.params[0], test.params[1], test.params[2])
			assert.Equal(t, test.params[0], req.ClientID, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
			assert.Equal(t, test.params[1], req.ClientSecret, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
			assert.Equal(t, test.params[2], req.RedirectUri, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
			assert.Equal(t, test.receiver.Code, req.Code, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
			assert.Equal(t, test.codeVerified, req.CodeVerifier, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
			assert.Equal(t, "authorization_code", req.GrantType, "actual and expected result is different when convertToOnaOAuth2TokenRequest")
		})
	}
}

func TestConvertToOnaChargeCompleteRequest(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc     string
		params   []string
		receiver *OnaChargeCompleteParams
	}{
		{
			desc: "first",
			receiver: &OnaChargeCompleteParams{
				PartnerTxID: partnerTxID,
				AccessToken: "access_token_here",
			},
		},
		{
			desc: "second",
			receiver: &OnaChargeCompleteParams{
				PartnerTxID: "partner-tx-id",
				AccessToken: "can we try something else",
			},
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToOnaChargeCompleteRequest()
			assert.Equal(t, test.receiver.PartnerTxID, req.PartnerTxID)
		})
	}
}

func TestConvertToOnaRefundRequest(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc       string
		merchantID string
		receiver   *OnaRefundParams
	}{
		{
			desc: "first",
			receiver: &OnaRefundParams{
				RefundPartnerTxID: "refund-partner-here",
				PartnerGroupTxID:  "partner-group-tx-id",
				Amount:            int64(2),
				Currency:          "SGD",
				TxID:              "this-is-tx-id",
				Description:       "nothing to tell",
				AccessToken:       "access_token_here",
			},
			merchantID: "can we leave",
		},
		{
			desc: "second",
			receiver: &OnaRefundParams{
				RefundPartnerTxID: "",
				PartnerGroupTxID:  "",
				Amount:            int64(2),
				Currency:          "SGD",
				TxID:              "this-is-tx-id",
				Description:       "nothing to tell",
				AccessToken:       "access_token_here",
			},
			merchantID: "",
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToOnaRefundRequest(test.merchantID)
			assert.Equal(t, test.merchantID, req.MerchantID, "actual and expected result is different when ConvertToOnaRefundRequest")
			assert.Equal(t, test.receiver.RefundPartnerTxID, req.PartnerTxID, "actual and expected result is different when ConvertToOnaRefundRequest")
			assert.Equal(t, test.receiver.PartnerGroupTxID, req.PartnerGroupTxID, "actual and expected result is different when ConvertToOnaRefundRequest")
			assert.Equal(t, test.receiver.TxID, req.OriginTxID, "actual and expected result is different when ConvertToOnaRefundRequest")
			assert.Equal(t, test.receiver.Currency, req.Currency, "actual and expected result is different when ConvertToOnaRefundRequest")
			assert.Equal(t, test.receiver.Description, req.Description, "actual and expected result is different when ConvertToOnaRefundRequest")
		})
	}
}

func TestConvertToOnaChargeInitParams(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc     string
		receiver *OnaCreateWebUrlParams
	}{
		{
			desc: "first",
			receiver: &OnaCreateWebUrlParams{
				OnaChargeInitParams: OnaChargeInitParams{
					PartnerTxID:        partnerTxID,
					PartnerGroupTxID:   partnerGroupTxID,
					Amount:             amount,
					Description:        description,
					Currency:           currency,
					MetaInfo:           nil,
					Items:              nil,
					ShippingDetails:    nil,
					HidePaymentMethods: nil,
				},
				State: "some-state-here",
			},
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToOnaChargeInitParams()
			assert.Equal(t, test.receiver.OnaChargeInitParams, *req)
		})
	}
}
