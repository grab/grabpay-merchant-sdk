package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"time"

	"github.com/grab/grabpay-merchant-sdk/config"
	"github.com/grab/grabpay-merchant-sdk/dto"
	"github.com/grab/grabpay-merchant-sdk/merchant"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

var (
	VNEnv           = config.EnvSTG
	VNCountry       = config.CountryVN
	VNPartnerID     = os.Getenv("VN_STG_OTC_PARTNER_ID")
	VNPartnerSecret = os.Getenv("VN_STG_OTC_PARTNER_SECRET")
	VNMerchantID    = os.Getenv("VN_STG_OTC_MERCHANT_ID")
	VNClientID      = os.Getenv("VN_STG_OTC_PARTNER_ID")
	VNClientSecret  = os.Getenv("VN_STG_OTC_PARTNER_SECRET")
	VNRedirectUri   = "http://localhost:8888/result"

	GlobalEnv           = config.EnvSTG
	GlobalCountry       = config.CountryMY
	GlobalPartnerID     = os.Getenv("MY_STG_OTC_PARTNER_ID")
	GlobalPartnerSecret = os.Getenv("MY_STG_OTC_PARTNER_SECRET")
	GlobalMerchantID    = os.Getenv("MY_STG_OTC_MERCHANT_ID")
	GlobalClientID      = os.Getenv("MY_STG_OTC_PARTNER_ID")
	GlobalClientSecret  = os.Getenv("MY_STG_OTC_PARTNER_SECRET")
	GlobalRedirectUri   = "http://localhost:8888/result"
)

func flow(merchant merchant.OnlineTransaction, methods []string, currency string) {

	codeVerifier := utils.GenerateRandomString(64)

	params := new(dto.OnaCreateWebUrlParams)
	params.CodeVerifier = codeVerifier
	params.PartnerTxID = utils.GenerateRandomString(32)
	fmt.Println("PartnerID = " + params.PartnerTxID)
	params.PartnerGroupTxID = utils.GenerateRandomString(32)
	params.Amount = int64(2000)
	params.Description = "testing"
	params.Currency = currency

	params.HidePaymentMethods = &methods
	start := time.Now()

	// Web init
	response, state, err := merchant.OnaCreateWebUrl(nil, params)
	if err != nil {
		panic(err)
	}
	elapsed := time.Since(start)
	log.Printf("Binomial took %s", elapsed)
	fmt.Println(response)
	fmt.Println(state)
	fmt.Println("Enter response code after payment ")
	var code string
	code = ""
	fmt.Scanln(&code)

	// API OnA OAuth
	oauthParams := &dto.OnaOAuth2TokenParams{
		CodeVerifier: codeVerifier,
		Code:         code,
	}
	respAuthCode, err := merchant.OnaOAuth2Token(nil, oauthParams)
	if err != nil {
		panic(err)
	}
	// fmt.Printf("%v", getData(processResponse(respAuthCode, err)))
	fmt.Println("DONE API OnA OAuth")

	//access_token := fmt.Sprintf("%v", getData(processResponse(respAuthCode, err))["access_token"])
	access_token := respAuthCode.AccessToken

	// API OnA charge complete
	chargeCompleteParams := &dto.OnaChargeCompleteParams{
		PartnerTxID: params.PartnerTxID,
		AccessToken: fmt.Sprintf("%v", access_token),
	}
	//respChargeComplete, err := merchant.OnaChargeComplete(nil, chargeCompleteParams)
	_, err = merchant.OnaChargeComplete(nil, chargeCompleteParams)
	//if respChargeComplete.StatusCode >= 400 {
	//	panic(err)
	//}
	//fmt.Printf("%v", getData(processResponse(respChargeComplete, err)))
	if err != nil {
		panic(err)
	}

	fmt.Println("DONE API OnA charge complete")

	// API OnA charge complete status
	chargeCompleteStatusParams := &dto.OnaGetChargeStatusParams{
		PartnerTxID: params.PartnerTxID,
		AccessToken: access_token,
		Currency:    params.Currency,
	}
	getChargeStatus, err := merchant.OnaGetChargeStatus(nil, chargeCompleteStatusParams)
	//if getChargeStatus.StatusCode >= 400 {
	//	panic(err)
	//}
	if err != nil {
		panic(err)
	}
	// fmt.Printf("%v", getData(processResponse(getChargeStatus, err)))
	fmt.Println("DONE API OnA charge complete status")

	// API OnA refund
	//originTxID := fmt.Sprintf("%v", getData(processResponse(getChargeStatus, err))["txID"])
	originTxID := getChargeStatus.TxID
	fmt.Println("\nEnter refund amount")
	var refundAmount int64
	fmt.Scanln(&refundAmount)
	refundParams := &dto.OnaRefundParams{
		RefundPartnerTxID: params.PartnerTxID,
		PartnerGroupTxID:  params.PartnerGroupTxID,
		Amount:            refundAmount,
		Currency:          params.Currency,
		TxID:              originTxID,
		Description:       "refund haha",
		AccessToken:       access_token,
	}
	//respRefund, err := merchant.OnaRefund(nil, refundParams)
	//if respRefund.StatusCode >= 400 {
	//	panic(err)
	//}
	//fmt.Printf("%v", getData(processResponse(respRefund, err)))

	_, err = merchant.OnaRefund(nil, refundParams)
	if err != nil {
		panic(err)
	}
	fmt.Println("DONE API OnA Refund")

	// API OnA refund status
	refundStatusParams := &dto.OnaGetRefundStatusParams{
		RefundPartnerTxID: params.PartnerTxID,
		Currency:          params.Currency,
		AccessToken:       access_token,
	}
	//respRefundStatus, err := merchant.OnaGetRefundStatus(nil, refundStatusParams)
	//if respRefundStatus.StatusCode >= 400 {
	//	panic(err)
	//}
	//fmt.Printf("%v", getData(processResponse(respRefundStatus, err)))
	_, err = merchant.OnaGetRefundStatus(nil, refundStatusParams)
	if err != nil {
		panic(err)
	}
	fmt.Println("DONE API OnA Refund Status")

	// API OnA OTC status
	OTCStatusParams := &dto.OnaGetOTCStatusParams{
		PartnerTxID: params.PartnerTxID,
		Currency:    params.Currency,
	}
	//respOTCStatus, err := merchant.OnaGetOTCStatus(nil, OTCStatusParams)
	//if respOTCStatus.StatusCode >= 400 {
	//	fmt.Println(respOTCStatus.StatusCode)
	//	panic(err)
	//}
	//fmt.Printf("%v", getData(processResponse(respOTCStatus, err)))
	_, err = merchant.OnaGetOTCStatus(nil, OTCStatusParams)
	if err != nil {
		panic(err)
	}
	fmt.Println("DONE API OnA OTC Status")

}

func flowRegional() {
	merchant := merchant.NewMerchantOnline(GlobalEnv, GlobalCountry, GlobalPartnerID, GlobalPartnerSecret, GlobalMerchantID, GlobalClientID, GlobalClientSecret, GlobalRedirectUri)
	methods := []string{"INSTALMENT"}
	flow(merchant, methods, "MYR")
}
func flowVN() {
	merchant := merchant.NewMerchantOnline(VNEnv, VNCountry, VNPartnerID, VNPartnerSecret, VNMerchantID, VNClientID, VNClientSecret, VNRedirectUri)
	flow(merchant, nil, "VND")
}

func main() {
	fmt.Println("Choose which flow you want to test:")
	fmt.Println("1. VN")
	fmt.Println("2. Regional")
	var flow int = 0
	fmt.Scanln(&flow)
	if flow == 1 {
		flowVN()
	} else if flow == 2 {
		flowRegional()
	} else {
		fmt.Println("Please re-run and choose 1/2")
	}
}

func processResponse(resp *http.Response, err error) merchant.Response {
	if err != nil {
		return merchant.Response{
			Code: -1,
			Data: "Error during request data",
		}
	}
	defer resp.Body.Close()
	bodyBytes, err := io.ReadAll(resp.Body)

	return merchant.Response{
		Code: resp.StatusCode,
		Data: string(bodyBytes),
	}
}

func getData(response merchant.Response) map[string]interface{} {
	data := make(map[string]interface{})
	dataString := fmt.Sprintf("%v", response.Data)
	print(dataString)
	err := json.Unmarshal([]byte(dataString), &data)
	if err != nil {
		panic(err)
	}
	return data
}
