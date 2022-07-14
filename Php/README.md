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
$params = new ChargeInitParams([
    'amount'             => int, //  Transaction amount as integer
    'currency'           => 'string', // Currency for the transaction
    'description'        => 'string', // Description of the charge
    'hidePaymentMethods' => array, // Payment method to hide for the transaction
    'items'              => array, // Items within the transaction
    'metaInfo'           => array, // Meta information regarding the transaction
    'partnerGroupTxID'   => 'string', // Partner order ID
    'partnerTxID'        => 'string', // Partner transaction ID
    'shippingDetails'    => array, // Shipping details for the transaction
]);
$response = $merchantIntegrationOnline->chargeInit($params);
```

#### Generate web URL for authentication

```php
$params = new GenerateWebUrlParams([
    'codeVerifier' => 'string', // Code verifier
    'currency'     => 'string', // Currency for the transaction
    'request'      => 'string', // Request token
]);
$response = $merchantIntegrationOnline->generateWebUrl($params);
```

#### Get oauth2 access token

```php
$params = new Oauth2TokenParams([
    'code'         => 'string', // Code
    'codeVerifier' => 'string', // Code verifier
]);
$response = $merchantIntegrationOnline->oauth2Token($params);
```

#### Complete the charge

```php
$params = new ChargeCompleteParams([
    'accessToken' => 'string', // Access token
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOnline->chargeComplete($params);
```

#### Get status of the transaction

```php
$params = new GetChargeStatusParams([
    'accessToken' => 'string', // Access token
    'currency'    => 'string', // Currency for the transaction
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOnline->getChargeStatus($params);
```

#### Get the status of the transaction without the need of an oauth access token

```php
$params = new GetOtcStatusParams([
    'currency'    => 'string', // Currency for the transaction
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOnline->getOtcStatus($params);
```

#### Refund transaction that has been charged

```php
$params = new RefundParams([
    'accessToken'      => 'string', // Access token
    'amount'           => int, // Refunded amount as integer
    'currency'         => 'string', // Currency for the transaction
    'description'      => 'string', // Description of the refund
    'originTxID'       => 'string', // GrabPay original Transaction ID
    'partnerGroupTxID' => 'string', // Partner order ID
    'partnerTxID'      => 'string', // Partner transaction ID to refund
]);
$response = $merchantIntegrationOnline->refund($params);
```

#### Get status of the refunded transaction

```php
$params = new GetRefundStatusParams([
    'accessToken' => 'string', // Access token
    'currency'    => 'string', // Currency for the refunded transaction
    'partnerTxID' => 'string', // Partner transaction ID to refund
]);
$response = $merchantIntegrationOnline->getRefundStatus($params)
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
$params = new CreateQrCodeParams([
    'amount'      => int, // Transaction amount as integer
    'currency'    => 'string', // Currency for the transaction
    'msgID'       => 'string', // Message ID
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response =  $merchantIntegrationOffline->createQrCode($params);
```

#### If merchant is on CPQR code, use this to perform the transaction

```php
$params = new PerformQrCodeTxnParams([
    'amount'      => int, // Transaction amount as integer
    'code'        => 'string', // Scanned QR code
    'currency'    => 'string', // Currency for the transaction
    'msgID'       => 'string', // Message ID
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOffline->performQrCode($params);
```

#### Cancel transaction that hasn't been processed

```php
$params = new CancelTxnParams([
    'currency'        => 'string', // Currency Currency for the transaction
    'msgID'           => 'string', // Message ID
    'origPartnerTxID' => 'string', // Original partner transaction ID
]);
$response = $merchantIntegrationOffline->cancel($params);
```

#### Refund transaction that has been charged

```php
$params = new RefundTxnParams([
    'amount'          => int, // Refunded amount as integer
    'currency'        => 'string', // Currency for the refunded transaction
    'msgID'           => 'string', // Message ID
    'origPartnerTxID' => 'string', // Original partner transaction ID to be refunded
    'partnerTxID'     => 'string', // Partner transaction ID to be refunded
    'reason'          => 'string', // Description of the refund
]);
$response = $merchantIntegrationOffline->refund($params);
```

#### Get status of the transaction

```php
$params = new GetTxnDetailsParams([
    'currency'    => 'string', // Currency for the transaction
    'msgID'       => 'string', // Message ID
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOffline->getTxnDetails($params);
```

#### Get status of the refunded transaction

```php
$params = new GetTxnDetailsParams([
    'currency'    => 'string', // Currency for the transaction
    'msgID'       => 'string', // Message ID
    'partnerTxID' => 'string', // Partner transaction ID
]);
$response = $merchantIntegrationOffline->getRefundDetails($params);
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
