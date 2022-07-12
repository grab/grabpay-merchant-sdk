package merchant

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"net/url"
	"time"

	"github.com/grab/grabpay-merchant-sdk/dto"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

const (
	contentType = "application/json"
)

var (
	ErrInvalidParams         = errors.New("invalid params")
	ErrFailedMarshalBody     = errors.New("cannot marshal body")
	ErrFailedCreateRequest   = errors.New("cannot create request")
	ErrFailedSendRequest     = errors.New("cannot send request to server")
	ErrFailedGetRequestValue = errors.New("cannot get request value from ChargeInit response")
	ErrFailedParseUrl        = errors.New("cannot parse url")
)

// OnaChargeInit function is used to initiate a one-time payment for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaChargeInit(ctx context.Context, params *dto.OnaChargeInitParams) (*dto.OnaChargeInitResponse, error) {
	path := m.config.Path.OnaChargeInit

	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.Currency) == 0 {
		return nil, ErrInvalidParams
	}

	reqBody := params.ConvertToOnaChargeInitRequest(m.config.MerchantID)

	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPost, contentType, path, bytesBody, now)

	response, err := makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaChargeInitResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaCreateWebUrl function is used to generate a Web URL that provides a web interface for Grab's user-level authentication for OnA transaction
// It returns a web url as string type
func (m *merchantIntegrationOnline) OnaCreateWebUrl(ctx context.Context, params *dto.OnaCreateWebUrlParams) (string, string, error) {
	webUrl := ""

	if params == nil {
		return webUrl, "", ErrInvalidParams
	}

	state := params.State
	initParam := params.ConvertToOnaChargeInitParams()
	initResp, err := m.OnaChargeInit(ctx, initParam)
	if err != nil {
		return webUrl, state, ErrFailedCreateRequest
	}

	request := initResp.Request

	if err != nil {
		return webUrl, state, ErrFailedGetRequestValue
	}

	relativeUrl, err := m.buildHttpUrlWithQuery(params.CodeVerifier, params.Currency, state, request)
	if err != nil {
		return webUrl, state, err
	}

	baseUrl, _ := url.Parse(m.config.Domain)
	webUrl = fmt.Sprintf("%s", baseUrl.ResolveReference(relativeUrl))

	return webUrl, state, nil
}

// OnaOAuth2Token function is used to generate an OAuth 2.0 token by passing code received in the return URL from GrabPay for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaOAuth2Token(ctx context.Context, params *dto.OnaOAuth2TokenParams) (*dto.OnaOAuth2TokenResponse, error) {
	path := m.config.Path.OnaOAuth2Token

	// verify params
	if params == nil || len(params.CodeVerifier) == 0 || len(params.Code) == 0 {
		return nil, ErrInvalidParams
	}

	// prepare request body
	reqBody := params.ConvertToOnaOAuth2TokenRequest(m.config.ClientID, m.config.ClientSecret, m.config.RedirectUri)

	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)

	response, err := makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaOAuth2TokenResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaChargeComplete function is used to complete the payment authorized by the user for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaChargeComplete(ctx context.Context, params *dto.OnaChargeCompleteParams) (*dto.OnaChargeCompleteResponse, error) {
	path := m.config.Path.OnaChargeComplete

	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.AccessToken) == 0 {
		return nil, ErrInvalidParams
	}

	// prepare request body
	reqBody := params.ConvertToOnaChargeCompleteRequest()

	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendAccessTokenAndSigToHeaders(m.config, headers, params.AccessToken, now)

	response, err := makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaChargeCompleteResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaGetChargeStatus function is used to check the current status of the transaction for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaGetChargeStatus(ctx context.Context, params *dto.OnaGetChargeStatusParams) (*dto.OnaGetChargeStatusResponse, error) {
	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.AccessToken) == 0 || len(params.Currency) == 0 {
		return nil, ErrInvalidParams
	}
	path := m.config.Path.GetOnaChargeStatus(params.PartnerTxID, params.Currency)

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendAccessTokenAndSigToHeaders(m.config, headers, params.AccessToken, now)

	response, err := makeRequest(m.domain, headers, http.MethodGet, path, nil)
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaGetChargeStatusResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaRefund function is used to generate full or partial refunds for a specific transaction for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaRefund(ctx context.Context, params *dto.OnaRefundParams) (*dto.OnaRefundResponse, error) {
	path := m.config.Path.OnaRefund

	// verify params
	if params == nil || len(params.PartnerGroupTxID) == 0 ||
		len(params.AccessToken) == 0 || len(params.Currency) == 0 || len(params.Description) == 0 || len(params.TxID) == 0 || len(params.RefundPartnerTxID) == 0 {
		return nil, ErrInvalidParams
	}

	// prepare request body
	reqBody := params.ConvertToOnaRefundRequest(m.config.MerchantID)

	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendAccessTokenAndSigToHeaders(m.config, headers, params.AccessToken, now)

	response, err := makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaRefundResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaGetRefundStatus function is used to check the refund status for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaGetRefundStatus(ctx context.Context, params *dto.OnaGetRefundStatusParams) (*dto.OnaGetRefundStatusResponse, error) {
	// verify params
	if params == nil || len(params.RefundPartnerTxID) == 0 || len(params.AccessToken) == 0 || len(params.Currency) == 0 {
		return nil, ErrInvalidParams
	}

	path := m.config.Path.GetOnaGetRefundStatus(params.RefundPartnerTxID, params.Currency)

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendAccessTokenAndSigToHeaders(m.config, headers, params.AccessToken, now)

	//return makeRequest(m.domain, headers, http.MethodGet, path, nil)
	response, err := makeRequest(m.domain, headers, http.MethodGet, path, nil)
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaGetRefundStatusResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

// OnaGetOTCStatus function is used to check the payment status for OnA transaction
// It returns http.Response, body of which is the same in dto.online_response.go file
func (m *merchantIntegrationOnline) OnaGetOTCStatus(ctx context.Context, params *dto.OnaGetOTCStatusParams) (*dto.OnaGetOTCStatusResponse, error) {
	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.Currency) == 0 {
		return nil, ErrInvalidParams
	}

	path := m.config.Path.GetOnaGetOTCStatus(params.PartnerTxID, params.Currency)

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, "", now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodGet, contentType, path, nil, now)

	//return makeRequest(m.domain, headers, http.MethodGet, path, nil)
	response, err := makeRequest(m.domain, headers, http.MethodGet, path, nil)
	if err != nil {
		return nil, ErrFailedSendRequest
	}
	initResponse := &dto.OnaGetOTCStatusResponse{}
	err = ProcessResponse(response, initResponse)

	if err != nil {
		return nil, err
	}
	return initResponse, nil
}

func (m *merchantIntegrationOnline) buildHttpUrlWithQuery(codeVerifier, currency, state, request string) (*url.URL, error) {
	codeChallenge := utils.Base64UrlEncode(utils.GenSha256(codeVerifier))
	acrValues := fmt.Sprintf("consent_ctx:countryCode=%s,currency=%s", m.config.Country, currency)
	codeChallengeMethod := "S256"
	nonce := utils.GenerateRandomString(16)
	responseType := "code"

	if len(state) == 0 {
		state = utils.GenerateRandomString(7)
	}

	path := "/grabid/v1/oauth2/authorize"
	relativeUrl, err := url.Parse(path)
	if err != nil {
		return nil, ErrFailedParseUrl
	}

	queryString := relativeUrl.Query()
	queryString.Set("acr_values", acrValues)
	queryString.Set("client_id", m.config.ClientID)
	queryString.Set("code_challenge", codeChallenge)
	queryString.Set("code_challenge_method", codeChallengeMethod)
	queryString.Set("nonce", nonce)
	queryString.Set("redirect_uri", m.config.RedirectUri)
	queryString.Set("request", request)
	queryString.Set("response_type", responseType)
	queryString.Set("state", state)

	relativeUrl.RawQuery = queryString.Encode()

	scope := "payment.vn.one_time_charge"
	if m.config.Country != "VN" {
		scope = "openid+payment.one_time_charge" // if encode scope with "+" in "openid+payment...", url does not work
	}
	relativeUrl.RawQuery += fmt.Sprintf("&scope=%s", scope)
	return relativeUrl, nil
}
