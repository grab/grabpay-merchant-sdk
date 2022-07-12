package merchant

import (
	"bytes"
	"context"
	"errors"
	"io"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/grab/grabpay-merchant-sdk/dto"

	"github.com/stretchr/testify/assert"
)

func TestGetFullPath(t *testing.T) {
	t.Parallel()
	domain := "http://example.com"
	path := ""

	output := getFullPath(domain, path)
	expected := "http://example.com"
	assert.Equal(t, expected, output, "Expected and actual should be the same for GetFullPath")

	path = "/some/path/here"
	output = getFullPath(domain, path)
	expected = "http://example.com/some/path/here"
	assert.Equal(t, expected, output, "Expected and actual should be the same for GetFullPath")

	domain = ""
	output = getFullPath(domain, path)
	expected = "/some/path/here"
	assert.Equal(t, expected, output, "Expected and actual should be the same for GetFullPath")
}

func TestProcessResponse(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc    string
		payload string
		err     error
	}{
		{
			desc:    "test error",
			payload: "{\"request\": \"eyJhbGciOiAibm9uZSJ9.eyJjbGFpbXMiOnsidHJhbnNhY3Rpb24iOnsidHhJRCI6IjA1YjVmNDAxZjM4NDQyN2ZiZmU0YTkxNTkyMDBjMTNlIn19fQ.\"}",
			err:     errors.New("this is error"),
		},
		{
			desc:    "no error",
			payload: "{\"request\": \"eyJhbGciOiAibm9uZSJ9.eyJjbGFpbXMiOnsidHJhbnNhY3Rpb24iOnsidHhJRCI6IjA1YjVmNDAxZjM4NDQyN2ZiZmU0YTkxNTkyMDBjMTNlIn19fQ.\"}",
			err:     nil,
		},
	}
	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {
			initResp := &http.Response{}
			if test.err != nil {
				initResp.Body = ioutil.NopCloser(bytes.NewBufferString(test.payload))
				initResp.StatusCode = 200
			} else {
				initResp.Body = ioutil.NopCloser(bytes.NewBufferString(test.payload))
				initResp.StatusCode = 400
			}

			err := ProcessResponse(initResp, &dto.OnaChargeInitResponse{})
			if initResp.StatusCode == 200 {
				assert.Nil(t, err)
			} else {
				assert.NotNil(t, err)
			}
		})
	}
}

func Test_DecodeResponse(t *testing.T) {
	t.Parallel()

	tests := []struct {
		desc    string
		payload string
		dto     interface{}
		msgID   string
	}{
		{
			desc:    "normal 1",
			payload: `{"msgID":"u001"}`,
			dto:     &dto.PosCreateQRCodeResponse{},
			msgID:   "u001",
		},
		{
			desc:    "normal 2",
			payload: `{"msgID":""}`,
			dto:     &dto.PosCreateQRCodeResponse{},
			msgID:   "",
		},
		{
			desc:    "err 1",
			payload: `{"userID":""}`,
			dto:     nil,
			msgID:   "u123",
		},
	}

	for _, test := range tests {
		t.Run(test.desc, func(t *testing.T) {

			var resp *http.Response
			w := httptest.NewRecorder()

			io.WriteString(w, test.payload)
			resp = w.Result()

			_, err := DecodeResponse(context.TODO(), resp, test.dto)

			if err == nil {
				assert.Equal(t, test.msgID, test.dto.(*dto.PosCreateQRCodeResponse).MsgID)
			} else {
				assert.NotNil(t, err)
			}
		})
	}
}
