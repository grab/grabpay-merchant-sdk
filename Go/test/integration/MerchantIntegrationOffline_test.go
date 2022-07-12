package integration

import (
	"fmt"
	"io"
	"os"
	"testing"

	"github.com/grab/grabpay-merchant-sdk/dto"
	"github.com/grab/grabpay-merchant-sdk/merchant"
	"github.com/grab/grabpay-merchant-sdk/utils"
)

var (
	_env           = "STG"
	_country       = "VN"
	_partnerID     = os.Getenv("VN_STG_POS_PARTNER_ID")
	_partnerSecret = os.Getenv("VN_STG_POS_PARTNER_SECRET")
	_merchantID    = os.Getenv("VN_STG_POS_MERCHANT_ID")
	terminalID     = os.Getenv("VN_STG_POS_TERMINAL_ID")
)

func TestCreateQRCode(t *testing.T) {
	merchant := merchant.NewMerchantOffline(_env, _country, _partnerID, _partnerSecret, _merchantID, terminalID)
	params := new(dto.PosCreateQRCodeParams)
	params.PartnerTxID = utils.GenerateRandomString(32)
	params.Amount = int64(10000)
	params.MsgID = utils.GenerateMD5(utils.GenerateRandomString(32))
	params.Currency = "VND"
	resp, err := merchant.PosCreateQRCode(nil, params)

	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()
	bodyBytes, err := io.ReadAll(resp.Body)
	fmt.Println(string(bodyBytes))

}
