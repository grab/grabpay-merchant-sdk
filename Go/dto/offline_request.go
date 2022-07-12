package dto

// CommonPOSRequest defines a struct to represent common POS payment requests
type CommonPOSRequest struct {
	MsgID       string `json:"msgID"`
	GrabID      string `json:"grabID"`
	TerminalID  string `json:"terminalID"`
	Currency    string `json:"currency"`
	PartnerTxID string `json:"partnerTxID"`
}

// PosCreateQRCodeRequest defines a struct to represent PosCreateQRCode request
type PosCreateQRCodeRequest struct {
	CommonPOSRequest
	Amount int64 `json:"amount"`
	// MerchantDisplayName string `json:"merchantDisplayName"`
}

// PosPerformQRCodeRequest defines a struct to represent PosPerformQRCode request
type PosPerformQRCodeRequest struct {
	CommonPOSRequest
	Amount int64  `json:"amount"`
	Code   string `json:"code"`
	// MerchantDisplayName string   `json:"merchantDisplayName"`
	AdditionalInfo []string `json:"additionalInfo,omitempty"`
}

// PosCancelRequest defines a struct to represent PosCancel request
type PosCancelRequest struct {
	CommonPOSRequest
	OriginTxID string `json:"origTxID"`
}

// PosRefundRequest defines a struct to represent PosRefund request
type PosRefundRequest struct {
	CommonPOSRequest
	Amount int64  `json:"amount"`
	Reason string `json:"reason"`
}
