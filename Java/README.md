# GrabPay Merchant Integration SDK for Java

## Requirements

- JDK 8 or above
- json package (`org.json`)
- Gradle v7.4 (If you have a different Gradle version, you can use `gradle wrapper` to download a wrapper)

## Using the SDK

Before using this SDK, you should assemble SDK by running `gradle assemble`.

For using this SDK, you might copy all files in [src](./src) folder and paste it to your project. Then you should import 2 classes `MerchantIntegrationOnline`, `MerchantIntegrationOffline` in `src/main/java/com/merchantsdk/payment` folder.

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

### Example set up parameters

```java
String partnerTxID = "partnerTxID";
String partnerGroupTxID = "partnerGroupTxID";
String refundPartnerTxID = "refundPartnerTxID";
long amount = 2000;
String currency = "CUR";
String description = "this is testing";

String origPartnerTxID = "origPartnerTxID";
String msgID = "msgID";
```

## One-Time Charge (OTC) Transactions

### Configuring for One-Time Charge

```java
MerchantIntegrationOnline merchantIntegrationOnline = new MerchantIntegrationOnline(stage, country, partner_id, partner_secret, merchant_id, client_id, client_secret, redirect_url)
```

### One-Time Charge (OTC) API

The values for `metaInfo`, `items`, `shippingDetails`, `hidePaymentMethods`, `state` are left as `null` in the examples below as they are optional parameters.

1. onaChargeInit

```java
merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency, description, metaInfo, items, shippingDetails, hidePaymentMethods)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaChargeInit(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, null, null, null, null);
```

2. onaCreateWebUrl

```java
merchantIntegrationOnline.onaCreateWebUrl(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, metaInfo, items, shippingDetails, hidePaymentMethods, state)
```

**Example request:**

```java
String response = merchantIntegrationOnline.onaCreateWebUrl(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, null, null, null, null, null);
```

3. onaOAuth2Token

```java
merchantIntegrationOnline.onaOAuth2Token(String partnerTxID, String code)
```

Merchant get **code** in redirected url after successfully pay

**Example request:**

```java
String code = "a09a6c1da7c843519d03978d659c60d8" ;
JSONObject response = merchantIntegrationOnline.onaOAuth2Token(partnerTxID, code);
```

4. onaChargeComplete

```java
merchantIntegrationOnline.onaChargeComplete(String partnerTxID,String accessToken)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaChargeComplete(partnerTxID, accessToken);
```

5. onaGetChargeStatus

```java
merchantIntegrationOnline.onaGetChargeStatus(String partnerTxID,String currency,String accessToken)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaGetChargeStatus(partnerTxID,currency, accessToken);
```

6. onaRefund

```java
merchantIntegrationOnline.onaRefund(String refundPartnerTxID, String partnerGroupTxID,long amount,String currency,String txID,String description,String accessToken)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

Merchant get **txID** from **_onaChargeComplete_** or **_onaGetChargeStatus_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaRefund(refundPartnerTxID, partnerGroupTxID, amount, currency, txID, description, accessToken);
```

7. onaGetRefundStatus

```java
merchantIntegrationOnline.onaGetRefundStatus(refundPartnerTxID, currency, accessToken)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaGetRefundStatus(refundPartnerTxID, accessToken, currency);
```

8. onaGetOTCStatus

```java
merchantIntegrationOnline.onaGetOTCStatus(String partnerTxID, accessToken, String currency)
```

Merchant get **accessToken** from **_onaOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.onaGetOTCStatus(partnerTxID, accessToken, currency);
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```
MerchantIntegrationOffline merchantIntegrationOffline = new MerchantIntegrationOffline(stage, country, partner_id, partner_secret, merchant_id, terminal_id)
```

### Point of Sale (POS) API

1. posCreateQRCode

```java
merchantIntegrationOffline.posCreateQRCode(String msgID, String partnerTxID, long amount, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posCreateQRCode(msgID, partnerTxID, amount, currency);
```

2. posPerformQRCode

```java
merchantIntegrationOffline.posPerformQRCode(String msgID, String partnerTxID, long amount, String currency, String code)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posPerformQRCode(msgID, partnerTxID, amount, currency, code);
```

3. posCancel

```java
merchantIntegrationOffline.posCancel(String msgID, String partnerTxID,String origPartnerTxID,String origTxID,String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posCancel(msgID, partnerTxID, origPartnerTxID, origTxID, currency);
```

4. posRefund

```java
merchantIntegrationOffline.posRefund(msgID, refundPartnerTxID, amount, currency, origPartnerTxID, description)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posRefund(msgID, refundPartnerTxID, amount, currency, origPartnerTxID, description);
```

5. posGetTxnDetails

```java
merchantIntegrationOffline.posGetTxnDetails(String msgID, String partnerTxID, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posGetTxnDetails(msgID, partnerTxID, currency);
```

6. posGetRefundDetails

```java
merchantIntegrationOffline.posGetRefundDetails(String msgID, String refundPartnerTxID, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.posGetRefundDetails(msgID, refundPartnerTxID, currency);
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved. Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
