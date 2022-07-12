package dto

// OnaChargeInitRequest contains request details to initiate one-time charge from partner
type OnaChargeInitRequest struct {
	CommonPaymentRequest
	MetaInfo           *ChargeMetaInfo  `json:"metaInfo,omitempty"`
	Items              []ItemInfo       `json:"items"`
	ShippingDetails    *ShippingDetails `json:"shippingDetails,omitempty"`
	HidePaymentMethods *[]string        `json:"hidePaymentMethods"`
}

// CommonPaymentRequest defines a struct to represent common payment requests
type CommonPaymentRequest struct {
	PartnerTxID      string `json:"partnerTxID"`
	PartnerGroupTxID string `json:"partnerGroupTxID"`
	Currency         string `json:"currency"`
	Amount           int64  `json:"amount"`
	Description      string `json:"description"`
	MerchantID       string `json:"merchantID"`
}

// OnaOAuth2TokenRequest defines a struct to represent OnaOAuth2Token request
type OnaOAuth2TokenRequest struct {
	GrantType    string `json:"grant_type"`
	ClientID     string `json:"client_id"`
	ClientSecret string `json:"client_secret"`
	CodeVerifier string `json:"code_verifier"`
	RedirectUri  string `json:"redirect_uri"`
	Code         string `json:"code"`
}

// OnaChargeCompleteRequest defines a struct to represent OnaChargeComplete request
type OnaChargeCompleteRequest struct {
	PartnerTxID string `json:"partnerTxID"`
}

// OnaGetChargeStatusRequest defines a struct to represent OnaGetChargeStatus request
type OnaGetChargeStatusRequest struct {
}

// OnaRefundRequest defines a struct to represent OnaRefund request
type OnaRefundRequest struct {
	CommonPaymentRequest
	OriginTxID string `json:"originTxID"`
}
