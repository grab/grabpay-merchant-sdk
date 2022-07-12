package dto

import (
	"github.com/grab/grabpay-merchant-sdk/utils"
)

// OnaChargeInitParams struct defines all needed variables for OnaChargeInit function
type OnaChargeInitParams struct {
	PartnerTxID        string
	PartnerGroupTxID   string
	Amount             int64
	Description        string
	Currency           string
	MetaInfo           *ChargeMetaInfo  `json:"metaInfo,omitempty"`
	Items              []ItemInfo       `json:"items,omitempty"`
	ShippingDetails    *ShippingDetails `json:"shippingDetails,omitempty"`
	HidePaymentMethods *[]string        `json:"hidePaymentMethods"`
}

func (o *OnaChargeInitParams) ConvertToOnaChargeInitRequest(merchantID string) *OnaChargeInitRequest {
	return &OnaChargeInitRequest{
		CommonPaymentRequest: CommonPaymentRequest{
			PartnerTxID:      o.PartnerTxID,
			PartnerGroupTxID: o.PartnerGroupTxID,
			MerchantID:       merchantID,
			Amount:           o.Amount,
			Description:      o.Description,
			Currency:         o.Currency,
		},
		MetaInfo:           o.MetaInfo,
		ShippingDetails:    o.ShippingDetails,
		Items:              o.Items,
		HidePaymentMethods: o.HidePaymentMethods,
	}
}

// OnaCreateWebUrlParams struct defines all needed variables for OnaCreateWebUrl function
type OnaCreateWebUrlParams struct {
	OnaChargeInitParams
	State        string
	CodeVerifier string
}

func (o *OnaCreateWebUrlParams) ConvertToOnaChargeInitParams() *OnaChargeInitParams {
	return &OnaChargeInitParams{
		PartnerTxID:        o.PartnerTxID,
		PartnerGroupTxID:   o.PartnerGroupTxID,
		Amount:             o.Amount,
		Currency:           o.Currency,
		Description:        o.Description,
		MetaInfo:           o.MetaInfo,
		Items:              o.Items,
		ShippingDetails:    o.ShippingDetails,
		HidePaymentMethods: o.HidePaymentMethods,
	}
}

// OnaOAuth2TokenParams struct defines all needed variables for OnaOAuth2Token function
type OnaOAuth2TokenParams struct {
	CodeVerifier string
	Code         string
}

func (o *OnaOAuth2TokenParams) ConvertToOnaOAuth2TokenRequest(clientID string, clientSecret string, redirectUri string) *OnaOAuth2TokenRequest {
	return &OnaOAuth2TokenRequest{
		GrantType:    "authorization_code",
		ClientID:     clientID,
		ClientSecret: clientSecret,
		CodeVerifier: utils.Base64UrlEncode(o.CodeVerifier),
		Code:         o.Code,
		RedirectUri:  redirectUri,
	}
}

// OnaChargeCompleteParams struct defines all needed variables for OnaChargeComplete function
type OnaChargeCompleteParams struct {
	PartnerTxID string
	AccessToken string
}

func (o *OnaChargeCompleteParams) ConvertToOnaChargeCompleteRequest() *OnaChargeCompleteRequest {
	return &OnaChargeCompleteRequest{
		PartnerTxID: o.PartnerTxID,
	}
}

// OnaGetChargeStatusParams struct defines all needed variables for OnaGetChargeStatus function
type OnaGetChargeStatusParams struct {
	PartnerTxID string
	Currency    string
	AccessToken string
}

// OnaRefundParams struct defines all needed variables for OnaRefund function
type OnaRefundParams struct {
	RefundPartnerTxID string
	PartnerGroupTxID  string
	Amount            int64
	Currency          string
	TxID              string
	Description       string
	AccessToken       string
}

func (o *OnaRefundParams) ConvertToOnaRefundRequest(merchantID string) *OnaRefundRequest {
	return &OnaRefundRequest{
		CommonPaymentRequest: CommonPaymentRequest{
			PartnerTxID:      o.RefundPartnerTxID,
			PartnerGroupTxID: o.PartnerGroupTxID,
			MerchantID:       merchantID,
			Amount:           o.Amount,
			Description:      o.Description,
			Currency:         o.Currency,
		},
		OriginTxID: o.TxID,
	}
}

// OnaGetRefundStatusParams struct defines all needed variables for OnaGetRefundStatus function
type OnaGetRefundStatusParams struct {
	RefundPartnerTxID string
	Currency          string
	AccessToken       string
}

// OnaGetOTCStatusParams struct defines all needed variables for OnaGetOTCStatus function
type OnaGetOTCStatusParams struct {
	PartnerTxID string
	Currency    string
}

// ChargeMetaInfo defines the detailed information for metaInfo included in transaction
type ChargeMetaInfo struct {
	BrandName          string              `json:"brandName,omitempty"`
	PluginVersion      string              `json:"pluginVersion,omitempty"`
	Location           *LocationInfo       `json:"location,omitempty"`
	Device             *DeviceInfo         `json:"device,omitempty"`
	SubMerchant        *SubMerchantContext `json:"subMerchant,omitempty"`
	PartnerName        string              `json:"partnerName,omitempty"`
	MexIntegrationType string              `json:"mexIntegrationType,omitempty"`
	PartnerUserInfo    *PartnerUserInfo    `json:"partnerUserInfo,omitempty"`
	MerchantURL        string              `json:"merchantURL,omitempty"` // used for shopify+lending
	Echo               string              `json:"echo,omitempty"`        // used to send back to partner during notification if they sending it
}

// ShippingDetails defines the detailed information for shipping included in transaction
type ShippingDetails struct {
	FirstName   string `json:"firstName,omitempty"`
	LastName    string `json:"lastName,omitempty"`
	Address     string `json:"address,omitempty"`
	City        string `json:"city,omitempty"`
	PostalCode  string `json:"postalCode,omitempty"`
	Phone       string `json:"phone,omitempty"`
	Email       string `json:"email,omitempty"`
	CountryCode string `json:"countryCode,omitempty"`
}

// ItemInfo defines the detailed information for items included in transaction
type ItemInfo struct {
	ItemName        string          `json:"itemName"`
	Quantity        int64           `json:"quantity"`
	Price           int64           `json:"price"`
	Category        *string         `json:"category,omitempty"`
	SupplierName    *string         `json:"supplierName,omitempty"`
	ItemCategory    string          `json:"itemCategory,omitempty"`
	URL             string          `json:"url,omitempty"`
	ImageURL        string          `json:"imageURL,omitempty"`
	SupplierDetails *SupplierDetail `json:"supplierDetails,omitempty"`
}

// SupplierDetail defines the detailed information shipping transaction
type SupplierDetail struct {
	SupplierID      string  `json:"supplierID,omitempty"`
	SupplierName    string  `json:"supplierName,omitempty"`
	SupplierURL     string  `json:"supplierUrl,omitempty"`
	LegalID         string  `json:"legalID,omitempty"`
	SupplierPhone   string  `json:"supplierPhone,omitempty"`
	SupplierEmail   string  `json:"supplierEmail,omitempty"`
	SupplierAddress string  `json:"supplierAddress,omitempty"`
	SupplierRating  float64 `json:"supplierRating,omitempty"`
}

// LocationInfo defines the location where payment was initiated.
type LocationInfo struct {
	IPAddress *string  `json:"ipAddress,omitempty"`
	Latitude  *float64 `json:"latitude,omitempty"`
	Longitude *float64 `json:"longitude,omitempty"`
	Accuracy  *float64 `json:"accuracy,omitempty"`
}

// DeviceInfo defines the device information used to initiate payment.
type DeviceInfo struct {
	DeviceID    string  `json:"deviceID,omitempty"`
	DeviceModel *string `json:"deviceModel,omitempty"`
	DeviceBrand *string `json:"deviceBrand,omitempty"`
	IOSUDID     *string `json:"iosUDID,omitempty"` // required for IOS devices
	IMEI        *string `json:"imei,omitempty"`    // required for Android devices
}

// SubMerchantContext defines the lowest level merchant which pax is transacting with
type SubMerchantContext struct {
	Name     *string `json:"name,omitempty"`
	Category *string `json:"category,omitempty"`
}

// PartnerUserInfo is data user from partner
type PartnerUserInfo struct {
	UserID string `json:"userID,omitempty"`
	Phone  string `json:"phone,omitempty"`
	Email  string `json:"email,omitempty"`
}
