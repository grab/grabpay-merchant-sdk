package merchant

import (
	"context"
	"net/url"

	"github.com/grab/grabpay-merchant-sdk/config"
	"github.com/grab/grabpay-merchant-sdk/dto"
)

type OnlineTransaction interface {
	buildHttpUrlWithQuery(partnerTxID, currency, state, request string) (*url.URL, error)
	OnaChargeInit(ctx context.Context, params *dto.OnaChargeInitParams) (*dto.OnaChargeInitResponse, error)
	OnaCreateWebUrl(ctx context.Context, params *dto.OnaCreateWebUrlParams) (string, string, error)
	OnaOAuth2Token(ctx context.Context, params *dto.OnaOAuth2TokenParams) (*dto.OnaOAuth2TokenResponse, error)
	OnaChargeComplete(ctx context.Context, params *dto.OnaChargeCompleteParams) (*dto.OnaChargeCompleteResponse, error)
	OnaGetChargeStatus(ctx context.Context, params *dto.OnaGetChargeStatusParams) (*dto.OnaGetChargeStatusResponse, error)
	OnaRefund(ctx context.Context, params *dto.OnaRefundParams) (*dto.OnaRefundResponse, error)
	OnaGetRefundStatus(ctx context.Context, params *dto.OnaGetRefundStatusParams) (*dto.OnaGetRefundStatusResponse, error)
	OnaGetOTCStatus(ctx context.Context, params *dto.OnaGetOTCStatusParams) (*dto.OnaGetOTCStatusResponse, error)
}

type merchantIntegrationOnline struct {
	config *config.Config
	domain string
}

// NewMerchantOnline function defines instance with config parameters to call APIs for OnA transactions
func NewMerchantOnline(env string, country string, partnerID string, partnerSecret string, merchantID string,
	clientID string, clientSecret string, redirectUri string) OnlineTransaction {
	newMerchant := &merchantIntegrationOnline{}
	_config := new(config.Config)
	_config.Init(env, country, partnerID, partnerSecret, merchantID, "", clientID, clientSecret, redirectUri)
	newMerchant.config = _config
	newMerchant.domain = _config.Domain
	return newMerchant
}
