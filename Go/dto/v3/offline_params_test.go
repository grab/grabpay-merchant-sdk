package v3

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
	storeID          = "123h3lsks-38h2gsjs"
	msgID            = "some-msg-id-here"
)

func TestConvertToPOSInitQRPaymentRequest(t *testing.T) {
	t.Parallel()

	param := &POSInitQRPaymentParams{
		POSDetails: &POSDetailsRequest{
			TerminalID: "new tesrminal",
		},
		PaymentMethod: &POSPaymentMethod{
			PaymentMethodExclusion: nil,
			MinAmtPostpaid:         50,
			MinAmt4Instalment:      50,
		},
		TransactionDetails: &POSInitTransactionDetails{
			PartnerTxID:       partnerTxID,
			PartnerGroupTxID:  partnerGroupTxID,
			Amount:            amount,
			Currency:          currency,
			BillRefNumber:     "somethinghere",
			GrabTxID:          partnerTxID,
			StoreGrabID:       storeID,
			PaymentChannel:    "some-channel",
			PaymentExpiryTime: 60,
		},
	}

	expected := &POSInitQRPaymentRequest{
		MsgID:              msgID,
		TransactionDetails: param.TransactionDetails,
		PaymentMethod:      param.PaymentMethod,
		POSDetails:         param.POSDetails,
	}
	output := param.ConvertToPOSInitQRPaymentRequest(msgID)

	assert.Equal(t, expected, output, "Output should be the same as expected")
}

func TestConvertToPOSInquireQRPaymentRequest(t *testing.T) {
	t.Parallel()

	param := &POSInquireQRPaymentParams{
		TransactionDetails: &POSInquireTransactionDetails{
			TxRefID:        partnerTxID,
			TxType:         partnerGroupTxID,
			TxRefType:      description,
			Currency:       currency,
			StoreGrabID:    storeID,
			PaymentChannel: "some-channel",
		},
	}

	output := param.ConvertToPOSInquireQRPaymentRequest(msgID)

	assert.Equal(t, msgID, output.MsgID, "msgID should be remain as input")
	assert.Equal(t, param.TransactionDetails, output.TransactionDetails, "TransactionDetails should be remained")
}

func TestConvertToPOSCancelQRPaymentRequest(t *testing.T) {
	t.Parallel()
	param := &POSCancelQRPaymentParams{
		TransactionDetails: &POSCancelTransactionDetails{
			OriginPartnerTxID: partnerTxID,
			Currency:          currency,
			StoreGrabID:       storeID,
			PaymentChannel:    "some-channel",
		},
	}

	output := param.ConvertToPOSCancelQRPaymentRequest(msgID)
	assert.Equal(t, msgID, output.MsgID, "msgID should be remain as input")
	assert.Equal(t, param.TransactionDetails, output.TransactionDetails, "TransactionDetails should be remained")
}

func TestConvertToPOSRefundQRPaymentRequest(t *testing.T) {
	t.Parallel()
	param := &POSRefundQRPaymentParams{
		TransactionDetails: &POSRefundTransactionDetails{
			OriginPartnerTxID: partnerTxID,
			Currency:          currency,
			StoreGrabID:       storeID,
			PaymentChannel:    "some-channel",
			Reason:            description,
			PartnerGroupTxID:  partnerGroupTxID,
			PartnerTxID:       partnerTxID,
			Amount:            amount,
		},
	}

	output := param.ConvertToPOSRefundQRPaymentRequest(msgID)
	assert.Equal(t, msgID, output.MsgID, "msgID should be remain as input")
	assert.Equal(t, param.TransactionDetails, output.TransactionDetails, "TransactionDetails should be remained")
}
