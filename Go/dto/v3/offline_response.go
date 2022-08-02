package v3

// POSRefundTransactionRespDetails ...
type POSRefundTransactionRespDetails struct {
	PaymentChannel   string `json:"paymentChannel"`
	StoreGrabID      string `json:"storeGrabID"`
	GrabTxID         string `json:"grabTxID"`
	PartnerTxID      string `json:"partnerTxID"`
	PartnerGroupTxID string `json:"partnerGroupTxID"`
	Amount           int64  `json:"amount"`
	Currency         string `json:"currency"`
	PaymentMethod    string `json:"paymentMethod"`
	UpdatedTime      int64  `json:"updatedTime"`
}

// POSMetadata ...
type POSMetadata struct {
	OffusTxID       string `json:"offusTxID,omitempty"`
	OriginOffusTxID string `json:"originOffusTxID,omitempty"`
}

// POSRefundPromoDetails ...
type POSRefundPromoDetails struct {
	ConsumerRefundedAmt          int64 `json:"consumerRefundedAmt"`
	MerchantFundedPromoRefundAmt int64 `json:"merchantFundedPromoRefundAmt"`
	PointsRefunded               int64 `json:"pointsRefunded"`
}

// OriginTxDetails ...
type OriginTxDetails struct {
	OriginGrabTxID         string `json:"originGrabTxID"`
	OriginPartnerTxID      string `json:"originPartnerTxID"`
	OriginPartnerGroupTxID string `json:"originPartnerGroupTxID"`
	OriginAmount           int64  `json:"originAmount"`
}

// InquirePOSDetails ...
type InquirePOSDetails struct {
	TerminalID string `json:"terminalID"`
}

// StatusDetails ...
type StatusDetails struct {
	Status       string `json:"status"`
	StatusCode   string `json:"statusCode"`
	StatusReason string `json:"statusReason"`
}

// POSDetailsResponse ...
type POSDetailsResponse struct {
	TerminalID string `json:"terminalID"`
	QrPayload  string `json:"qrPayload,omitempty"`
}

// POSInquireTransactionRespDetails ...
type POSInquireTransactionRespDetails struct {
	PaymentChannel    string `json:"paymentChannel"`
	StoreGrabID       string `json:"storeGrabID"`
	TxType            string `json:"txType"`
	GrabTxID          string `json:"grabTxID"`
	PartnerTxID       string `json:"partnerTxID"`
	PartnerGroupTxID  string `json:"partnerGroupTxID"`
	BillRefNumber     string `json:"billRefNumber"`
	Amount            int64  `json:"amount"`
	RefundedAmount    int64  `json:"refundedAmount"`
	Currency          string `json:"currency"`
	PaymentMethod     string `json:"paymentMethod"`
	UpdatedTime       int64  `json:"updatedTime"`
	PaymentExpiryTime int64  `json:"paymentExpiryTime"`
}

type PromoDetails struct {
	PromoCode                    string `json:"promoCode,omitempty"`
	ConsumerPaidAmt              int64  `json:"consumerPaidAmt,omitempty"`
	MerchantFundedPromoAmt       int64  `json:"merchantFundedPromoAmt,omitempty"`
	PointsMultiplier             string `json:"pointsMultiplier,omitempty"`
	PointsAwarded                int64  `json:"pointsAwarded,omitempty"`
	ConsumerRefundedAmt          int64  `json:"consumerRefundedAmt,omitempty"`
	MerchantFundedPromoRefundAmt int64  `json:"merchantFundedPromoRefundAmt,omitempty"`
	PointsRefunded               int64  `json:"pointsRefunded,omitempty"`
}

// POSInitQRPaymentResponse embedded Response to transport for POSInitQRPayment RPC in PaysiDealer
type POSInitQRPaymentResponse struct {
	TransactionDetails *POSInitTransactionDetails `json:"transactionDetails,omitempty"`
	POSDetails         *POSDetailsResponse        `json:"POSDetails,omitempty"`
	StatusDetails      *StatusDetails             `json:"statusDetails"`
}

// POSInquireQRPaymentResponse embedded Response to transport for POSInquireQRPayment RPC in PaysiDealer
type POSInquireQRPaymentResponse struct {
	TransactionDetails *POSInquireTransactionRespDetails `json:"transactionDetails,omitempty"`
	POSDetails         *InquirePOSDetails                `json:"POSDetails,omitempty"`
	StatusDetails      *StatusDetails                    `json:"statusDetails,omitempty"`
	OriginTxDetails    *OriginTxDetails                  `json:"originTxDetails,omitempty"`
	PromoDetails       *PromoDetails                     `json:"promoDetails,omitempty"`
	Metadata           *POSMetadata                      `json:"metadata,omitempty"`
}

// POSCancelQRPaymentResponse embedded Response to transport for POSCancelQRPayment RPC in PaysiDealer
type POSCancelQRPaymentResponse struct {
	TransactionDetails *POSCancelTransactionDetails `json:"transactionDetails,omitempty"`
	StatusDetails      *StatusDetails               `json:"statusDetails,omitempty"`
}

// POSRefundQRPaymentResponse embedded Response to transport for POSRefundQRPayment RPC in PaysiDealer
type POSRefundQRPaymentResponse struct {
	TransactionDetails *POSRefundTransactionRespDetails `json:"transactionDetails,omitempty"`
	OriginTxDetails    *OriginTxDetails                 `json:"originTxDetails,omitempty"`
	PromoDetails       *POSRefundPromoDetails           `json:"promoDetails,omitempty"`
	StatusDetails      *StatusDetails                   `json:"statusDetails,omitempty"`
	Metadata           *POSMetadata                     `json:"metadata,omitempty"`
}
