package utils

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strings"
	"testing"
	"time"

	"github.com/grab/grabpay-merchant-sdk/config"
	"github.com/stretchr/testify/assert"
)

func TestBase64UrlEncode(t *testing.T) {
	t.Parallel()

	input := "+++"
	output := Base64UrlEncode(input)
	expected := "---"
	assert.Equal(t, expected, output,
		"expected and real result of base64UrlEncode must be same")

	input = "abs"
	output = Base64UrlEncode(input)
	expected = "abs"
	assert.Equal(t, expected, output,
		"expected and real result of base64UrlEncode must be same")

	input = "poiuqwe+lkjasdfn12=234-//asfdjh12+"
	output = Base64UrlEncode(input)
	expected = "poiuqwe-lkjasdfn12234-__asfdjh12-"
	assert.Equal(t, expected, output,
		"expected and real result of base64UrlEncode must be same")
}

func TestCurrentTimeInGMTAndUnix(t *testing.T) {
	t.Parallel()

	gmtTime, _ := CurrentTimeInGMTAndUnix(time.Now().UTC())
	if !strings.Contains(gmtTime, "GMT") {
		t.Fatal(`GMT time should contain GMT keyword`)
	}
	assert.Equal(t, 29, len(gmtTime), `expected gmt time should have %D length`, 29)

	timeSplit := strings.Split(gmtTime, " ")
	assert.Equal(t, 6, len(timeSplit))
}

func TestGenerateMD5(t *testing.T) {
	t.Parallel()

	input := ""
	output := GenerateMD5(input)
	expected := "d41d8cd98f00b204e9800998ecf8427e"
	assert.Equal(t, expected, output, "expected and real result of generateMD5 must be same")

	input = "lakjshf123-089.klsajdfh"
	output = GenerateMD5(input)
	expected = "2da5bfaf7de739e4b2517e6162357fe5"
	assert.Equal(t, expected, output, "expected and real result of generateMD5 must be same")
}

func TestGenerateHmac(t *testing.T) {
	t.Parallel()

	var body = map[string]interface{}{
		"partnerTxID":      "ORDER123",
		"partnerGroupTxID": "ORDER123",
		"amount":           100,
		"currency":         "SGD",
		"merchantID":       "44e46f25-7787-44ce-b785-cf9f9b95601e",
		"description":      "paysi-agent-partner-v2-otc",
	}

	requestBody, _ := json.Marshal(body)

	env := new(config.Config)
	env.Init("STG", "country", "569c95f1-f5e4-4bd9-8dff-541d8f2af81e", "NvPwMZqiUkYyfDwG", "merchantID", "terminalID", "clientID", "clientSecret", "redirectUri")

	output := GenerateHmac(env, "POST", "Mon, 12 Nov 2018 08:20:55 GMT", "application/json", "/grabpay/partner/v2/charge/init", requestBody)
	expected := "pSK8CLf03MJzmSPmeDyqsrSIuwyy1QvExmxmf3sme4w="
	assert.Equal(t, expected, output, "expected and real result of generateHmac must be same")

	output = GenerateHmac(env, http.MethodGet, "Mon, 12 Nov 2018 08:20:55 GMT", "application/json", "/grabpay/partner/v2/charge/init", requestBody)
	assert.Equal(t, "Y3fKICAm9/5nTkr3nCNrifuQ4HVwXa/qVZfftCo0dpo=", output, "mdStr should be empty")

	output = GenerateHmac(env, http.MethodPost, "Mon, 12 Nov 2018 08:20:55 GMT", "application/json", "/grabpay/partner/v2/charge/init", nil)
	assert.Equal(t, "+tPrMFb6M+tqffZtYmKV/9DzX6pUc48wfJe++tqHQmA=", output, "mdStr should be empty")
}

func TestGenerateRandomString(t *testing.T) {
	t.Parallel()

	length := 32
	str := GenerateRandomString(32)
	if len(str) != 32 {
		t.Fatal(`GenerateRandomString(%D) should return a string with length %D but %D`, length, length, len(str))
	}
}

func TestSha256(t *testing.T) {
	t.Parallel()

	output := GenSha256("abc")
	assert.Equal(t, "ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=", output, "SHA256 should be the same")

	output = GenSha256("")
	assert.Equal(t, "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=", output, "SHA256 should be the same")

	output = GenSha256("{name: john}")
	assert.Equal(t, "aEVXvRaiF6i4hftN0kZJMlJthLi62Q6DyO5NVliApKE=", output, "SHA256 should be the same")

	output = GenSha256(GenerateRandomString(10000))
	// why 44? 6 bit/char, 256/6 = 42,6 + pading ~ 44
	assert.Equal(t, 44, len(output), "SHA256 code should have length 44")
}

func TestGenerateMsgID(t *testing.T) {
	t.Parallel()

	msgID := "qwertyuiopasdfghjklzxcvbnm123456"
	actual := GenerateMsgID(msgID)
	assert.Equal(t, msgID, actual, "msgID should be remained if right")

	msgID = "abxy"
	actual = GenerateMsgID(msgID)
	assert.NotEqual(t, msgID, actual, "msgID should be generated if wrong")
	assert.Equal(t, 32, len(actual), "generated msgID should have length 32")

	msgID = ""
	actual = GenerateMsgID(msgID)
	assert.Equal(t, 32, len(actual), "generated msgID should have length 32")
}

func TestGeneratePOPSign(t *testing.T) {
	t.Parallel()

	access_token := "some-access-token"

	_, unixTime := CurrentTimeInGMTAndUnix(time.Now().UTC())
	env := new(config.Config)
	env.Init("env", "country", "partnerID", "partnerSecret", "merchantID", "terminalID", "clientID", "clientSecret", "redirectURI")

	access_token = "some-access-token-abc"
	pops := GeneratePOPSign(env, access_token, unixTime)
	assert.Equal(t, 111, len(pops), "generated msgID should have length 111")

	// a fixed input
	access_token = "some-access-token-1"
	unixTime = 1645690076
	expectedPops := "eyJ0aW1lX3NpbmNlX2Vwb2NoIjoxNjQ1NjkwMDc2LCJzaWciOiI2R2xOR0JFX3I0UmZCSFZBMUl2T0ZJUEpSOVl5dXlSSUJ5Sm16dU1venBVIn0"
	pops = GeneratePOPSign(env, access_token, unixTime)
	assert.Equal(t, expectedPops, pops)
}

func TestPrepareCommonHeaders(t *testing.T) {
	t.Parallel()

	env := new(config.Config)
	env.Init("env", "vn-1", "x-partnerID-x", "partnerSecret", "merchantID", "terminalID", "clientID", "clientSecret", "redirectURI")
	env.SdkSignature = "x-sign-x"
	env.SdkVersion = "x-version-x"

	headers := PrepareCommonHeaders(env, "ct-123", "msgID", time.Now().UTC())
	length := 8

	assert.Equal(t, length, len(headers), fmt.Sprintf("common header should have %d key-value", length))
	assert.NotEmpty(t, headers.Get("Date"), "common header should contain Date key")
	assert.Equal(t, headers.Get("Content-Type"), "ct-123", "actual value of Content-type is different from expected")
	assert.Equal(t, headers.Get("Accept"), "ct-123", "actual value of Accept is different from expected")
	assert.Equal(t, 32, len(headers.Get("X-Request-ID")), "actual value of X-Request-ID should have 32 characters")
	assert.Equal(t, headers.Get("X-Sdk-Country"), "VN-1", "actual value of X-Sdk-Country is different from expected")
	assert.Equal(t, headers.Get("X-Sdk-Version"), "x-version-x", "actual value of X-Sdk-Version is different from expected")
	assert.Equal(t, headers.Get("X-Sdk-Language"), "Golang", "actual value of X-Sdk-Language is different from expected")
	assert.Equal(t, headers.Get("X-Sdk-Signature"), "x-sign-x", "actual value of X-Sdk-Signature is different from expected")

}

func TestAppendAccessTokenAndSigToHeaders(t *testing.T) {
	t.Parallel()

	env := new(config.Config)
	env.Init("env", "country", "partnerID", "partnerSecret", "merchantID", "terminalID", "clientID", "clientSecret", "redirectURI")
	env.SdkSignature = "sign"
	env.SdkVersion = "version"

	header := http.Header{}
	header.Set("name", "tesing")
	header = AppendAccessTokenAndSigToHeaders(env, header, "access-token-123", time.Now().UTC())

	length := 3
	assert.Equal(t, length, len(header), fmt.Sprintf("common header should have %d key-value", length))

	header = http.Header{}
	header = AppendAccessTokenAndSigToHeaders(env, header, "access-token-123", time.Now().UTC())
	length = 2
	assert.Equal(t, length, len(header), fmt.Sprintf("common header should have %d key-value", length))

	assert.NotEmpty(t, header.Get("X-GID-AUX-POP"), "header should not have empty value for X-GID-AUX-POP")
	assert.NotEmpty(t, header.Get("Authorization"), "header should not have empty value for Authorization")
}

func TestAppendHmacToHeaders(t *testing.T) {
	t.Parallel()

	env := new(config.Config)
	env.Init("env", "country", "partnerID", "partnerSecret", "merchantID", "terminalID", "clientID", "clientSecret", "redirectURI")
	env.SdkSignature = "sign"
	env.SdkVersion = "version"

	header := http.Header{}
	header.Set("name", "tesing")
	header = AppendHmacToHeaders(env, header, "method", "content-type", "/path/to/resource", []byte{}, time.Now().UTC())

	length := 2
	assert.Equal(t, length, len(header), fmt.Sprintf("common header should have %d key-value", length))

	header = http.Header{}
	header = AppendHmacToHeaders(env, header, "method", "content-type", "/path/to/resource", []byte{}, time.Now().UTC())
	length = 1
	assert.Equal(t, length, len(header), fmt.Sprintf("common header should have %d key-value", length))

	assert.NotEmpty(t, header.Get("Authorization"), "header should not have empty value for Authorization")
}
