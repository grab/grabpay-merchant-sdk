// Package merchant provide all APIs for OnA and POS transaction
package merchant

import (
	"context"
	"net/http"

	"github.com/grab/grabpay-merchant-sdk/config"
	"github.com/grab/grabpay-merchant-sdk/dto"
)

type OfflineTransaction interface {
	PosCreateQRCode(ctx context.Context, params *dto.PosCreateQRCodeParams) (*http.Response, error)
	PosPerformQRCode(ctx context.Context, params *dto.PosPerformQRCodeParams) (*http.Response, error)
	PosCancel(ctx context.Context, params *dto.PosCancelParams) (*http.Response, error)
	PosRefund(ctx context.Context, params *dto.PosRefundParams) (*http.Response, error)
	PosGetTxnDetails(ctx context.Context, params *dto.PosGetTxnDetailsParams) (*http.Response, error)
	PosGetRefundDetails(ctx context.Context, params *dto.PosGetRefundDetailsParams) (*http.Response, error)
}

type merchantIntegrationOffline struct {
	config *config.Config
	domain string
}

// NewMerchantOffline function defines instance with config parameters to call APIs for OnA transactions
func NewMerchantOffline(env string, country string, partnerID string, partnerSecret string, merchantID string, terminalID string) OfflineTransaction {
	newMerchant := &merchantIntegrationOffline{}
	_config := new(config.Config)
	_config.Init(env, country, partnerID, partnerSecret, merchantID, terminalID, "", "", "")
	newMerchant.config = _config
	newMerchant.domain = _config.Domain
	return newMerchant
}
