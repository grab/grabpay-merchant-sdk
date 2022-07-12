# GrabPay Merchant Integration SDK for Python

## Using the SDK

For using this SDK, you might copy all files in [src](./src) folder and paste it to your project. Then you should import 2 classes `MerchantIntegrationOnline`, `MerchantIntegrationOffline` in `src` folder.

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

```python
from MerchantIntegrationOnline import MerchantIntegrationOnline

callOna = MerchantIntegrationOnline(stage, country, partner_id, partner_secret, merchant_id, client_id, client_secret, redirect_url)
```

### One-Time Charge (OTC) API

1. Charge Init:

```python
respChargeInit = callOna.onaChargeInit(
        partnerTxID,
        partnerGroupTxID,
        amount,
        currency,
        description,
        metaInfo={},
        items={},
        shippingDetails={},
        hidePaymentMethods=[]
)
```

2. Create Web Url:

```python
respChargeInit = callOna.onaCreateWebUrl(
    partnerTxID,
    partnerGroupTxID,
    amount,
    currency,
    description,
    codeVerifier,
    metaInfo={},
    items={},
    shippingDetails={},
    hidePaymentMethods=[],
    state=None
)
```

3. get OAuth Token

```python
respAuthCode = callOna.onaOAuthToken(code, codeVerifier)
```

4. Charge Complete

```python
respChargeComplete = callOna.onaChargeComplete(partnerTxID, respAuthCode.access_token)
```

5 Get Charge status

```python
respChargeStatus = callOna.onaGetChargeStatus(partnerTxID, currency, respAuthCode.accessToken)
```

6. refund online

```python
response = callOna.onaRefund(refundPartnerTxID, amount, origPartnerTxID, description, respAuthCode.accessToken, currency)
```

7. Get Refund Status

```python
response = callOna.onaGetRefundStatus(partnerTxID, respAuthCode.accessToken, currency)
```

8. Get One time charge status

```python
    response = callOna.onaGetOtcStatus(partnerTxID, respAuthCode.accessToken, currency)
```

## Point of Sale (POS) Transactions

### Configuring for Point of Sale

```python
from MerchantIntegrationOffline import MerchantIntegrationOffline

callPos = MerchantIntegrationOffline(stage, country, partner_id, partner_secret, merchant_id, terminal_id)
```

### Point of Sale (POS) API

1. Create QR Code

```python
respCreateQRCode = callPos.posCreateQRCode(msgID, partnerTxID, amount, currency)
```

2. Get transaction status:

```python
resp = callPos.posGetTxnStatus(msgID, partnerTxID, currency)
```

3. cancelTxn

```python
resp = callPos.posCancel(msgID, partnerTxID, origPartnerTxID, origTxID, currency)
```

4. refund Pos transaction

```python
resp = callPos.posRefund(msgID, partnerTxID, originTxID, amount, description, currency)
```

5. Get refund transaction status:

```python
resp = callPos.posGetRefundStatus(msgID, partnerTxID, currency)
```

6. performTxn

```python
resp = callPos.posPerformQRCode(msgID, partnerTxID, amount, code, currency)
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
