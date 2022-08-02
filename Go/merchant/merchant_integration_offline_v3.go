package merchant

import (
	"context"
	"net/http"

	"github.com/grab/grabpay-merchant-sdk/config"
	dtoPOS "github.com/grab/grabpay-merchant-sdk/dto/v3"
)

type OfflineTransactionV3 interface {
	POSInitiate(ctx context.Context, params *dtoPOS.POSInitQRPaymentParams) (*http.Response, error)
	POSInquire(ctx context.Context, params *dtoPOS.POSInquireQRPaymentParams) (*http.Response, error)
	POSCancel(ctx context.Context, params *dtoPOS.POSCancelQRPaymentParams) (*http.Response, error)
	POSRefund(ctx context.Context, params *dtoPOS.POSRefundQRPaymentParams) (*http.Response, error)
}

type merchantIntegrationOfflineV3 struct {
	config *config.Config
	domain string
}

// NewMerchantOfflineV3 function defines instance with config parameters to call APIs for OnA transactions
func NewMerchantOfflineV3(env string, country string, partnerID string, partnerSecret string, merchantID string, terminalID string) OfflineTransactionV3 {
	newMerchant := &merchantIntegrationOfflineV3{}
	_config := new(config.Config)
	_config.Init(env, country, partnerID, partnerSecret, merchantID, terminalID, "", "", "")
	newMerchant.config = _config
	newMerchant.domain = _config.Domain
	return newMerchant
}
