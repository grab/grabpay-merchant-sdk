package dto

// PosCreateQRCodeResponse defines the body struct of API "/partners/v1/terminal/qrcode/create"
type PosCreateQRCodeResponse struct {
	MsgID  string `json:"msgID"`
	QRCode string `json:"qrcode"`
	TxID   string `json:"txID"`
}

// PosPerformQRCodeResponse defines the body struct of API "/partners/v1/terminal/transaction/perform"
type PosPerformQRCodeResponse struct {
	MsgID          string             `json:"msgID"`
	TxID           string             `json:"txID"`
	Status         string             `json:"status"`
	Amount         int64              `json:"amount"`
	Updated        int64              `json:"updated"`
	Currency       string             `json:"currency"`
	ErrorMsg       string             `json:"errMsg,omitempty"`
	AdditionalInfo *P2MAdditionalInfo `json:"additionalInfo,omitempty"`
}

// P2MAdditionalInfo defines the P2M additional information for transaction
type P2MAdditionalInfo struct {
	AmountBreakdown *AmountBreakdown `json:"amountBreakdown,omitempty"`
}

// AmountBreakdown defines the AmountBreakdown information for P2MAdditionalInfo
type AmountBreakdown struct {
	DiscountAmount                 *int64 `json:"discountAmount,omitempty"`
	PaidAmount                     *int64 `json:"paidAmount,omitempty"`
	MerchantFundedPromo            int64  `json:"merchantFundedPromo,omitempty"`
	MerchantRetentionPromoAmount   int64  `json:"merchantRetentionPromoAmount,omitempty"`
	MerchantAcquisitionPromoAmount int64  `json:"merchantAcquisitionPromoAmount,omitempty"`
	GrabPromoAmount                int64  `json:"grabPromoAmount,omitempty"`
	GrabPointsAmount               int64  `json:"grabPointsAmount,omitempty"`
	BonusPointsMultiplier          string `json:"bonusPointsMultiplier,omitempty"`
	BonusPointsAwarded             int64  `json:"bonusPointsAwarded,omitempty"`

	// Attributes to store refunded values
	RefundedChargeAmount *int64 `json:"refundedChargeAmount,omitempty"`
	RevokedPayoutAmount  *int64 `json:"revokePayoutAmount,omitempty"`
	RefundedPointsAmount int64  `json:"refundedPointsAmount,omitempty"`
	RefundedPoints       int64  `json:"refundedPoints,omitempty"`
	RefundedPromoAmount  int64  `json:"refundedPromoAmount,omitempty"`
}

// PosCancelResponse defines the body struct of API "/partners/v1/terminal/transaction/{origPartnerTxID}/cancel"
type PosCancelResponse struct {
	// No response elements are returned.
}

// PosRefundResponse defines the body struct of API "/partners/v1/terminal/transaction/{origPartnerTxID}/refund"
type PosRefundResponse struct {
	MsgID          string             `json:"msgID"`
	TxID           string             `json:"txID"`
	OriginTxID     string             `json:"originTxID"`
	Status         string             `json:"status"`
	Description    string             `json:"description"` // not yet used. TODO to consider return refund fail reason like charge module.
	AdditionalInfo *P2MAdditionalInfo `json:"additionalInfo,omitempty"`
	Msg            string             `json:"msg"`
}

// PosGetTxnDetailsResponse defines the body struct of API "/partners/v1/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType=P2M&grabID={grabID}&terminalID={terminalID}" with txType=P2M
type PosGetTxnDetailsResponse struct {
	MsgID          string             `json:"msgID"`
	TxID           string             `json:"txID"`
	Status         string             `json:"status"`
	Amount         int64              `json:"amount"`
	Updated        int64              `json:"updated"`
	Currency       string             `json:"currency"`
	ErrorMsg       string             `json:"errMsg,omitempty"`
	AdditionalInfo *P2MAdditionalInfo `json:"additionalInfo,omitempty"`
}

// ======= How to use this =======
// - after calling an API, ex. resp, err := sdkInstance.PosGetTxnDetails(...), we can
// -   first, initiate a respObj := &dto.PosGetTxnDetailsResponse
// -   then, decode resp with pkg.DecodeResponse(ctx, resp, respObj) in package pkg
// - and eventually, use respObj to get values we want, such as respObj.Status
// ===============================
