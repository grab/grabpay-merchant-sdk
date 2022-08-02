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
	v3 "github.com/grab/grabpay-merchant-sdk/dto/v3"
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
	GlobalCountry       = config.CountryGlobal
	GlobalPartnerID     = os.Getenv("MY_STG_OTC_PARTNER_ID")
	GlobalPartnerSecret = os.Getenv("MY_STG_OTC_PARTNER_SECRET")
	GlobalMerchantID    = os.Getenv("MY_STG_OTC_MERCHANT_ID")
	GlobalClientID      = os.Getenv("MY_STG_OTC_PARTNER_ID")
	GlobalClientSecret  = os.Getenv("MY_STG_OTC_PARTNER_SECRET")
	GlobalRedirectUri   = "http://localhost:8888/result"

	SGEnv           = config.EnvSTG
	SGCountry       = config.CountrySG
	SGPartnerID     = os.Getenv("SG_STG_POS_PARTNER_ID")
	SGPartnerSecret = os.Getenv("SG_STG_POS_PARTNER_SECRET")
	SGMerchantID    = os.Getenv("SG_STG_POS_MERCHANT_ID")
	SGTerminalId    = os.Getenv("SG_STG_POS_TERMINAL_ID")
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

func PosV3Flow() {
	merchant1 := merchant.NewMerchantOfflineV3(SGEnv, SGCountry, SGPartnerID, SGPartnerSecret, SGMerchantID, SGTerminalId)
	partner_tx_id := utils.GenerateRandomString(32)
	time := time.Now().Add(time.Minute * 10).Unix()
	fmt.Println(time)
	posInitParam := &v3.POSInitQRPaymentParams{
		POSDetails: &v3.POSDetailsRequest{
			TerminalID: SGTerminalId,
		},
		TransactionDetails: &v3.POSInitTransactionDetails{
			PaymentChannel:    "MPQR",
			StoreGrabID:       SGMerchantID,
			GrabTxID:          partner_tx_id,
			PartnerTxID:       partner_tx_id,
			PartnerGroupTxID:  partner_tx_id,
			Amount:            int64(500),
			Currency:          "SGD",
			PaymentExpiryTime: time,
		},
	}
	resp, err := merchant1.POSInitiate(nil, posInitParam)
	if err != nil {
		panic(err)
	}
	initResponse := &v3.POSInitQRPaymentResponse{}
	err = merchant.ProcessResponse(resp, initResponse)
	//if err != nil {
	//	panic(err)
	//}
	jsonString, err := json.Marshal(initResponse)
	fmt.Println(string(jsonString))

	inquireParam := &v3.POSInquireQRPaymentParams{
		TransactionDetails: &v3.POSInquireTransactionDetails{
			Currency:       "SGD",
			PaymentChannel: "MPQR",
			TxType:         "PAYMENT",
			TxRefType:      "PARTNERTXID",
			TxRefID:        partner_tx_id,
			StoreGrabID:    SGMerchantID,
		},
	}
	inquireResp, err := merchant1.POSInquire(nil, inquireParam)
	if err != nil {
		panic(err)
	}
	inquireResponse := &v3.POSInquireQRPaymentResponse{}
	err = merchant.ProcessResponse(inquireResp, inquireResponse)
	if err != nil {
		panic(err)
	}
	jsonString, err = json.Marshal(inquireResponse)
	fmt.Println(string(jsonString))

	var paid = 0
	fmt.Println("Do you paid?")
	fmt.Println("1. Yes - continue refunding")
	fmt.Println("2. No - continue canceling")
	fmt.Scanln(&paid)
	if paid == 1 {
		refundParam := &v3.POSRefundQRPaymentParams{
			TransactionDetails: &v3.POSRefundTransactionDetails{
				Amount:            posInitParam.TransactionDetails.Amount,
				PaymentChannel:    posInitParam.TransactionDetails.PaymentChannel,
				StoreGrabID:       SGMerchantID,
				OriginPartnerTxID: partner_tx_id,
				PartnerTxID:       partner_tx_id,
				PartnerGroupTxID:  partner_tx_id,
				Currency:          posInitParam.TransactionDetails.Currency,
				Reason:            "buy-something-else",
			},
		}
		refundResp, _ := merchant1.POSRefund(nil, refundParam)

		refundResponse := &v3.POSRefundQRPaymentResponse{}
		error1 := merchant.ProcessResponse(refundResp, refundResponse)
		if error1 != nil {
			panic(error1)
		}
		jsonString, err = json.Marshal(refundResponse)
		fmt.Println(string(jsonString))
	} else {
		cancelParam := &v3.POSCancelQRPaymentParams{
			TransactionDetails: &v3.POSCancelTransactionDetails{
				PaymentChannel:    posInitParam.TransactionDetails.PaymentChannel,
				StoreGrabID:       SGMerchantID,
				OriginPartnerTxID: partner_tx_id,
				Currency:          posInitParam.TransactionDetails.Currency,
			},
		}
		cancelResp, _ := merchant1.POSCancel(nil, cancelParam)

		cancelResponse := &v3.POSCancelQRPaymentResponse{}
		error1 := merchant.ProcessResponse(cancelResp, cancelResponse)
		if error1 != nil {
			panic(error1)
		}
		jsonString, err = json.Marshal(cancelResponse)
		fmt.Println(string(jsonString))
	}
}
func main() {
	fmt.Println("Choose which flow you want to test:")
	fmt.Println("1. VN")
	fmt.Println("2. Regional")
	fmt.Println("3. POS v3")
	var flow int = 0
	fmt.Scanln(&flow)
	if flow == 1 {
		flowVN()
	} else if flow == 2 {
		flowRegional()
	} else if flow == 3 {
		PosV3Flow()
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
