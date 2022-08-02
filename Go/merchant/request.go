package merchant

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"net/http"

	"github.com/grab/grabpay-merchant-sdk/dto"
)

// Response provide struct for shortened response body
type Response struct {
	Code int         `json:"code"`
	Data interface{} `json:"data"`
}

//func processResponse(resp *http.Response, err error, dto interface{}) Response {
//
//	if err != nil {
//		return Response{
//			Code: -1,
//			Data: err.Error(),
//		}
//	}
//	defer resp.Body.Close()
//	bodyBytes, _ := io.ReadAll(resp.Body)
//
//	err = json.Unmarshal(bodyBytes, dto)
//	if err != nil {
//		return Response{
//			Code: -1,
//			Data: err.Error(),
//		}
//	}
//
//	return Response{
//		Code: resp.StatusCode,
//		Data: dto,
//	}
//}

func ProcessResponse(resp *http.Response, _dto interface{}) error {
	bodyBytes, readErr := io.ReadAll(resp.Body)
	if readErr != nil {
		log.Println("[processResponse] Error when read response body.", readErr)
	}
	defer resp.Body.Close()

	statusCode := resp.StatusCode
	if statusCode >= 300 {
		return errors.New(fmt.Sprintf("Httpcode : %d,\nDescription: %s", resp.StatusCode, string(bodyBytes)))
	}

	return json.Unmarshal(bodyBytes, _dto)
}

// DecodeResponse receives a *http.Response pointer and an object pointer of a target struct
func DecodeResponse(ctx context.Context, response *http.Response, dto interface{}) (interface{}, error) {
	defer func() {
		if cErr := response.Body.Close(); cErr != nil {
			log.Println("[decodeResponse] Error when closing response body.", cErr)
		}
	}()

	body, rErr := ioutil.ReadAll(response.Body)
	if rErr != nil {
		log.Println("[decodeResponse] Failed to parse response body.", rErr)
		return nil, rErr
	}

	if jErr := json.Unmarshal(body, dto); jErr != nil {
		log.Println(fmt.Sprintf("[decodeResponse] Failed to decode response %s. Error: %s", string(body), jErr))
		return nil, jErr
	}
	return dto, nil
}

// makeRequest function is used to call request with given domain, header, url, body
func makeRequest(domain string, headers http.Header, httpMethod, path string, body io.Reader) (*http.Response, error) {
	// new request
	req, err := http.NewRequest(httpMethod, getFullPath(domain, path), body)

	if err != nil {
		return nil, ErrFailedCreateRequest
	}
	req.Header = headers

	// send request
	client := &http.Client{}
	return client.Do(req)
}

func getFullPath(domain string, path string) string {
	return domain + path
}

func getRequestFromResponse(resp *http.Response) (string, error) {
	//processedResp := processResponse(resp, nil, dto)
	dnaChargeInitResponse := &dto.OnaChargeInitResponse{}
	err := ProcessResponse(resp, dnaChargeInitResponse)
	if err != nil {
		return "", err
	}

	return dnaChargeInitResponse.Request, nil

	//
	//data := make(map[string]string)
	//dataString := fmt.Sprintf("%v", processedResp.Data)
	//fmt.Println(dataString)
	//err := json.Unmarshal([]byte(dataString), &data)
	//if err != nil {
	//	return "", err
	//}
	//request := data["request"]
	//
	//return request, nil
}
