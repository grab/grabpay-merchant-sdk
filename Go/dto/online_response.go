package dto

// OnaChargeInitResponse defines the body struct of API "/partner/v2/charge/init"
type OnaChargeInitResponse struct {
	PartnerTxID string `json:"partnerTxID"`
	Request     string `json:"request"`
}

// OnaChargeCompleteResponse defines the body struct of API "/partner/v2/charge/complete"
type OnaChargeCompleteResponse struct {
	PaymentResponse
}

// OnaGetChargeStatusResponse defines the body struct of API "/partner/v2/charge/{partnerTxID}/status"
type OnaGetChargeStatusResponse struct {
	PaymentResponse
}

// OnaRefundResponse defines the body struct of API "/partner/v2/refund"
type OnaRefundResponse struct {
	PaymentResponse
}

// OnaGetRefundStatusResponse defines the body struct of API "/partner/v2/refund/{refundPartnerTxID}/status"
type OnaGetRefundStatusResponse struct {
	PaymentResponse
}

// PaymentResponse defines the common struct for multiple payment detail APIs
type PaymentResponse struct {
	TxID          string `json:"txID"`
	Status        string `json:"status"`
	Description   string `json:"description"`
	TxStatus      string `json:"txStatus"`
	Reason        string `json:"reason"`
	PaymentMethod string `json:"paymentMethod,omitempty"`
	MsgID         string `json:"msgID"`
}

// OnaGetOTCStatusResponse defines the body struct of API "/partner/v2/one-time-charge/{partnerTxID}/status"
type OnaGetOTCStatusResponse struct {
	TxID          string `json:"txID"`
	OAuthCode     string `json:"oAuthCode"`
	Status        string `json:"status"`
	PaymentMethod string `json:"paymentMethod,omitempty"`
	TxStatus      string `json:"txStatus"`
	Reason        string `json:"reason"`
}

// OnaOAuth2TokenResponse defines the body struct of API "/grabid/v1/oauth2/token"
type OnaOAuth2TokenResponse struct {
	AccessToken  string `json:"access_token"`
	TokenType    string `json:"token_type"`
	ExpiresIn    int64  `json:"expires_in"`
	IDToken      string `json:"id_token,omitempty"`
	RefreshToken string `json:"refresh_token,omitempty"`
}

// ======= How to use this =======
// - after calling an API, ex. resp, err := sdkInstance.OnaChargeInit(...), we can
// -   first, initiate a respObj := &dto.OnaChargeInitResponse
// -   then, decode resp with pkg.DecodeResponse(ctx, resp, respObj) in package pkg
// - and eventually, use respObj to get values we want, such as respObj.Request
// ===============================
