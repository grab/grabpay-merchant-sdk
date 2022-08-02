package merchant

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"time"

	dtoPOS "github.com/grab/grabpay-merchant-sdk/dto/v3"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

var (
	contentTypeJson = "application/json"
	contentTypeForm = "application/x-www-form-urlencoded"
)

func (m *merchantIntegrationOfflineV3) POSInitiate(ctx context.Context, params *dtoPOS.POSInitQRPaymentParams) (*http.Response, error) {
	path := m.config.Path.POSInit

	// verify params
	if params == nil {
		return nil, ErrInvalidParams
	}

	// prepare request body
	msgID := utils.GenerateMsgID("")
	reqBody := params.ConvertToPOSInitQRPaymentRequest(msgID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentTypeJson, reqBody.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPost, contentTypeJson, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPost, path, bytes.NewBuffer(bytesBody))
}
func (m *merchantIntegrationOfflineV3) POSInquire(ctx context.Context, params *dtoPOS.POSInquireQRPaymentParams) (*http.Response, error) {
	// verify params
	if params == nil {
		return nil, ErrInvalidParams
	}

	msgID := utils.GenerateMsgID("")
	path, err := m.config.Path.GetPOSInquire(msgID, params)
	if err != nil {
		return nil, err
	}

	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentTypeForm, msgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodGet, contentTypeForm, path, nil, now)

	return makeRequest(m.domain, headers, http.MethodGet, path, nil)
}
func (m *merchantIntegrationOfflineV3) POSCancel(ctx context.Context, params *dtoPOS.POSCancelQRPaymentParams) (*http.Response, error) {
	path := m.config.Path.POSCancel

	// verify params
	if params == nil {
		return nil, ErrInvalidParams
	}

	// prepare request body
	msgID := utils.GenerateMsgID("")
	reqBody := params.ConvertToPOSCancelQRPaymentRequest(msgID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentTypeJson, reqBody.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPut, contentTypeJson, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPut, path, bytes.NewBuffer(bytesBody))
}
func (m *merchantIntegrationOfflineV3) POSRefund(ctx context.Context, params *dtoPOS.POSRefundQRPaymentParams) (*http.Response, error) {
	path := m.config.Path.POSRefund

	// verify params
	if params == nil {
		return nil, ErrInvalidParams
	}

	// prepare request body
	msgID := utils.GenerateMsgID("")
	reqBody := params.ConvertToPOSRefundQRPaymentRequest(msgID)
	bytesBody, err := json.Marshal(reqBody)
	if err != nil {
		return nil, ErrFailedMarshalBody
	}
	// prepare headers
	now := time.Now().UTC()
	headers := utils.PrepareCommonHeaders(m.config, contentTypeJson, reqBody.MsgID, now)
	headers = utils.AppendHmacToHeaders(m.config, headers, http.MethodPut, contentTypeJson, path, bytesBody, now)

	return makeRequest(m.domain, headers, http.MethodPut, path, bytes.NewBuffer(bytesBody))
}
