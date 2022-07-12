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
	_env           = "STG"
	_country       = "VN"
	_partnerID     = "partner-id"
	_partnerSecret = "partner-secret"
	_merchantID    = "merchant-id"
	terminalId     = "terminal-id"
)

func NewOfflineTestServer(url string) OfflineTransaction {
	_config := &config.Config{}
	_config.Init(_env, _country, _partnerID, _partnerSecret, _merchantID, terminalId, "", "", "")
	return &merchantIntegrationOffline{
		domain: url,
		config: _config,
	}
}

func TestPosCreateQRCode(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosCreateQRCodeParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosCreateQRCodeParams{
				PartnerTxID: "a123",
				Currency:    "VND",
				Amount:      int64(2000),
				MsgID:       "some-msgID-here",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
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
			params: &dto.PosCreateQRCodeParams{
				Currency: "VND",
				Amount:   int64(2000),
				MsgID:    "some-msgID-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosCreateQRCodeParams{
				PartnerTxID: "a123",
				Amount:      int64(2000),
				MsgID:       "some-msgID-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosCreateQRCodeParams{
				PartnerTxID: "a123",
				Amount:      int64(2000),
				Currency:    "VND",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosCreateQRCode(context.TODO(), test.params)

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

func TestPosPerformQRCode(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosPerformQRCodeParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosPerformQRCodeParams{
				PartnerTxID: "a123",
				Currency:    "VND",
				Amount:      int64(2000),
				MsgID:       "some-msgID-here",
				Code:        "x-code-y-z",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
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
			params: &dto.PosPerformQRCodeParams{
				Currency: "VND",
				Amount:   int64(2000),
				MsgID:    "some-msgID-here",
				Code:     "x-code-y-z",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosPerformQRCodeParams{
				PartnerTxID: "a123",
				Amount:      int64(2000),
				MsgID:       "some-msgID-here",
				Code:        "x-code-y-z",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosPerformQRCodeParams{
				PartnerTxID: "a123",
				Amount:      int64(2000),
				Currency:    "VND",
				Code:        "x-code-y-z",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Code",
			payload: "",
			params: &dto.PosPerformQRCodeParams{
				PartnerTxID: "a123",
				Amount:      int64(2000),
				Currency:    "VND",
				MsgID:       "some-msgID-here",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPost, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosPerformQRCode(context.TODO(), test.params)

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

func TestPosCancel(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosCancelParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosCancelParams{
				PartnerTxID:     "a123",
				Currency:        "VND",
				MsgID:           "some-msgID-here",
				OrigTxID:        "new-orig-Tx-ID-here",
				OrigPartnerTxID: "orig-a-124",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
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
			params: &dto.PosCancelParams{
				Currency:        "VND",
				MsgID:           "some-msgID-here",
				OrigTxID:        "new-orig-Tx-ID-here",
				OrigPartnerTxID: "orig-a-124",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosCancelParams{
				PartnerTxID:     "a123",
				MsgID:           "some-msgID-here",
				OrigTxID:        "new-orig-Tx-ID-here",
				OrigPartnerTxID: "orig-a-124",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosCancelParams{
				PartnerTxID:     "a123",
				Currency:        "VND",
				OrigTxID:        "new-orig-Tx-ID-here",
				OrigPartnerTxID: "orig-a-124",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - origTxID",
			payload: "",
			params: &dto.PosCancelParams{
				PartnerTxID:     "a123",
				Currency:        "VND",
				MsgID:           "some-msgID-here",
				OrigPartnerTxID: "orig-a-124",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - origPartnerTxID",
			payload: "",
			params: &dto.PosCancelParams{
				PartnerTxID: "a123",
				Currency:    "VND",
				MsgID:       "some-msgID-here",
				OrigTxID:    "new-orig-Tx-ID-here",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPut, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosCancel(context.TODO(), test.params)

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

func TestPosRefund(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosRefundParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosRefundParams{
				RefundPartnerTxID: "a123",
				Currency:          "VND",
				Amount:            int64(2000),
				MsgID:             "some-msgID-here",
				OrigPartnerTxID:   "some-orig-Partner-here",
				Description:       "test-pos-refund",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
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
			params: &dto.PosRefundParams{
				Currency:        "VND",
				Amount:          int64(2000),
				MsgID:           "some-msgID-here",
				OrigPartnerTxID: "some-orig-Partner-here",
				Description:     "test-pos-refund",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosRefundParams{
				RefundPartnerTxID: "a123",
				Amount:            int64(2000),
				MsgID:             "some-msgID-here",
				OrigPartnerTxID:   "some-orig-Partner-here",
				Description:       "test-pos-refund",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosRefundParams{
				RefundPartnerTxID: "a123",
				Currency:          "VND",
				Amount:            int64(2000),
				OrigPartnerTxID:   "some-orig-Partner-here",
				Description:       "test-pos-refund",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - origPartnerTxID",
			payload: "",
			params: &dto.PosRefundParams{
				RefundPartnerTxID: "a123",
				Currency:          "VND",
				Amount:            int64(2000),
				MsgID:             "new message abyz",
				Description:       "test-pos-refund",
			},
			expErr: ErrInvalidParams,
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			ts := httptest.NewServer(http.HandlerFunc(func(rw http.ResponseWriter, r *http.Request) {
				assert.Equal(t, http.MethodPut, r.Method)
				assert.True(t, len(r.Header.Get("Authorization")) >= test.authHeaderLenMin)

				rw.WriteHeader(http.StatusOK)
				_, _ = rw.Write([]byte(test.payload))
			}))
			defer ts.Close()

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosRefund(context.TODO(), test.params)

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

func TestPosGetTxnDetails(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosGetTxnDetailsParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosGetTxnDetailsParams{
				Currency:    "VND",
				MsgID:       "some-msgID-here",
				PartnerTxID: "some-Partner-TX-id-here",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosGetTxnDetailsParams{
				MsgID:       "some-msgID-here",
				PartnerTxID: "some-Partner-TX-id-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosGetTxnDetailsParams{
				Currency:    "VND",
				PartnerTxID: "some-Partner-TX-id-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - partnerTxID",
			payload: "",
			params: &dto.PosGetTxnDetailsParams{
				Currency: "VND",
				MsgID:    "some-msgID-here",
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

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosGetTxnDetails(context.TODO(), test.params)

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

func TestPosGetRefundDetails(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *dto.PosGetRefundDetailsParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &dto.PosGetRefundDetailsParams{
				Currency:          "VND",
				MsgID:             "some-msgID-here",
				RefundPartnerTxID: "some-Partner-TX-id-here",
			},
			expErr:           nil,
			authHeaderLenMin: len(_partnerID + ":"),
		},
		{
			desc:    "invalid params - nil",
			payload: "",
			params:  nil,
			expErr:  ErrInvalidParams,
		},
		{
			desc:    "invalid params - Currency",
			payload: "",
			params: &dto.PosGetRefundDetailsParams{
				MsgID:             "some-msgID-here",
				RefundPartnerTxID: "some-Partner-TX-id-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - msgID",
			payload: "",
			params: &dto.PosGetRefundDetailsParams{
				Currency:          "VND",
				RefundPartnerTxID: "some-Partner-TX-id-here",
			},
			expErr: ErrInvalidParams,
		},
		{
			desc:    "invalid params - partnerTxID",
			payload: "",
			params: &dto.PosGetRefundDetailsParams{
				Currency: "VND",
				MsgID:    "some-msgID-here",
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

			offlineMex := NewOfflineTestServer(ts.URL)
			resp, err := offlineMex.PosGetRefundDetails(context.TODO(), test.params)

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
