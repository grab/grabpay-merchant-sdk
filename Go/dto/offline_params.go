// Package dto provides predefined request, response parameters for each request, that will be called to GrabPay server
package dto

// PosCreateQRCodeParams ...
type PosCreateQRCodeParams struct {
	MsgID       string
	PartnerTxID string
	Amount      int64
	Currency    string
}

func (p *PosCreateQRCodeParams) ConvertToPosCreateQRCodeRequest(merchantID, terminalID string) *PosCreateQRCodeRequest {
	return &PosCreateQRCodeRequest{
		CommonPOSRequest: CommonPOSRequest{
			MsgID:       p.MsgID,
			GrabID:      merchantID,
			TerminalID:  terminalID,
			Currency:    p.Currency,
			PartnerTxID: p.PartnerTxID,
		},
		Amount: p.Amount,
	}
}

// PosPerformQRCodeParams struct defines all needed variables for PosPerformQRCode function
type PosPerformQRCodeParams struct {
	MsgID          string
	PartnerTxID    string
	Amount         int64
	Currency       string
	Code           string
	AdditionalInfo []string
}

func (p *PosPerformQRCodeParams) ConvertToPosPerformQRCodeRequest(merchantID, terminalID string) *PosPerformQRCodeRequest {
	return &PosPerformQRCodeRequest{
		CommonPOSRequest: CommonPOSRequest{
			MsgID:       p.MsgID,
			GrabID:      merchantID,
			TerminalID:  terminalID,
			Currency:    p.Currency,
			PartnerTxID: p.PartnerTxID,
		},
		Amount:         p.Amount,
		Code:           p.Code,
		AdditionalInfo: p.AdditionalInfo,
	}
}

// PosCancelParams struct defines all needed variables for PosCancel function
type PosCancelParams struct {
	MsgID           string
	PartnerTxID     string
	OrigPartnerTxID string
	OrigTxID        string
	Currency        string
}

func (p *PosCancelParams) ConvertToPosCancelRequest(merchantID, terminalID string) *PosCancelRequest {
	return &PosCancelRequest{
		CommonPOSRequest: CommonPOSRequest{
			MsgID:       p.MsgID,
			GrabID:      merchantID,
			TerminalID:  terminalID,
			Currency:    p.Currency,
			PartnerTxID: p.PartnerTxID,
		},
		OriginTxID: p.OrigTxID,
	}
}

// PosRefundParams struct defines all needed variables for PosRefund function
type PosRefundParams struct {
	MsgID             string
	RefundPartnerTxID string
	Amount            int64
	Currency          string
	OrigPartnerTxID   string
	Description       string
}

func (p *PosRefundParams) ConvertToPosRefundRequest(merchantID, terminalID string) *PosRefundRequest {
	return &PosRefundRequest{
		CommonPOSRequest: CommonPOSRequest{
			MsgID:       p.MsgID,
			GrabID:      merchantID,
			TerminalID:  terminalID,
			Currency:    p.Currency,
			PartnerTxID: p.RefundPartnerTxID,
		},
		Amount: p.Amount,
		Reason: p.Description,
	}
}

// PosGetTxnDetailsParams struct defines all needed variables for PosGetTxnDetails function
type PosGetTxnDetailsParams struct {
	MsgID          string
	PartnerTxID    string
	Currency       string
	AdditionalInfo []string
}

// PosGetRefundDetailsParams struct defines all needed variables for PosGetRefundDetails function
type PosGetRefundDetailsParams struct {
	MsgID             string
	RefundPartnerTxID string
	Currency          string
	AdditionalInfo    []string
}
