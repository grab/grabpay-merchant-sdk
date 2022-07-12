# GrabPay Merchant Integration for .NET

The .NET SDK is provided as a .NET library targeted at .NET Standard 2.0.

## Using the SDK

1. Add Net library to your solution.
   Right click on your project solution. Select `Add` -> `Existing project` -> Select `Net.csproj` file.
1. Import Net project to your project.
   Right click on your project Dependencies. Select `Add Reference` -> Select `Net` Project.

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

## One-Time Charge (OTC) Transactions

### Configuring for One-Time Charge

```cs
using Net.Public;

// ...

MerchantIntegrationOnline clientOnA = new MerchantIntegrationOnline(stage, country, partner_id, partner_secret, merchant_id, client_id, client_secret, redirect_url);
```

### One-Time Charge (OTC) API

1. Charge Init:

```cs
respChargeInit = clientOnA.OnaChargeInit(partnerTxID,  partnerGroupTxID, amount, currency, description, metaInfo, items, shippingDetails, hidePaymentMethods)
```

2. Create Web Url:

```cs
respCreateWebUrl = clientOnA.OnaCreateWebUrl(partnerTxID,  partnerGroupTxID, amount, currency, codeVerifier, description, metaInfo, items, shippingDetails, hidePaymentMethods, state)
```

3. get OAuth Token

```cs
respAuthCode = clientOnA.OnaOAuth2Token(code, codeVerifier)
```

4. Charge Complete

```cs
respChargeComplete = clientOnA.OnaChargeComplete(partnerTxID,respAuthCode.access_token)
```

5 Get Charge status

```cs
respChargeStatus = clientOnA.OnaGetChargeStatus(partnerTxID, currency, respAuthCode.accessToken)
```

6. refund online

```cs
response = clientOnA.OnaRefund(refundPartnerTxID, amount, origPartnerTxID, description, respAuthCode.accessToken, currency)
```

7. Get Refund Status

```cs
response = clientOnA.OnaGetRefundStatus(partnerTxID, respAuthCode.accessToken, currency)
```

8. Get One time charge status

```cs
response = clientOnA.OnaGetOTCStatus(partnerTxID, respAuthCode.accessToken, currency)
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```cs
using Net.Public;

// ...

MerchantIntegrationOffline clientPos = new MerchantIntegrationOffline(stage, country, partner_id, partner_secret, merchant_id, terminal_id);
```

### Point of Sale (POS) API

1. Create QR Code

```cs
respCreateQRCode= clientPos.PosCreateQRCode(msgID, partnerTxID, amount, currency)
```

2. Get transaction detail:

```cs
resp = clientPos.PosGetTxnStatus(msgID, partnerTxID, currency)
```

3. Cancel a Transaction

```cs
resp = clientPos.PosCancel(msgID, partnerTxID, origPartnerTxID, origTxID, currency)
```

4. Refund a POS Payment

```cs
resp = clientPos.PosRefund(msgID, refundPartnerTxID, amount, currency, origPartnerTxID, description)
```

5. Get refund transaction detail:

```cs
resp = clientPos.PosGetRefundDetails(msgID, refundPartnerTxID, currency)
```

6. Perform a Transaction

```cs
resp = clientPos.PosPerformQRCode(msgID, partnerTxID, amount, currency, code)
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
