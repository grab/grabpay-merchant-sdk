package dto

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestConvertToPosCreateQRCodeRequest(t *testing.T) {

	t.Parallel()

	tests := []struct {
		desc       string
		params     []string
		receiver   *PosCreateQRCodeParams
		merchantID string
		terminalID string
	}{
		{
			desc: "first",
			receiver: &PosCreateQRCodeParams{
				PartnerTxID: partnerTxID,
				MsgID:       "some-msg-ID",
				Amount:      amount,
				Currency:    "this-country-currency",
			},
			merchantID: "merchant-ID-here",
			terminalID: "terminal-ID-here",
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToPosCreateQRCodeRequest(test.merchantID, test.terminalID)
			assert.Equal(t, test.receiver.PartnerTxID, req.PartnerTxID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.MsgID, req.MsgID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Amount, req.Amount, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Currency, req.Currency, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.merchantID, req.GrabID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.terminalID, req.TerminalID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")

		})
	}
}

func TestConvertToPosPerformQRCodeRequest(t *testing.T) {

	t.Parallel()

	tests := []struct {
		desc       string
		params     []string
		receiver   *PosPerformQRCodeParams
		merchantID string
		terminalID string
	}{
		{
			desc: "first",
			receiver: &PosPerformQRCodeParams{
				PartnerTxID: partnerTxID,
				MsgID:       "some-msg-ID",
				Amount:      amount,
				Currency:    "this-country-currency",
				Code:        "this-is-code",
			},
			merchantID: "merchant-ID-here",
			terminalID: "terminal-ID-here",
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToPosPerformQRCodeRequest(test.merchantID, test.terminalID)
			assert.Equal(t, test.receiver.PartnerTxID, req.PartnerTxID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.MsgID, req.MsgID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Amount, req.Amount, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Currency, req.Currency, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Code, req.Code, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.merchantID, req.GrabID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.terminalID, req.TerminalID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
		})
	}
}
func TestConvertToPosCancelRequest(t *testing.T) {

	t.Parallel()

	tests := []struct {
		desc       string
		params     []string
		receiver   *PosCancelParams
		merchantID string
		terminalID string
	}{
		{
			desc: "first",
			receiver: &PosCancelParams{
				PartnerTxID:     partnerTxID,
				MsgID:           "some-msg-ID",
				OrigTxID:        "origin-Tx-ID-here",
				OrigPartnerTxID: "origin-partner-tx-ID-here",
				Currency:        "this-country-currency",
			},
			merchantID: "merchant-ID-here",
			terminalID: "terminal-ID-here",
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToPosCancelRequest(test.merchantID, test.terminalID)
			assert.Equal(t, test.receiver.PartnerTxID, req.PartnerTxID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.MsgID, req.MsgID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Currency, req.Currency, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.OrigTxID, req.OriginTxID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.merchantID, req.GrabID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.terminalID, req.TerminalID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")

		})
	}
}

func TestConvertToPosRefundRequest(t *testing.T) {

	t.Parallel()

	tests := []struct {
		desc       string
		params     []string
		receiver   *PosRefundParams
		merchantID string
		terminalID string
	}{
		{
			desc: "first",
			receiver: &PosRefundParams{
				RefundPartnerTxID: partnerTxID,
				MsgID:             "some-msg-ID",
				Amount:            amount,
				Currency:          "this-country-currency",
				Description:       description,
				OrigPartnerTxID:   "this-tx-id",
			},
			merchantID: "merchant-ID-here",
			terminalID: "terminal-ID-here",
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			req := test.receiver.ConvertToPosRefundRequest(test.merchantID, test.terminalID)
			assert.Equal(t, test.receiver.RefundPartnerTxID, req.PartnerTxID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.MsgID, req.MsgID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Amount, req.Amount, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Currency, req.Currency, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.receiver.Description, req.Reason, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.merchantID, req.GrabID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")
			assert.Equal(t, test.terminalID, req.TerminalID, "actual and expected result is different when ConvertToPosCreateQRCodeRequest")

		})
	}
}
