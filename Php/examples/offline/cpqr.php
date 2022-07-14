<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\CancelTxnParams;
use GrabPay\Merchant\Models\Offline\GetTxnDetailsParams;
use GrabPay\Merchant\Models\Offline\PerformQrCodeTxnParams;
use GrabPay\Merchant\Models\Offline\RefundTxnParams;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Init MerchantIntegrationOffline
$merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, TERMINAL_ID);

// Amount to charge
$amount = 10;

// Partner transaction ID
$partnerTxID = MerchantIntegrationOffline::generateRandomString();

// Scanned QR Code
$code = '650000000000000000';

// Performs a payment transaction from a Customer Presented QR (CPQR) code
$performQrCodeTxnParams = new PerformQrCodeTxnParams([
    'amount'      => $amount,
    'code'        => $code,
    'currency'    => MerchantIntegrationOffline::SGD,
    'msgID'       => MerchantIntegrationOffline::generateRandomString(),
    'partnerTxID' => $partnerTxID,
]);
$performQrCode = $merchantIntegrationOffline->performQrCode($performQrCodeTxnParams);

// Returns the payment transaction details
$getTxnDetailsParams = new GetTxnDetailsParams([
    'currency'    => MerchantIntegrationOffline::SGD,
    'msgID'       => MerchantIntegrationOffline::generateRandomString(),
    'partnerTxID' => $partnerTxID,
]);
$getTxnDetails = $merchantIntegrationOffline->getTxnDetails($getTxnDetailsParams);

// Cancels a pending payment
$cancelPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$cancelTxnParams = new CancelTxnParams([
    'currency'        => MerchantIntegrationOffline::SGD,
    'msgID'           => MerchantIntegrationOffline::generateRandomString(),
    'origPartnerTxID' => $partnerTxID,
]);
$cancel = $merchantIntegrationOffline->cancel($cancelTxnParams);

// Refunds a successful payment
$refundPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$refundReason = 'Test CPQR Refund Payment';
$refundTxnParams = new RefundTxnParams([
    'amount'          => $amount,
    'currency'        => MerchantIntegrationOffline::SGD,
    'msgID'           => MerchantIntegrationOffline::generateRandomString(),
    'origPartnerTxID' => $partnerTxID,
    'partnerTxID'     => $refundPartnerTxID,
    'reason'          => $refundReason,
]);
$refund = $merchantIntegrationOffline->refund($refundTxnParams);

// Returns the refund transaction details
$getTxnDetailsParams = new GetTxnDetailsParams([
    'currency'    => MerchantIntegrationOffline::SGD,
    'msgID'       => MerchantIntegrationOffline::generateRandomString(),
    'partnerTxID' => $refundPartnerTxID,
]);
$getRefundDetails = $merchantIntegrationOffline->getRefundDetails($getTxnDetailsParams);
?>
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Consumer Present QR (CPQR) Code</title>
</head>

<body>
    <p>partnerTxID</p>
    <pre><?php print_r($partnerTxID); ?></pre>
    <p>performQrCode</p>
    <pre><?php print_r($performQrCode->data); ?></pre>
    <p>getTxnDetails</p>
    <pre><?php print_r($getTxnDetails->data); ?></pre>
    <p>cancelPartnerTxID</p>
    <pre><?php print_r($cancelPartnerTxID); ?></pre>
    <p>cancel</p>
    <pre><?php print_r($cancel->data); ?></pre>
    <p>refundPartnerTxID</p>
    <pre><?php print_r($refundPartnerTxID); ?></pre>
    <p>refund</p>
    <pre><?php print_r($refund->data); ?></pre>
    <p>getRefundDetails</p>
    <pre><?php print_r($getRefundDetails->data); ?></pre>
</body>

</html>