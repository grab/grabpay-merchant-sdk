# GrabPay Merchant Integration SDK for PHP

## Introduction

`ONA` stands for `Online Acceptance` while `POS` stands for `Point-Of-Sale`.

We are refering `ONA` as `Online` and `POS` as `Offline`.

`ONA` has two types of API, `OTC` which stands for `One-Time Charge` and `Tokenization`.

`POS` has two types of QR code, `MPQR` which stands for `Merchant Present QR` and `CPQR` which stands for `Consumer Present QR`.

## Installation

1. Download and unzip [grab/grabpay-merchant-sdk](https://github.com/grab/grabpay-merchant-sdk/archive/refs/heads/main.zip).

```bash
# Download and unzip grabpay-merchant-sdk
curl --location https://github.com/grab/grabpay-merchant-sdk/archive/refs/heads/main.zip --output ./grabpay-merchant-sdk.zip

# Replace /path/to/project with the actual path to your project
unzip ./grabpay-merchant-sdk.zip /path/to/project
```

2. In your project `composer.json`, add the following lines and replace `/path/to/project` with the actual path to your project.

```bash
"repositories": [
    {
        "type": "path",
        "url": "/path/to/project/grabpay-merchant-sdk-main/Php",
        "options": {
            "symlink": false
        }
    }
],
```

3. Run the following command to include `grab/grabpay-merchant-sdk` to your project.

```bash
composer require grab/grabpay-merchant-sdk dev-main
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

#### Init

The Payment Initiate API allows a merchant to initiate both a Merchant Present QR (MPQR), and well as a Consumer Present QR payment (CPQR).

##### MPQR

```php
$params = new InitParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, // Indicating it is MPQR
        'partnerTxID'       => 'string', // Partner transaction ID
        'partnerGroupTxID'  => 'string', // Partner order ID
        'amount'            => int, // Transaction amount as integer
        'currency'          => MerchantIntegrationOffline::SGD, // Currency, here we are using SGD
        'paymentExpiryTime' => strtotime('+5 minutes'), // Payment expiry time
    ],
]);
$response =  $merchantIntegrationOffline->initiate($params);
```

##### CPQR

```php
$params = new InitParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR, // Indicating it is CPQR
        'partnerTxID'       => 'string', // Partner transaction ID
        'partnerGroupTxID'  => 'string', // Partner order ID
        'amount'            => int, // Transaction amount as integer
        'currency'          => MerchantIntegrationOffline::SGD, // Currency, here we are using SGD
        'paymentExpiryTime' => strtotime('+5 minutes'), // Payment expiry time
    ],
    'POSDetails' => [
        'consumerIdentifier' => $code, // Scanned QR code
    ],
]);
$response = $merchantIntegrationOffline->initiate($params);
```

#### Cancel

A cancellation request can be made using the Cancellation API in the following scenarios:

1. When a consumer decides to cancel a Grab payment at a self-service terminal.
2. When a cashier needs to cancel a Grab payment.
3. When a transaction is not completed before paymentExpiryTime.

```php
$params = new CancelParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, // Indicating it is MPQR or CPQR
        'originPartnerTxID' => 'string', // Partner transaction ID
        'currency'          => MerchantIntegrationOffline::SGD, // Currency, here we are using SGD
    ],
]);
$response = $merchantIntegrationOffline->cancel($params);
```

#### Refund

A refund request can be made for a payment transaction. The Refund API supports the following refunds:

1. Partial refund.
2. Full refund.

Merchants will need to provide the original payment partnerTxID in the originPartnerTxID parameter in the transactionDetails object in order to initiate a refund for a specific payment.

The refund validity is 90 days from the date of payment.

```php
$params = new RefundParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, // Indicating it is MPQR or CPQR
        'originPartnerTxID' => 'string', // The original partner transaction ID to refund
        'partnerTxID'       => 'string', // Partner transaction ID
        'partnerGroupTxID'  => 'string', // Partner order ID
        'amount'            => int, // Transaction amount as integer
        'currency'          => MerchantIntegrationOffline::SGD, // Currency, here we are using SGD
        'reason'            => 'string', // The reason for the refund
    ],
]);
$response = $merchantIntegrationOffline->refund($params);
```

#### Inquiry

The Inquiry API allows the merchant to perform the following checks:

1. When an ongoing transaction is in PENDING status, and merchant has yet to receive a terminal transaction status (SUCCESS / FAILURE).
2. When merchant needs to check the details of a historical transaction.

Merchants are to implement rate limiting when making an inquiry call.

Merchant will need to implement rate limiting feature, and restrict to a maximum of 50 API calls per second per Partner ID. This will help avoid unnecessary HTTP 429 response status.

When using the Inquiry API poll for PENDING transactions, merchants are advised to rate limit to 1 Inquiry API call per second per transaction.

```php
$params = new InquiryParams([
    'transactionDetails' => [
        'paymentChannel' => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR, // Indicating it is MPQR or CPQR
        'currency'       => MerchantIntegrationOffline::SGD, // Currency, here we are using SGD
        'txType'         => MerchantIntegrationOffline::TX_TYPE_PAYMENT, // Indicating whether it is a PAYMENT or REFUND inquiry
        'txRefType'      => MerchantIntegrationOffline::TX_REF_TYPE_PARTNERTXID, // Indicating whether the reference ID is GRABTXID or PARTNERTXID
        'txRefID'        => 'string', // Partner transaction ID if the above is PARTNERTXID
    ],
]);
$response = $merchantIntegrationOffline->inquiry($params);
```

## License

© 2022 GrabTaxi Holdings Pte Ltd. All rights reserved.
Any use of such assets are governed by the API Terms and Conditions (available at [Grab Developer](https://developer.grab.com/pages/terms-of-use)).
