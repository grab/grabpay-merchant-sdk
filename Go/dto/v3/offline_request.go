package v3

// POSInitTransactionDetails ...
type POSInitTransactionDetails struct {
	PaymentChannel    string `json:"paymentChannel"`
	StoreGrabID       string `json:"storeGrabID"`
	GrabTxID          string `json:"grabTxID"`
	PartnerTxID       string `json:"partnerTxID"`
	PartnerGroupTxID  string `json:"partnerGroupTxID"`
	BillRefNumber     string `json:"billRefNumber"`
	Amount            int64  `json:"amount"`
	Currency          string `json:"currency"`
	PaymentExpiryTime int64  `json:"paymentExpiryTime"`
}

// POSPaymentMethod ...
type POSPaymentMethod struct {
	PaymentMethodExclusion []string `json:"paymentMethodExclusion,omitempty"`
	MinAmtPostpaid         int64    `json:"minAmtPostpaid,omitempty"`
	MinAmt4Instalment      int64    `json:"minAmt4Instalment,omitempty"`
}

// POSDetailsRequest ...
type POSDetailsRequest struct {
	TerminalID         string `json:"terminalID,omitempty"`
	ConsumerIdentifier string `json:"consumerIdentifier,omitempty"`
}

// POSInquireTransactionDetails ...
type POSInquireTransactionDetails struct {
	PaymentChannel string `json:"paymentChannel,omitempty"`
	StoreGrabID    string `json:"storeGrabID,omitempty"`
	Currency       string `json:"currency,omitempty"`
	TxType         string `json:"txType,omitempty"`
	TxRefType      string `json:"txRefType,omitempty"`
	TxRefID        string `json:"txRefID,omitempty"`
}

// POSCancelTransactionDetails ...
type POSCancelTransactionDetails struct {
	PaymentChannel    string `json:"paymentChannel"`
	StoreGrabID       string `json:"storeGrabID"`
	OriginPartnerTxID string `json:"originPartnerTxID"`
	Currency          string `json:"currency"`
}

// POSRefundTransactionDetails ...
type POSRefundTransactionDetails struct {
	PaymentChannel    string `json:"paymentChannel,omitempty"`
	StoreGrabID       string `json:"storeGrabID,omitempty"`
	OriginPartnerTxID string `json:"originPartnerTxID,omitempty"`
	PartnerTxID       string `json:"partnerTxID,omitempty"`
	PartnerGroupTxID  string `json:"partnerGroupTxID,omitempty"`
	Amount            int64  `json:"amount,omitempty"`
	Currency          string `json:"currency,omitempty"`
	Reason            string `json:"reason,omitempty"`
}

// POSInitQRPaymentRequest embedded Request to transport for POSInitQRPayment RPC in PaysiDealer
type POSInitQRPaymentRequest struct {
	MsgID              string                     `json:"msgID,omitempty"`
	TransactionDetails *POSInitTransactionDetails `json:"transactionDetails,omitempty"`
	PaymentMethod      *POSPaymentMethod          `json:"paymentMethod,omitempty"`
	POSDetails         *POSDetailsRequest         `json:"POSDetails,omitempty"`
}

// POSInquireQRPaymentRequest embedded Request to transport for POSInquireQRPayment RPC in PaysiDealer
type POSInquireQRPaymentRequest struct {
	MsgID              string                        `json:"msgID,omitempty"`
	TransactionDetails *POSInquireTransactionDetails `json:"transactionDetails,omitempty"`
}

// POSCancelQRPaymentRequest embedded Request to transport for POSCancelQRPayment RPC in PaysiDealer
type POSCancelQRPaymentRequest struct {
	MsgID              string                       `json:"msgID,omitempty"`
	TransactionDetails *POSCancelTransactionDetails `json:"transactionDetails,omitempty"`
}

// POSRefundQRPaymentRequest embedded Request to transport for POSRefundQRPayment RPC in PaysiDealer
type POSRefundQRPaymentRequest struct {
	MsgID              string                       `json:"msgID,omitempty"`
	TransactionDetails *POSRefundTransactionDetails `json:"transactionDetails,omitempty"`
}
