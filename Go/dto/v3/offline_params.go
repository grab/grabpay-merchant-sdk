package v3

// POSInitQRPaymentParams ...
type POSInitQRPaymentParams struct {
	TransactionDetails *POSInitTransactionDetails `json:"transactionDetails,omitempty"`
	PaymentMethod      *POSPaymentMethod          `json:"paymentMethod,omitempty"`
	POSDetails         *POSDetailsRequest         `json:"POSDetails,omitempty"`
}

// POSInquireQRPaymentParams ...
type POSInquireQRPaymentParams struct {
	TransactionDetails *POSInquireTransactionDetails `json:"transactionDetails,omitempty"`
}

// POSCancelQRPaymentParams ...
type POSCancelQRPaymentParams struct {
	TransactionDetails *POSCancelTransactionDetails `json:"transactionDetails,omitempty"`
}

// POSRefundQRPaymentParams ...
type POSRefundQRPaymentParams struct {
	TransactionDetails *POSRefundTransactionDetails `json:"transactionDetails,omitempty"`
}

func (p *POSInitQRPaymentParams) ConvertToPOSInitQRPaymentRequest(msgID string) *POSInitQRPaymentRequest {
	return &POSInitQRPaymentRequest{
		MsgID:              msgID,
		TransactionDetails: p.TransactionDetails,
		PaymentMethod:      p.PaymentMethod,
		POSDetails:         p.POSDetails,
	}
}

func (p *POSInquireQRPaymentParams) ConvertToPOSInquireQRPaymentRequest(msgID string) *POSInquireQRPaymentRequest {
	return &POSInquireQRPaymentRequest{
		MsgID:              msgID,
		TransactionDetails: p.TransactionDetails,
	}
}

func (p *POSCancelQRPaymentParams) ConvertToPOSCancelQRPaymentRequest(msgID string) *POSCancelQRPaymentRequest {
	return &POSCancelQRPaymentRequest{
		MsgID:              msgID,
		TransactionDetails: p.TransactionDetails,
	}
}

func (p *POSRefundQRPaymentParams) ConvertToPOSRefundQRPaymentRequest(msgID string) *POSRefundQRPaymentRequest {
	return &POSRefundQRPaymentRequest{
		MsgID:              msgID,
		TransactionDetails: p.TransactionDetails,
	}
}
