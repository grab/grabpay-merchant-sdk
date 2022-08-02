package merchant

import (
	"context"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/stretchr/testify/assert"

	"github.com/grab/grabpay-merchant-sdk/config"
	v3 "github.com/grab/grabpay-merchant-sdk/dto/v3"
)

var (
	partnerTxID      = "partner-tx-id"
	partnerGroupTxID = "partner-group-tx-id"
	amount           = int64(2000)
	currency         = "currency"
	storeID          = "store-id"
)

func NewOfflineV3TestServer(url string) OfflineTransactionV3 {
	_config := &config.Config{}
	_config.Init(_env, _country, _partnerID, _partnerSecret, _merchantID, terminalId, "", "", "")
	return &merchantIntegrationOfflineV3{
		domain: url,
		config: _config,
	}
}

func TestMerchantIntegrationOfflineV3_POSInit(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *v3.POSInitQRPaymentParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "init",
			payload: "",
			params: &v3.POSInitQRPaymentParams{
				POSDetails: &v3.POSDetailsRequest{
					TerminalID: "new tesrminal",
				},
				PaymentMethod: &v3.POSPaymentMethod{
					PaymentMethodExclusion: nil,
					MinAmtPostpaid:         50,
					MinAmt4Instalment:      50,
				},
				TransactionDetails: &v3.POSInitTransactionDetails{
					PartnerTxID:       partnerTxID,
					PartnerGroupTxID:  partnerGroupTxID,
					Amount:            amount,
					Currency:          currency,
					BillRefNumber:     "somethinghere",
					GrabTxID:          partnerTxID,
					StoreGrabID:       storeID,
					PaymentChannel:    "some-channel",
					PaymentExpiryTime: 60,
				},
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

			offlineMex := NewOfflineV3TestServer(ts.URL)
			resp, err := offlineMex.POSInitiate(context.TODO(), test.params)

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

func TestMerchantIntegrationOfflineV3_POSInquire(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *v3.POSInquireQRPaymentParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "valid",
			payload: "",
			params: &v3.POSInquireQRPaymentParams{
				TransactionDetails: &v3.POSInquireTransactionDetails{
					TxRefID:        partnerTxID,
					TxType:         partnerGroupTxID,
					TxRefType:      partnerGroupTxID,
					Currency:       currency,
					StoreGrabID:    storeID,
					PaymentChannel: "some-channel",
				},
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

			offlineMex := NewOfflineV3TestServer(ts.URL)
			resp, err := offlineMex.POSInquire(context.TODO(), test.params)

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

func TestMerchantIntegrationOfflineV3_POSCancel(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *v3.POSCancelQRPaymentParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "valid",
			payload: "",
			params: &v3.POSCancelQRPaymentParams{
				TransactionDetails: &v3.POSCancelTransactionDetails{
					OriginPartnerTxID: partnerTxID,
					Currency:          currency,
					StoreGrabID:       storeID,
					PaymentChannel:    "some-channel",
				},
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

			offlineMex := NewOfflineV3TestServer(ts.URL)
			resp, err := offlineMex.POSCancel(context.TODO(), test.params)

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

func TestMerchantIntegrationOfflineV3_POSRefund(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc             string
		payload          string
		params           *v3.POSRefundQRPaymentParams
		expErr           error
		authHeaderLenMin int
	}{
		{
			desc:    "valid",
			payload: "",
			params: &v3.POSRefundQRPaymentParams{
				TransactionDetails: &v3.POSRefundTransactionDetails{
					PartnerTxID:       partnerTxID,
					PartnerGroupTxID:  partnerGroupTxID,
					OriginPartnerTxID: partnerTxID,
					Amount:            amount,
					Currency:          currency,
					StoreGrabID:       storeID,
					PaymentChannel:    "some-channel",
					Reason:            "i-do-not-know-the-reason",
				},
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

			offlineMex := NewOfflineV3TestServer(ts.URL)
			resp, err := offlineMex.POSRefund(context.TODO(), test.params)

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
