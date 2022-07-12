// Package utils provides all helper functions for APIs
package utils

import (
	"crypto/hmac"
	"crypto/md5"
	"crypto/rand"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"math/big"
	"net/http"
	"strings"
	"time"

	"github.com/grab/grabpay-merchant-sdk/config"
)

type popSignaturePayload struct {
	TimeSinceEpoch int64  `json:"time_since_epoch"`
	Sig            string `json:"sig"`
}

func GenerateCodeVerifier(codeVerifier string) string {
	if len(codeVerifier) >= 43 && len(codeVerifier) <= 128 {
		return codeVerifier
	}

	for 43 > len(codeVerifier) {
		codeVerifier = "0" + codeVerifier
	}
	return codeVerifier
}
func Base64UrlEncode(data string) string {
	tmp := strings.Replace(data, "=", "", -1)
	tmp = strings.Replace(tmp, "/", "_", -1)
	return strings.Replace(tmp, "+", "-", -1)
}

func computeHmac256(message string, secret string) string {
	key := []byte(secret)
	h := hmac.New(sha256.New, key)
	h.Write([]byte(message))
	result := h.Sum(nil)
	return base64.StdEncoding.EncodeToString(result)
}
func GenSha256(data string) string {
	hash := sha256.New()
	hash.Write([]byte(data))
	mdStr := base64.StdEncoding.EncodeToString(hash.Sum(nil))
	return mdStr
}
func GenerateRandomString(length int) string {
	// rand.Seed(time.Now().UnixNano())
	letters := []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
	b := make([]rune, length)
	for i := range b {
		pos, _ := rand.Int(rand.Reader, big.NewInt(int64(len(letters))))
		b[i] = letters[pos.Int64()]
	}
	return string(b)
}

func GenerateMsgID(msgID string) string {
	if msgID == "" || len(msgID) != 32 {
		msg := GenerateRandomString(32)
		msgID = GenerateMD5(msg)
	}
	return msgID
}

func CurrentTimeInGMTAndUnix(_time time.Time) (string, int64) {
	dateUTC := _time.Format(time.RFC1123)
	return strings.Replace(dateUTC, "UTC", "GMT", -1), _time.Unix()
}

func GenerateMD5(msg string) string {
	hash := md5.Sum([]byte(msg))
	return hex.EncodeToString(hash[:])
}

func GenerateHmac(env *config.Config, method string, date string, contentType string, apiUrl string, requestBody []byte) string {
	hash := sha256.New()
	hash.Write(requestBody)
	mdStr := base64.StdEncoding.EncodeToString(hash.Sum(nil))

	if method == http.MethodGet || requestBody == nil {
		mdStr = ""
	}

	request := []string{method, contentType, date, apiUrl, mdStr}
	stringToSign := strings.Join(request, "\n") + "\n"

	hmacSignature := computeHmac256(stringToSign, env.PartnerSecret)
	return hmacSignature
}

func GeneratePOPSign(env *config.Config, accessToken string, unixTime int64) string {
	messageToHash := fmt.Sprintf("%d%s", unixTime, accessToken)

	hash := hmac.New(sha256.New, []byte(env.ClientSecret))
	if _, err := hash.Write([]byte(messageToHash)); err != nil {
		return ""
	}

	signature := base64.RawURLEncoding.EncodeToString(hash.Sum(nil))

	// append the signature and time in a JSON format
	jsonPayload := popSignaturePayload{
		TimeSinceEpoch: unixTime,
		Sig:            signature,
	}

	payloadBytes, _ := json.Marshal(jsonPayload)
	return base64.RawURLEncoding.EncodeToString(payloadBytes)
}

func PrepareCommonHeaders(_config *config.Config, contentType, msgID string, now time.Time) http.Header {
	header := http.Header{}
	msgID = GenerateMsgID(msgID)
	formattedTimestamp, _ := CurrentTimeInGMTAndUnix(now)
	header.Set("Date", formattedTimestamp)
	header.Set("Content-Type", contentType)
	header.Set("Accept", contentType)
	header.Set("X-Request-ID", msgID)
	header.Set("X-Sdk-Country", _config.Country)
	header.Set("X-Sdk-Version", _config.SdkVersion)
	header.Set("X-Sdk-Language", "Golang")
	header.Set("X-Sdk-Signature", _config.SdkSignature)
	return header
}

func AppendAccessTokenAndSigToHeaders(_config *config.Config, headers http.Header, accessToken string, now time.Time) http.Header {
	_, unixTime := CurrentTimeInGMTAndUnix(now)
	headers.Set("X-GID-AUX-POP", GeneratePOPSign(_config, accessToken, unixTime))
	headers.Set("Authorization", fmt.Sprintf("Bearer %s", accessToken))

	return headers
}

func AppendHmacToHeaders(_config *config.Config, headers http.Header, httpMethod, contentType, path string, bytesBody []byte, now time.Time) http.Header {
	formattedTimestamp, _ := CurrentTimeInGMTAndUnix(now)
	hmac := GenerateHmac(_config, httpMethod, formattedTimestamp, contentType, path, bytesBody)
	headers.Set("Authorization", fmt.Sprintf("%s:%s", _config.PartnerID, hmac))
	return headers
}
