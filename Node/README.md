# GrabPay Merchant Integration SDK for Node.js

## Using the SDK

> Note: While this SDK is provided for JavaScript, it must only be used in server-side environment. Using the SDK on client-side environments (e.g. browsers) may expose your credentials such as `partner_secret` to public.

After getting a copy of the repository, you should run command below to install `@grab/grabpay-merchant-sdk` as a dependency to your application using `npm`.

```shell
npm install <path-to-Node-folder>
```

Example: `npm install /lib/merchant-sdk/Node` (where `package.json` resides).

You can then import what you need using:

```typescript
import { MerchantIntegrationOnline, Utils } from '@grab/grabpay-merchant-sdk';
```

## Configuring the SDK

- `env` : String : either "PRD" for production or "STG" for staging
- `country` : String : [ISO 3166-1 Alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code of the merchant location.
- `partnerId` : String - Unique ID for a partner. Retrieve from Developer Home
- `partnerSecret` : String - Secret key for a partner. Retrieve from Developer Home
- `merchantId` : String - Retrieve from Developer Home
- `terminalId` : String - Retrieve from Developer Home **(POS only)**
- `clientId` : String - Retrieve from Developer Home **(OTC only)**
- `clientSecret` : String - Retrieve from Developer Home **(OTC only)**
- `redirectUrl` : String - The url configured in Developer Home **(OTC only)**

## One-Time Charge (OTC) Transactions

### Configuring for One-Time Charge

```ts
const clientOnline = new MerchantIntegrationOnline({
  env,
  country,
  partnerId,
  partnerSecret,
  merchantId,
  clientId,
  clientSecret,
  redirectUrl,
});
```

### One-Time Charge (OTC) API

1. Charge Init:

```ts
respChargeInit = clientOnline.chargeInit({
  partnerTxID,
  partnerGroupTxID,
  amount,
  currency,
  description,
  metaInfo,
  items,
  shippingDetails,
  hidePaymentMethods,
});
```

2. Generate Web Url:

```ts
respChargeInit = clientOnline.generateWebUrl({
  partnerTxID,
  partnerGroupTxID,
  amount,
  currency,
  description,
  metaInfo,
  items,
  shippingDetails,
  hidePaymentMethods,
  state,
  codeVerifier,
});
```

3. get OAuth Token

```ts
respAuthCode = clientOnline.oauth2Token({ code, codeVerifier });
```

4. Charge Complete

```ts
respChargeComplete = clientOnline.chargeComplete({ partnerTxID, accessToken });
```

5 Get Charge status

```ts
respChargeStatus = clientOnline.getChargeStatus({ partnerTxID, currency, accessToken });
```

6. refund online

```ts
response = clientOnline.refund({
  partnerTxID,
  amount,
  origPartnerTxID,
  description,
  accessToken,
  currency,
});
```

7. Get Refund Status

```js
response = clientOnline.getRefundStatus({ partnerTxID, accessToken, currency });
```

8. Get One time charge status

```ts
response = clientOnline.getOtcStatus({ partnerTxID, accessToken, currency });
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```ts
const clientOffline = new MerchantIntegrationOffline({
  env,
  country,
  partnerId,
  partnerSecret,
  merchantId,
  terminalId,
});
```

### Point of Sale (POS) API

1. Create QR Code

```ts
respCreateQRCode = clientOffline.createQrCode({ msgID, partnerTxID, amount, currency });
```

2. Get transaction detail:

```ts
resp = clientOffline.getTxnDetails({ partnerTxID, currency });
```

3. Cancel a Transaction

```ts
resp = clientOffline.cancel({ msgID, partnerTxID, origPartnerTxID, origTxID, currency });
```

4. Refund a POS Payment

```ts
resp = clientOffline.refund({ msgID, partnerTxID, amount, currency, origPartnerTxID, description });
```

5. Get refund transaction detail:

```ts
resp = clientOffline.getRefundDetails({ partnerTxID, currency });
```

6. Perform a Transaction

```ts
resp = clientOffline.performQrCode({ msgID, partnerTxID, amount, currency, code });
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
