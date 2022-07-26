<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\CancelParams;
use GrabPay\Merchant\Models\Offline\InitiateParams;
use GrabPay\Merchant\Models\Offline\InquiryParams;
use GrabPay\Merchant\Models\Offline\RefundParams;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Init MerchantIntegrationOffline
$merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, TERMINAL_ID);

// Amount to charge
$amount = 10;

// Partner transaction ID
$partnerTxID = MerchantIntegrationOffline::generateRandomString();

// Partner group transaction ID
$partnerGroupTxID = MerchantIntegrationOffline::generateRandomString();

// Scanned QR Code
$code = '650000000000000000';

// Performs a payment transaction from a Customer Presented QR (CPQR) code
$initiateParams = new InitiateParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR,
        'partnerTxID'       => $partnerTxID,
        'partnerGroupTxID'  => $partnerGroupTxID,
        'amount'            => $amount,
        'currency'          => MerchantIntegrationOffline::SGD,
        'paymentExpiryTime' => strtotime('+5 minutes'),
    ],
    'POSDetails' => [
        'consumerIdentifier' => $code,
    ],
]);
$initiate = $merchantIntegrationOffline->initiate($initiateParams);

// Returns the payment transaction details
$inquiryParams = new InquiryParams([
    'transactionDetails' => [
        'paymentChannel' => MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR,
        'currency'       => MerchantIntegrationOffline::SGD,
        'txType'         => MerchantIntegrationOffline::TX_TYPE_PAYMENT,
        'txRefType'      => MerchantIntegrationOffline::TX_REF_TYPE_PARTNERTXID,
        'txRefID'        => $partnerTxID,
    ],
]);
$inquiry = $merchantIntegrationOffline->inquiry($inquiryParams);

// Cancels a pending payment
$cancelPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$initiateCancelParams = new InitiateParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
        'partnerTxID'       => $cancelPartnerTxID,
        'partnerGroupTxID'  => MerchantIntegrationOffline::generateRandomString(),
        'amount'            => $amount,
        'currency'          => MerchantIntegrationOffline::SGD,
        'paymentExpiryTime' => strtotime('+5 minutes'),
    ],
]);
$initiateCancel = $merchantIntegrationOffline->initiate($initiateCancelParams);
$cancelParams = new CancelParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
        'originPartnerTxID' => $cancelPartnerTxID,
        'currency'          => MerchantIntegrationOffline::SGD,
    ],
]);
$cancel = $merchantIntegrationOffline->cancel($cancelParams);

// Refunds a successful payment
$refundPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$refundReason = 'Test CPQR Refund Payment';
$refundTxnParams = new RefundParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_CPQR,
        'originPartnerTxID' => $partnerTxID,
        'partnerTxID'       => $refundPartnerTxID,
        'partnerGroupTxID'  => $partnerGroupTxID,
        'amount'            => $amount,
        'currency'          => MerchantIntegrationOffline::SGD,
        'reason'            => $refundReason,
    ],
]);
$refund = $merchantIntegrationOffline->refund($refundTxnParams);
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
    <p>initiate</p>
    <pre><?php print_r($initiate->data); ?></pre>
    <p>inquiry</p>
    <pre><?php print_r($inquiry->data); ?></pre>
    <p>cancelPartnerTxID</p>
    <pre><?php print_r($cancelPartnerTxID); ?></pre>
    <p>cancel</p>
    <pre><?php print_r($cancel->data); ?></pre>
    <p>refundPartnerTxID</p>
    <pre><?php print_r($refundPartnerTxID); ?></pre>
    <p>refund</p>
    <pre><?php print_r($refund->data); ?></pre>
</body>

</html>