package merchant

import (
	"context"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/grab/grabpay-merchant-sdk/config"
	"github.com/grab/grabpay-merchant-sdk/dto"

	"github.com/stretchr/testify/assert"
)

const (
	env           = "STG"
	country       = "VN"
	partnerID     = "partner-id"
	partnerSecret = "partner-secret"
	merchantID    = "merchant-id"
	clientID      = "client-id"
	clientSecret  = "client-secret"
	redirectUri   = "http://localhost:8888/result"
)

func NewOnlineTestServer(url string) OnlineTransaction {
	_config := &config.Config{}
	_config.Init(env, country, partnerID, partnerSecret, merchantID, "", clientID, clientSecret, redirectUri)
	return &merchantIntegrationOnline{
		domain: url,
		config: _config,
	}
}

func NewOnlineTestServer2(url, country string) OnlineTransaction {
	_config := &config.Config{}
	_config.Init(env, country, partnerID, partnerSecret, merchantID, "", clientID, clientSecret, redirectUri)
	return &merchantIntegrationOnline{
		domain: url,
		config: _config,
	}
}

func TestOnaChargeInit(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaChargeInitParams
		expErr           error
		authHeaderLenMin int
		status           int
	}{
		{
			desc:    "init",
			payload: "{\"partnerTxID\": \"bbe234bf4db94a35a5cf515358187dd7\",\n    \"request\": \"eyJhbGciOiAibm9uZSJ9.eyJjbGFpbXMiOnsidHJhbnNhY3Rpb24iOnsidHhJRCI6ImM3ODk2ZTJlNGVmZTQ4NGFiNjNhZmM2Y2Y5ZTljMDEyIn19fQ.\"}",
			params: &dto.OnaChargeInitParams{
				PartnerTxID: "a123",
				Currency:    "VND",
			},
			expErr:           nil,
			authHeaderLenMin: len(partnerID + ":"),
			status:           200,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
			status:  400,
		},
		{
			desc:    "invalid params - PartnerTxID",
			payload: "",
			params: &dto.OnaChargeInitParams{
				Currency: "VND",
			},
			expErr: ErrInvalidParams,
			status: 400,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.OnaChargeInitParams{
				PartnerTxID: "a123",
			},
			expErr: ErrInvalidParams,
			status: 400,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(test.status)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaChargeInit(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaChargeComplete(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaChargeCompleteParams
		expErr           error
		authHeaderLenMin int
		xgidHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "{\"msgID\":\"\",\"txID\": \"c7896e2e4efe484ab63afc6cf9e9c012\",\"status\": \"success\",\"description\": \"\",\"paymentMethod\": \"GPWALLET\",\"txStatus\": \"success\",\"reason\": \"\"}",
			params: &dto.OnaChargeCompleteParams{
				PartnerTxID: "a123",
				AccessToken: "actoken1",
			},
			expErr:           nil,
			authHeaderLenMin: len("Bearer actoken1"),
			xgidHeaderLenMin: 0,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - PartnerTxID",
			payload: "",
			params: &dto.OnaChargeCompleteParams{
				AccessToken: "actk",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - AccessToken",
			payload: "",
			params: &dto.OnaChargeCompleteParams{
				PartnerTxID: "a123",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)
				assert.True(t, len(r.Header.Get("X-GID-AUX-POP")) >= test.xgidHeaderLenMin)
				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaChargeComplete(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}
		})
	}
}

func TestOnaOAuth2Token(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc    string
		payload string
		params  *dto.OnaOAuth2TokenParams
		expErr  error
	}{
		{
			desc:    "init",
			payload: "{\"access_token\": \"oiSldUIn0.ejBiZTg0NzMDY0YWI2M2FmYzZjZjllOWMwMTIiLCJpc3MiOiJodHRwcz9.\",\"token_type\": \"Bearer\",\"expires_in\": 31535999}",
			params: &dto.OnaOAuth2TokenParams{
				CodeVerifier: "a123",
				Code:         "actoken1",
			},
			expErr: nil,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - PartnerTxID",
			payload: "",
			params: &dto.OnaOAuth2TokenParams{
				Code: "actk",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Code",
			payload: "",
			params: &dto.OnaOAuth2TokenParams{
				CodeVerifier: "a123",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaOAuth2Token(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				//assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaGetChargeStatus(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaGetChargeStatusParams
		expErr           error
		authHeaderLenMin int
		xgidHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "{\"txID\": \"c7896e2e4efe484ab63afc6cf9e9c012\",\"oAuthCode\": \"\",\"status\": \"success\",\"paymentMethod\": \"GPWALLET\",\"additionalInfo\": {\"amountBreakdown\": {\"grossAmount\": 2600,\"paidAmount\": 2000,\"mocaPromoAmount\": 600}},\"txStatus\": \"success\",\"reason\": \"\"}",
			params: &dto.OnaGetChargeStatusParams{
				PartnerTxID: "a123",
				AccessToken: "actoken1",
				Currency:    "VND",
			},
			expErr:           nil,
			authHeaderLenMin: len("Bearer actoken1"),
			xgidHeaderLenMin: 0,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - PartnerTxID",
			payload: "",
			params: &dto.OnaGetChargeStatusParams{
				AccessToken: "actk",
				Currency:    "VND",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - AccessToken",
			payload: "",
			params: &dto.OnaGetChargeStatusParams{
				PartnerTxID: "a123",
				Currency:    "VND",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.OnaGetChargeStatusParams{
				PartnerTxID: "a123",
				AccessToken: "actk",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodGet, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)
				assert.True(t, len(r.Header.Get("X-GID-AUX-POP")) >= test.xgidHeaderLenMin)
				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaGetChargeStatus(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				//assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaRefund(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaRefundParams
		expErr           error
		authHeaderLenMin int
		xgidHeaderLenMin int
	}{
		{
			desc:    "success - all params must be available",
			payload: "{\"txID\" : \"1kjlhkasjdhf\",\"status\":\"success\",\"description\" : \"no description\",\"txStatus\" : \"refund\",\"reason\" : \"refund\",\"msgID\": \"alskdhjfopi\"}",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				PartnerGroupTxID:  "pg123",
				Amount:            12,
				Currency:          "VND",
				TxID:              "tx123",
				Description:       "desc123",
				AccessToken:       "actoken1",
			},
			expErr:           nil,
			authHeaderLenMin: len("Bearer actoken1"),
			xgidHeaderLenMin: 0,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - RefundPartnerTxID",
			payload: "",
			params: &dto.OnaRefundParams{
				PartnerGroupTxID: "pg123",
				Amount:           12,
				Currency:         "VND",
				TxID:             "tx123",
				Description:      "desc123",
				AccessToken:      "actoken1",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - PartnerGroupTxID",
			payload: "",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				Amount:            12,
				Currency:          "VND",
				TxID:              "tx123",
				Description:       "desc123",
				AccessToken:       "actoken1",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				PartnerGroupTxID:  "pg123",
				Amount:            12,
				TxID:              "tx123",
				Description:       "desc123",
				AccessToken:       "actoken1",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - TxID",
			payload: "",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				PartnerGroupTxID:  "pg123",
				Amount:            12,
				Currency:          "VND",
				Description:       "desc123",
				AccessToken:       "actoken1",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Description",
			payload: "",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				PartnerGroupTxID:  "pg123",
				Amount:            12,
				Currency:          "VND",
				TxID:              "tx123",
				AccessToken:       "actoken1",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - AccessToken",
			payload: "",
			params: &dto.OnaRefundParams{
				RefundPartnerTxID: "a123",
				PartnerGroupTxID:  "pg123",
				Amount:            12,
				Currency:          "VND",
				TxID:              "tx123",
				Description:       "desc123",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)
				assert.True(t, len(r.Header.Get("X-GID-AUX-POP")) >= test.xgidHeaderLenMin)
				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaRefund(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaGetRefundStatus(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaGetRefundStatusParams
		expErr           error
		authHeaderLenMin int
		xgidHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "{\"txID\" : \"1kjlhkasjdhf\",\"status\":\"success\",\"description\" : \"no description\",\"txStatus\" : \"refund\",\"reason\" : \"refund\",\"msgID\": \"alskdhjfopi\"}",
			params: &dto.OnaGetRefundStatusParams{
				RefundPartnerTxID: "a123",
				AccessToken:       "actoken1",
				Currency:          "VND",
			},
			expErr:           nil,
			authHeaderLenMin: len("Bearer actoken1"),
			xgidHeaderLenMin: 0,
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - RefundPartnerTxID",
			payload: "",
			params: &dto.OnaGetRefundStatusParams{
				AccessToken: "actk",
				Currency:    "VND",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - AccessToken",
			payload: "",
			params: &dto.OnaGetRefundStatusParams{
				RefundPartnerTxID: "a123",
				Currency:          "VND",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.OnaGetRefundStatusParams{
				RefundPartnerTxID: "a123",
				AccessToken:       "actk",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodGet, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)
				assert.True(t, len(r.Header.Get("X-GID-AUX-POP")) >= test.xgidHeaderLenMin)
				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaGetRefundStatus(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaGetOTCStatus(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaGetOTCStatusParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "{\"txID\": \"c7896e2e4efe484ab63afc6cf9e9c012\",\"oAuthCode\": \"\",\"status\": \"success\",\"paymentMethod\": \"GPWALLET\",\"additionalInfo\": {\"amountBreakdown\": {\"grossAmount\": 2600,\"paidAmount\": 2000,\"mocaPromoAmount\": 600}},\"txStatus\": \"success\",\"reason\": \"\"}",
			params: &dto.OnaGetOTCStatusParams{
				PartnerTxID: "a123",
				Currency:    "VND",
			},
			expErr:           nil,
			authHeaderLenMin: len(partnerID + ":"),
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - PartnerTxID",
			payload: "",
			params: &dto.OnaGetOTCStatusParams{
				Currency: "VND",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.OnaGetOTCStatusParams{
				PartnerTxID: "a123",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodGet, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, err := onlineMex.OnaGetOTCStatus(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotNil(t, resp)
				assert.Nil(t, err)
			} else {
				assert.Nil(t, resp)
				assert.Equal(t, test.expErr, err)
			}

		})
	}
}

func TestOnaCreateWebUrl(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.OnaCreateWebUrlParams
		expErr           error
		authHeaderLenMin int
		statusCode       int
	}{
		{
			desc:    "init",
			payload: "{\"request\": \"eyJhbGciOiAibm9uZSJ9.eyJjbGFpbXMiOnsidHJhbnNhY3Rpb24iOnsidHhJRCI6IjA1YjVmNDAxZjM4NDQyN2ZiZmU0YTkxNTkyMDBjMTNlIn19fQ.\"}",
			params: &dto.OnaCreateWebUrlParams{
				OnaChargeInitParams: dto.OnaChargeInitParams{
					PartnerTxID:      "some-partner-id-here",
					PartnerGroupTxID: "some-partner-id-here",
					Amount:           int64(2000),
					Currency:         "VND",
					Description:      "test webinit",
				},
				State: "1234567",
			},
			expErr:           nil,
			statusCode:       200,
			authHeaderLenMin: len(partnerID + ":"),
		},
		{
			desc:       "invalid params - nil",
			payload:    "",
			params:     nil,
			expErr:     ErrInvalidParams,
			statusCode: 200,
		},
		{
			desc:    "invalid response - no request value",
			payload: "",
			params: &dto.OnaCreateWebUrlParams{
				OnaChargeInitParams: dto.OnaChargeInitParams{
					PartnerTxID:      "some-partner-id-here",
					PartnerGroupTxID: "some-partner-id-here",
					Amount:           int64(2000),
					Currency:         "VND",
					Description:      "test webinit",
				},
				State: "1234567",
			},
			expErr:     ErrFailedGetRequestValue,
			statusCode: 400,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				// rw.WriteHeader(http.StatusOK)
				rw.WriteHeader(test.statusCode)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			onlineMex := NewOnlineTestServer(ts.URL)
			resp, state, err := onlineMex.OnaCreateWebUrl(context.TODO(), test.params)

			if test.expErr == nil {
				assert.NotEmpty(t, resp)
				assert.NotEmpty(t, state)
				assert.Equal(t, test.expErr, err)
			} else if test.expErr == ErrInvalidParams {
				assert.Empty(t, resp)
				assert.Empty(t, state)
				assert.Equal(t, test.expErr, err)
			} else {
				assert.Empty(t, resp)
				assert.NotEmpty(t, state)
				assert.Contains(t, []error{ErrInvalidParams, ErrFailedCreateRequest, ErrFailedGetRequestValue}, err)
			}
		})
	}
}

func TestBuildHttpUrlWithQuery(t *testing.T) {
	t.Parallel()
	type params struct {
		partnerTxID, currency, state, request string
	}

	type expected struct {
		codeChallenge       string
		acrValues           string
		codeChallengeMethod string
		responseType        string
		path                string
		scope               string
	}
	tests := []struct {
		desc           string
		paramsInput    params
		expectedOutput expected
		country        string
	}{
		{
			desc:    "first one",
			country: "VN",
			paramsInput: params{
				partnerTxID: "x-partner-TX-id",
				currency:    "country-currency",
				state:       "new-state",
				request:     "request for weburl",
			},
			expectedOutput: expected{
				codeChallenge:       "FRIlhvRI3TLfCuLmmdeAonf_I-IjyzHwbIQLVsrfgms",
				acrValues:           "consent_ctx:countryCode=VN,currency=country-currency",
				codeChallengeMethod: "S256",
				responseType:        "code",
				path:                "/grabid/v1/oauth2/authorize",
				scope:               "scope=payment.vn.one_time_charge",
			},
		},
		{
			desc:    "empty state",
			country: "VN",
			paramsInput: params{
				partnerTxID: "x-partner-TX-id",
				currency:    "country-currency",
				state:       "",
				request:     "request for weburl",
			},
			expectedOutput: expected{
				codeChallenge:       "FRIlhvRI3TLfCuLmmdeAonf_I-IjyzHwbIQLVsrfgms",
				acrValues:           "consent_ctx:countryCode=VN,currency=country-currency",
				codeChallengeMethod: "S256",
				responseType:        "code",
				path:                "/grabid/v1/oauth2/authorize",
				scope:               "scope=payment.vn.one_time_charge",
			},
		},
		{
			desc:    "change scope",
			country: "MY",
			paramsInput: params{
				partnerTxID: "x-partner-TX-id",
				currency:    "country-currency",
				state:       "new-state",
				request:     "request for weburl",
			},
			expectedOutput: expected{
				codeChallenge:       "FRIlhvRI3TLfCuLmmdeAonf_I-IjyzHwbIQLVsrfgms",
				acrValues:           "consent_ctx:countryCode=MY,currency=country-currency",
				codeChallengeMethod: "S256",
				responseType:        "code",
				path:                "/grabid/v1/oauth2/authorize",
				scope:               "scope=openid+payment.one_time_charge",
			},
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			onlineMex := NewOnlineTestServer2("http://example-domain.test", test.country)

			url, err := onlineMex.buildHttpUrlWithQuery(test.paramsInput.partnerTxID, test.paramsInput.currency, test.paramsInput.state, test.paramsInput.request)
			assert.Equal(t, nil, err, "Err should be nil")
			assert.Equal(t, test.expectedOutput.responseType, url.Query().Get("response_type"), "actual and expected params value should be the same")
			assert.Equal(t, test.expectedOutput.acrValues, url.Query().Get("acr_values"), "actual and expected params value should be the same")
			assert.Equal(t, test.expectedOutput.codeChallengeMethod, url.Query().Get("code_challenge_method"), "actual and expected params value should be the same")
			assert.Contains(t, url.String(), test.expectedOutput.scope, "actual and expected params value should be the same")
			assert.Equal(t, url.Path, test.expectedOutput.path, "actual and expected params value should be the same")
		})
	}
}
