package merchant

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/grab/grabpay-merchant-sdk/dto"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

// PosCreateQRCode function is used to create a new payment order for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosCreateQRCode(ctx context.Context, params *dto.PosCreateQRCodeParams) (*http.Response, error) {
	path := m.config.Path.PosCreateQRCode

	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.MsgID) == 0 || len(params.Currency) == 0 {
		return nil, ErrInvalidParams
	}
	// prepare request body
	reqBody := params.ConvertToPosCreateQRCodeRequest(m.config.MerchantID, m.config.TerminalID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPost, contentType, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
}

// PosPerformQRCode function is used to perform a payment transaction based on the consumer presented QR code for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosPerformQRCode(ctx context.Context, params *dto.PosPerformQRCodeParams) (*http.Response, error) {
	path := m.config.Path.PosPerformQRCode
	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.MsgID) == 0 || len(params.Currency) == 0 || len(params.Code) == 0 {
		return nil, ErrInvalidParams
	}
	// prepare request body
	reqBody := params.ConvertToPosPerformQRCodeRequest(m.config.MerchantID, m.config.TerminalID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPost, contentType, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
}

// PosCancel function is used to cancel a pending payment for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosCancel(ctx context.Context, params *dto.PosCancelParams) (*http.Response, error) {
	path := m.config.Path.PosCancel

	// verify params
	if params == nil || len(params.PartnerTxID) == 0 || len(params.MsgID) == 0 || len(params.Currency) == 0 || len(params.OrigPartnerTxID) == 0 || len(params.OrigTxID) == 0 {
		return nil, ErrInvalidParams
	}
	// prepare request body
	reqBody := params.ConvertToPosCancelRequest(m.config.MerchantID, m.config.TerminalID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPut, contentType, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPut, path, bytes.NewBuffer(bytesBody))
}

// PosRefund function is used to refund a previously successful payment for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosRefund(ctx context.Context, params *dto.PosRefundParams) (*http.Response, error) {
	path := m.config.Path.PosRefund

	// verify params
	if params == nil || len(params.RefundPartnerTxID) == 0 || len(params.MsgID) == 0 || len(params.Currency) == 0 || len(params.OrigPartnerTxID) == 0 || len(params.Description) == 0 {
		return nil, ErrInvalidParams
	}
	// prepare request body
	reqBody := params.ConvertToPosRefundRequest(m.config.MerchantID, m.config.TerminalID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPut, contentType, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPut, path, bytes.NewBuffer(bytesBody))
}

// PosGetTxnDetails function is used to get details of the payment transaction for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosGetTxnDetails(ctx context.Context, params *dto.PosGetTxnDetailsParams) (*http.Response, error) {
	// verify params
	if params == nil || len(params.MsgID) == 0 || len(params.Currency) == 0 || len(params.PartnerTxID) == 0 {
		return nil, ErrInvalidParams
	}

	path := m.config.Path.GetPosTxnDetails(params.MsgID, m.config.MerchantID, m.config.TerminalID, params.Currency, "P2M", params.PartnerTxID, params.AdditionalInfo)

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodGet, contentType, path, nil, now)

	return makeRequest(m.domain, headers, http.MethodGet, path, nil)
}

// PosGetRefundDetails function is used to get details of the refund transaction for POS transaction
// It returns http.Response, body of which is the same in dto.offline_response.go file
func (m *merchantIntegrationOffline) PosGetRefundDetails(ctx context.Context, params *dto.PosGetRefundDetailsParams) (*http.Response, error) {
	// verify params
	if params == nil || len(params.MsgID) == 0 || len(params.Currency) == 0 || len(params.RefundPartnerTxID) == 0 {
		return nil, ErrInvalidParams
	}
	path := m.config.Path.GetPosTxnDetails(params.MsgID, m.config.MerchantID, m.config.TerminalID, params.Currency, "Refund", params.RefundPartnerTxID, params.AdditionalInfo)

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentType, params.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodGet, contentType, path, nil, now)

	return makeRequest(m.domain, headers, http.MethodGet, path, nil)
}

func (m *merchantIntegrationOffline) getFullPath(path string) string {
	return m.config.Domain + path
}
