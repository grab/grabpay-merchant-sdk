# GrabPay Merchant Integration SDK for Go

This Go SDK is provided for merchants who have backend systems implemented in Golang.

## Using the SDK

Before using this Go SDK, you should install all needed packages for this project.

Run command `go get` in root folder of Go SDK

You will work with files in `pkg` package, so you need to import it firstly.

## Configuring the SDK

- `stage` : String : either "PRD" or "STG"
- `country` : String : [ISO 3166-1 Alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code of the merchant location.
- `partner_id` : String - Unique ID for a partner. Retrieve from Developer Home
- `partner_secret` : String - Secret key for a partner. Retrieve from Developer Home
- `merchant_id` : String - Retrieve from Developer Home
- `terminal_id` : String - Retrieve from Developer Home **(POS only)**
- `client_id` : String - Retrieve from Developer Home **(OTC only)**
- `client_secret` : String - Retrieve from Developer Home **(OTC only)**
- `redirect_url` : String - The url configured in Developer Home **(OTC only)**

## Details for APIs

All params for APIs are defined in structs, which are located in `dto` package. There are 2 kind of structs : `online_params`, `offline_params`.
We will provide an example for API OnaChargeInit, you can follow it and write for other APIs

### Example set up parameters

```
partnerTxID := "partnerTxID";
partnerGroupTxID := "partnerGroupTxID";
refundPartnerTxID := "refundPartnerTxID";
amount := int64(2000);
currency := "CUR";
description := "this is testing";
origPartnerTxID := "origPartnerTxID";
msgID := "msgID";
```

## One-Time Charge (OTC) Transactions

### Configuring for One-Time Charge

```go
newOnlineMerchant := pkg.NewMerchantOnline(env, country, partner_id, partner_secret, merchant_id, client_id, client_secret, redirect_url)
```

### One-Time Charge (OTC) API

Parameters _metaInfo_, _items_, _shippingDetails_, _hidePaymentMethods_, _state_ in APIs are left as nil because it is optional
Context parameter in APIs are decleare as context.Background(), you can use your context if needed.

1. onaChargeInit

```
OnaChargeInit(ctx context.Context, params *dto.OnaChargeInitParams) (*http.Reponse, error)
```

**Example request:**

```
params := &dto.OnaChargeInitParams{
    PartnerTxID:        partnerTxID,
    PartnerGroupTxID:   partnerGroupTxID,
    Amount:             amount,
    Description:        "testing",
    Currency:           currency,
}

resp, err := newOnlineMerchant.OnaChargeInit(context.Background(), params)

```

2. onaCreateWebUrl

```go
OnaCreateWebUrl(ctx context.Context, params *dto.OnaCreateWebUrlParams) (string, string, error)
```

**Example request:**

```go
url, state, err := newOnlineMerchant.OnaCreateWebUrl(context.Background(), onaCreateWebUrlParams)
```

3. onaOAuth2Token

```go
OnaOAuth2Token(ctx context.Context, params *dto.OnaOAuth2TokenParams) (*http.Response, error)
```

Merchant get **code** in redirected url after successfully pay

**Example request:**

```go
resp, err := newOnlineMerchant.OnaOAuth2Token(context.Background(), onaOauthParams)
```

4. onaChargeComplete

```
OnaChargeComplete(ctx context.Context, params *dto.OnaChargeCompleteParams) (*http.Response, error)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```go
resp, err := newOnlineMerchant.OnaChargeComplete(context.Background(), onaChargeCompleteParams)
```

5. onaGetChargeStatus

```go
OnaGetChargeStatus(ctx context.Context, params *dto.OnaGetChargeStatusParams) (*http.Response, error)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```go
resp, err := newOnlineMerchant.OnaGetChargeStatus(context.Background(), onaChargeCompleteStatusParams)
```

6. onaRefund

```go
OnaRefund(ctx context.Context, params *dto.OnaRefundParams) (*http.Response, error)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

Merchant get **txID** from **_onaChargeComplete_** or **_onaGetChargeStatus_** API response

**Example request:**

```
resp, err := newOnlineMerchant.OnaRefund(context.Background(), onaRefundParams)
```

7. onaGetRefundStatus

```go
OnaGetRefundStatus(ctx context.Context, params *dto.OnaGetRefundStatusParams) (*http.Response, error)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```go
resp, err := newOnlineMerchant.OnaGetRefundStatus(context.Background(), onaRefundStatusParams)
```

8. onaGetOTCStatus

```go
OnaGetOTCStatus(ctx context.Context, params *dto.OnaGetOTCStatusParams) (*http.Response, error)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```go
resp, err := newOnlineMerchant.OnaGetOTCStatus(context.Background(), onaOTCStatusParams)
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```go
newOfflineMerchant := pkg.NewMerchantOffline(_env, _country, _partnerID, _partnerSecret, _merchantID, terminal_id)
```

### Point of Sale (POS) API

1. posCreateQRCode

```go
PosCreateQRCode(ctx context.Context, params *dto.PosCreateQRCodeParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosCreateQRCode(context.Background(), posCreateQRParams)
```

2. posPerformQRCode

```go
PosPerformQRCode(ctx context.Context, params *dto.PosPerformQRCodeParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosPerformQRCode(context.Background(), posPerformQRCodeParams)
```

3. posCancel

```go
PosCancel(ctx context.Context, params *dto.PosCancelParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosCancel(context.Background(), posCancelParams)
```

4. posRefund

```go
PosRefund(ctx context.Context, params *dto.PosRefundParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosRefund(context.Background(), posRefundParams)
```

5. posGetTxnDetails

```go
PosGetTxnDetails(ctx context.Context, params *dto.PosGetTxnDetailsParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosGetTxnDetails(context.Background(), posGetTxnDetailsParams)
```

6. posGetRefundDetails

```go
PosGetRefundDetails(ctx context.Context, params *dto.PosGetRefundDetailsParams) (*http.Response, error)
```

**Example request:**

```go
resp, err := newOfflineMerchant.PosGetRefundDetails(context.Background(), posGetRefundDetailsParams)
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved. Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
