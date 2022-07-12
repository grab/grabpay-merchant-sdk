# GrabPay Merchant Integration SDK for PHP

## Introduction

`ONA` stands for `Online Acceptance` while `POS` stands for `Point-Of-Sale`.

We are refering `ONA` as `Online` and `POS` as `Offline`.

`ONA` has two types of API, `OTC` which stands for `One-Time Charge` and `Tokenization`.

`POS` has two types of QR code, `MPQR` which stands for `Merchant Present QR` and `CPQR` which stands for `Consumer Present QR`.

## Installation

Include `grabpay-merchant-sdk` using composer.

```bash
composer require grab/grabpay-merchant-sdk
```

## Configuring the SDK

- `environment` - (string) either `stg` or `prd`.
- `country` - (string) [ISO 3166-1 Alpha-2](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) country code of the merchant location.
- `partnerID` - (string) Unique ID for a partner. Retrieved from Developer Home.
- `partnerSecret` - (string) Secret key for a partner. Retrieved from Developer Home.
- `merchantID` - (string) Retrieved from Developer Home.
- `terminalID` - (string) Retrieved from Developer Home **(POS only)**.
- `clientID` - (string) Retrieved from Developer Home **(ONA only)**.
- `clientSecret` - (string) Retrieved from Developer Home **(ONA only)**.
- `redirectUrl` - (string) The URL configured in Developer Home **(ONA only)**.

## ONA Transactions

### Configuring for ONA

```php
use GrabPay\Merchant\MerchantIntegrationOnline;

/**
 * @param string $environment Environment
 * @param string $country Country
 * @param string $partnerID Partner ID
 * @param string $partnerSecret Partner Secret
 * @param string $merchantID Merchant ID
 * @param string $clientID Client ID
 * @param string $clientSecret Client Secret
 * @param string $redirectUrl Redirect URL
 */
$merchantIntegrationOnline = new MerchantIntegrationOnline($environment, $country, $partnerID, $partnerSecret, $merchantID, $clientID, $clientSecret, $redirectUrl);
```

### OTC API

#### Initialise the charge

```php
/**
 * @param string $partnerTxID Partner transaction ID
 * @param string $partnerGroupTxID Partner order ID
 * @param int $amount Transaction amount as integer
 * @param string $currency Currency for the transaction
 * @param string $description Description of the charge
 * @param array $metaInfo Meta information regarding the transaction
 * @param array $items Items within the transaction
 * @param array $shippingDetails Shipping details for the transaction
 * @param array $hidePaymentMethods Payment method to hide for the transaction
 */
$response = $merchantIntegrationOnline->onaChargeInit($partnerTxID, $partnerGroupTxID, $amount, $currency, $description, $metaInfo, $items, $shippingDetails, $hidePaymentMethods);
```

#### Generate web URL for authentication

```php
/**
 * @param string $currency Currency for the transaction
 * @param string $codeVerifier Code verifier
 * @param string $requestToken Request token
 */
$response = $merchantIntegrationOnline->onaCreateWebUrl($currency, $codeVerifier, $requestToken);
```

#### Get oauth2 access token

```php
/**
 * @param string $code Code
 * @param string $codeVerifier Code verifier
 */
$response = $merchantIntegrationOnline->($code, $codeVerifier);
```

#### Complete the charge

```php
/**
 * @param string $partnerTxID Partner transaction ID
 * @param string $accessToken Access token
 */
$response = $merchantIntegrationOnline->onaChargeComplete($partnerTxID, $accessToken);
```

#### Get status of the transaction

```php
/**
 * @param string $partnerTxID Partner transaction ID
 * @param string $currency Currency for the transaction
 * @param string $accessToken Access token
 */
$response = $merchantIntegrationOnline-> onaGetChargeStatus($partnerTxID, $currency, $accessToken);
```

#### Refund transaction that has been charged

```php
/**
 * @param string $refundPartnerTxID Partner transaction ID to refund
 * @param string $partnerGroupTxID Partner order ID
 * @param int $amount Transaction amount as integer
 * @param string $currency Currency for the transaction
 * @param string $txID Transaction ID
 * @param string $description Description of the refund
 * @param string $accessToken Access token
 */
$response = $merchantIntegrationOnline->onaRefund($refundPartnerTxID, $partnerGroupTxID, $amount, $currency, $txID, $description, $accessToken);
```

#### Get status of the refunded transaction

```php
/**
 * @param string $refundPartnerTxID Partner transaction ID to refund
 * @param string $currency Currency for the transaction
 * @param string $accessToken Access token
 */
$response = $merchantIntegrationOnline->onaGetRefundStatus($refundPartnerTxID, $currency, $accessToken);
```

#### Get the status of the transaction without the need of an oauth access token

```php
/**
 * @param string $partnerTxID Partner transaction ID
 * @param string $currency Currency for the transaction
 */
$response = $merchantIntegrationOnline->onaGetOTCStatus($partnerTxID, $currency);
```

### Tokenization API

Coming soon.

## POS Transactions

### Configuring for POS

```php
use GrabPay\Merchant\MerchantIntegrationOffline;

/**
 * @param string $environment Environment
 * @param string $country Country
 * @param string $partnerID Partner ID
 * @param string $partnerSecret Partner Secret
 * @param string $merchantID Merchant ID
 * @param string $terminalID Terminal ID
 */
$merchantIntegrationOffline = new MerchantIntegrationOffline($environment, $country, $partnerID, $partnerSecret, $merchantID, $terminalID);
```

### POS API

#### Create MPQR code

```php
/**
 * @param string $msgID Message ID
 * @param string $partnerTxID Partner transaction ID
 * @param int $amount Transaction amount as integer
 * @param string $currency Currency for the transaction
 */
$response =  $merchantIntegrationOffline->posCreateQRCode($msgID, $partnerTxID, $amount, $currency);
```

#### If merchant is on CPQR code, use this to perform the transaction

```php
/**
 * @param string $msgID Message ID
 * @param string $partnerTxID Partner transaction ID
 * @param int $amount Transaction amount as integer
 * @param string $currency Currency for the transaction
 * @param string $code QR code
 */
$response = $merchantIntegrationOffline->posPerformQRCode($msgID, $partnerTxID, $amount, $currency, $code);
```

#### Cancel transaction that hasn't been processed

```php
/**
 * @param string $msgID Message ID
 * @param string $partnerTxID Partner transaction ID
 * @param string $origPartnerTxID Partner transaction ID to cancel
 * @param string $origTxID Original partner transaction ID
 * @param string $currency Currency Currency for the transaction
 */
$response = $merchantIntegrationOffline->posCancel($msgID, $partnerTxID, $origPartnerTxID, $origTxID, $currency);
```

#### Refund transaction that has been charged

```php
/**
 * @param string $msgID Message ID
 * @param string $refundPartnerTxID Refund transaction ID
 * @param int $amount Transaction amount as integer
 * @param string $currency Currency for the transaction
 * @param string $origPartnerTxID Original transaction ID to be refunded
 * @param string $description Description of the charge
 */
$response = $merchantIntegrationOffline->posRefund($msgID, $refundPartnerTxID, $amount, $currency, $origPartnerTxID, $description);
```

#### Get status of the transaction

```php
/**
 * @param string $msgID Message ID
 * @param string $partnerTxID Partner transaction ID
 * @param string $currency Currency for the transaction
 */
$response = $merchantIntegrationOffline->posGetTxnStatus($msgID, $partnerTxID, $currency);
```

#### Get status of the refunded transaction

```php
/**
 * @param string $msgID Message ID
 * @param string $refundPartnerTxID Refund transaction ID
 * @param string $currency Currency for the transaction
 */
$response = $merchantIntegrationOffline->posGetRefundStatus($msgID, $refundPartnerTxID, $currency);
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
