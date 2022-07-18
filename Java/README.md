# GrabPay Merchant Integration SDK for Java

## Requirements

- JDK 8 or above
- json package (`org.json`)
- Gradle v7.4 (If you have a different Gradle version, you can use `gradle wrapper` to download a wrapper)

## Using the SDK

Before using this SDK, you should assemble SDK by running `gradle assemble`.

For using this SDK, you might copy all files in [src](./src) folder and paste it to your project. Then you should import 2 classes `MerchantIntegrationOnline`, `MerchantIntegrationOffline` in `src/main/java/com/merchantsdk/payment` folder.

## Configuring the SDK

- `env` : EnvironmentType : either production or staging, values provided through the `EnvironmentType` enum.
- `country` : Country : [ISO 3166-1 Alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code of the merchant location. Values provided through the `Country` enum
- `partnerId` : String - Unique ID for a partner. Retrieve from Developer Home
- `partnerSecret` : String - Secret key for a partner. Retrieve from Developer Home
- `merchantId` : String - Retrieve from Developer Home
- `terminalId` : String - Retrieve from Developer Home **(POS only)**
- `clientId` : String - Retrieve from Developer Home **(OTC only)**
- `clientSecret` : String - Retrieve from Developer Home **(OTC only)**
- `redirectUrl` : String - The url configured in Developer Home **(OTC only)**

## Details for APIs

### Example set up parameters

```java
String partnerTxID = "partnerTxID";
String partnerGroupTxID = "partnerGroupTxID";
long amount = 2000;
String currency = "CUR";
String description = "this is testing";

String origPartnerTxID = "origPartnerTxID";
String msgID = "msgID";
```

## One-Time Charge (OTC) Transactions

### Configuring for One-Time Charge

```java
MerchantIntegrationOnline merchantIntegrationOnline = new MerchantIntegrationOnline(EnvirionmentType.STAGING, Country.MALAYSIA, partnerId, partnerSecret, merchantId, clientId, clientSecret, redirectUrl);
```

### One-Time Charge (OTC) API

The values for `metaInfo`, `items`, `shippingDetails`, `hidePaymentMethods`, `state` are left as `null` in the examples below as they are optional parameters.

1. chargeInit

```java
merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency, description, metaInfo, items, shippingDetails, hidePaymentMethods)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.chargeInit(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, null, null, null, null);
```

2. generateWebUrl

```java
merchantIntegrationOnline.generateWebUrl(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, metaInfo, items, shippingDetails, hidePaymentMethods, state)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.generateWebUrl(partnerTxID, partnerGroupTxID, amount, currency, codeVerifier, description, null, null, null, null, null);
```

3. getOAuth2Token

```java
merchantIntegrationOnline.getOAuth2Token(String code, String codeVerifier);
```

Merchant get **code** in redirected url after successfully pay

**Example request:**

```java
String code = "a09a6c1da7c843519d03978d659c60d8" ;
JSONObject response = merchantIntegrationOnline.getOAuth2Token(code, codeVerifier);
```

4. chargeComplete

```java
merchantIntegrationOnline.chargeComplete(String partnerTxID, String accessToken)
```

Merchant get **accessToken** from **_getOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.chargeComplete(partnerTxID, accessToken);
```

5. getChargeStatus

```java
merchantIntegrationOnline.getChargeStatus(String partnerTxID, String currency, String accessToken)
```

Merchant get **accessToken** from **_getOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.getChargeStatus(partnerTxID, currency, accessToken);
```

6. Refund

```java
merchantIntegrationOnline.refund(String partnerTxID, String partnerGroupTxID,long amount,String currency, String txID, String description, String accessToken)
```

Merchant get **accessToken** from **_getOAuth2Token_** API response

Merchant get **txID** from **_chargeComplete_** or **_getChargeStatus_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.refund(partnerTxID, partnerGroupTxID, amount, currency, txID, description, accessToken);
```

7. getRefundStatus

```java
merchantIntegrationOnline.getRefundStatus(partnerTxID, currency, accessToken)
```

Merchant get **accessToken** from **_getOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.getRefundStatus(partnerTxID, accessToken, currency);
```

8. getOTCStatus

```java
merchantIntegrationOnline.getOTCStatus(String partnerTxID, accessToken, String currency)
```

Merchant get **accessToken** from **_getOAuth2Token_** API response

**Example request:**

```java
JSONObject response = merchantIntegrationOnline.getOTCStatus(partnerTxID, accessToken, currency);
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```
MerchantIntegrationOffline merchantIntegrationOffline = new MerchantIntegrationOffline(stage, country, partner_id, partner_secret, merchant_id, terminal_id)
```

### Point of Sale (POS) API

1. createQrCode

```java
merchantIntegrationOffline.createQrCode(String msgID, String partnerTxID, long amount, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.createQrCode(msgID, partnerTxID, amount, currency);
```

2. performQrCode

```java
merchantIntegrationOffline.performQrCode(String msgID, String partnerTxID, long amount, String currency, String code)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.performQrCode(msgID, partnerTxID, amount, currency, code);
```

3. cancel

```java
merchantIntegrationOffline.cancel(String msgID, String partnerTxID,String origPartnerTxID,String origTxID,String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.cancel(msgID, partnerTxID, origPartnerTxID, origTxID, currency);
```

4. refund

```java
merchantIntegrationOffline.refund(msgID, partnerTxID, amount, currency, origPartnerTxID, description)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.refund(msgID, partnerTxID, amount, currency, origPartnerTxID, description);
```

5. getTxnDetails

```java
merchantIntegrationOffline.getTxnDetails(String msgID, String partnerTxID, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.getTxnDetails(msgID, partnerTxID, currency);
```

6. getRefundDetails

```java
merchantIntegrationOffline.getRefundDetails(String msgID, String partnerTxID, String currency)
```

**Example request:**

```java
JSONObject response = merchantIntegrationOffline.getRefundDetails(msgID, partnerTxID, currency);
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved. Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
