package integration

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"testing"

	"github.com/grab/grabpay-merchant-sdk/dto"
	"github.com/grab/grabpay-merchant-sdk/merchant"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

var (
	env           = "STG"
	country       = "VN"
	partnerID     = os.Getenv("VN_STG_OTC_PARTNER_ID")
	partnerSecret = os.Getenv("VN_STG_OTC_PARTNER_SECRET")
	merchantID    = os.Getenv("VN_STG_OTC_MERCHANT_ID")
	clientID      = os.Getenv("VN_STG_OTC_PARTNER_ID")
	clientSecret  = os.Getenv("VN_STG_OTC_PARTNER_SECRET")
	redirectUri   = "http://localhost:8888/result"
)

func TestOnaChargeInit(t *testing.T) {
	t.Parallel()

	merchant := merchant.NewMerchantOnline(env, country, partnerID, partnerSecret, merchantID, clientID, clientSecret, redirectUri)
	params := new(dto.OnaChargeInitParams)
	params.PartnerTxID = utils.GenerateRandomString(32)
	params.PartnerGroupTxID = utils.GenerateRandomString(32)
	params.Amount = int64(2000)
	params.Description = "testing"
	params.Currency = "VND"

	resp, _ := merchant.OnaChargeInit(nil, params)
	fmt.Println(resp)

}

func TestOnaWebInit(t *testing.T) {
	t.Parallel()

	merchant := merchant.NewMerchantOnline(env, country, partnerID, partnerSecret, merchantID, clientID, clientSecret, redirectUri)
	params := new(dto.OnaCreateWebUrlParams)
	params.PartnerTxID = utils.GenerateRandomString(32)
	params.PartnerGroupTxID = utils.GenerateRandomString(32)
	params.Amount = int64(2000)
	params.Description = "testing"
	params.Currency = "VND"

	// Web init
	response, state, _ := merchant.OnaCreateWebUrl(nil, params)
	fmt.Println(response)
	fmt.Println(state)
	// fmt.Println("Enter response code after payment ")
	// var code string
	// code = ""
	// fmt.Scanln(&code)
	//
	// // API OnA OAuth
	// oauthParams := &dto.OnaOAuth2TokenParams{
	// 	PartnerTxID: params.PartnerTxID,
	// 	Code:        code,
	// }
	// respAuthCode, err := merchant.OnaOAuth2Token(nil, oauthParams)
	// if respAuthCode.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA OAuth")
	//
	// access_token := getData(processResponse(respAuthCode, err))["access_token"]
	//
	// // API OnA charge complete
	// chargeCompleteParams := &dto.OnaChargeCompleteParams{
	// 	PartnerTxID: params.PartnerTxID,
	// 	AccessToken: access_token,
	// }
	// respChargeComplete, err := merchant.OnaChargeComplete(nil, chargeCompleteParams)
	// if respChargeComplete.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA charge complete")
	//
	// // API OnA charge complete status
	// chargeCompleteStatusParams := &dto.OnaGetChargeStatusParams{
	// 	PartnerTxID: params.PartnerTxID,
	// 	AccessToken: access_token,
	// 	Currency:    params.Currency,
	// }
	// getChargeStatus, err := merchant.OnaGetChargeStatus(nil, chargeCompleteStatusParams)
	// if getChargeStatus.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA charge complete status")
	//
	// // API OnA refund
	// originTxID := getData(processResponse(getChargeStatus, err))["txID"]
	// fmt.Println("Enter refund amount")
	// var refundAmount int64
	// fmt.Scanln(&refundAmount)
	// refundParams := &dto.OnaRefundParams{
	// 	RefundPartnerTxID: params.PartnerTxID,
	// 	PartnerGroupTxID:  params.PartnerGroupTxID,
	// 	Amount:            refundAmount,
	// 	Currency:          params.Currency,
	// 	TxID:              originTxID,
	// 	Description:       "refund haha",
	// 	AccessToken:       access_token,
	// }
	// respRefund, err := merchant.OnaRefund(nil, refundParams)
	// if respRefund.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA Refund")
	//
	// // API OnA refund status
	// refundStatusParams := &dto.OnaGetRefundStatusParams{
	// 	RefundPartnerTxID: refundParams.RefundPartnerTxID,
	// 	Currency:          refundParams.Currency,
	// 	AccessToken:       access_token,
	// }
	// respRefundStatus, err := merchant.OnaGetRefundStatus(nil, refundStatusParams)
	// if respRefundStatus.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA Refund Status")
	//
	// // API OnA OTC status
	// OTCStatusParams := &dto.OnaGetOTCStatusParams{
	// 	PartnerTxID: params.PartnerTxID,
	// 	Currency:    params.Currency,
	// }
	// respOTCStatus, err := merchant.OnaGetOTCStatus(nil, OTCStatusParams)
	// if respOTCStatus.StatusCode >= 400 {
	// 	panic(err)
	// }
	// fmt.Println("DONE API OnA OTC Status")

}

func processResponse(resp *http.Response, err error) merchant.Response {
	if err != nil {
		return merchant.Response{
			Code: -1,
			Data: "Error during request data",
		}
	}
	// defer resp.Body.Close()
	bodyBytes, err := io.ReadAll(resp.Body)

	return merchant.Response{
		Code: resp.StatusCode,
		Data: string(bodyBytes),
	}
}

func getData(response merchant.Response) map[string]string {
	data := make(map[string]string)
	dataString := fmt.Sprintf("%v", response.Data)

	err := json.Unmarshal([]byte(dataString), &data)
	if err != nil {
		panic(err)
	}
	return data
}
