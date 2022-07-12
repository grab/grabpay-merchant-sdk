<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOffline;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Init MerchantIntegrationOffline
$merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, TERMINAL_ID);

// Amount to charge
$amount = 10;

// Description of the refund
$refundDescription = 'Test CPQR Refund Payment';

// Partner transaction ID
$partnerTxID = MerchantIntegrationOffline::generateRandomString();

// Scanned QR Code
$qrCode = '650000000000000000';

// Performs a payment transaction from a Customer Presented QR (CPQR) code
$posPerformQRCode = $merchantIntegrationOffline->posPerformQRCode(MerchantIntegrationOffline::generateRandomString(), $partnerTxID, $amount, MerchantIntegrationOffline::SGD, $qrCode);

// Returns the payment transaction details
$posGetTxnStatus = $merchantIntegrationOffline->posGetTxnStatus(MerchantIntegrationOffline::generateRandomString(), $partnerTxID, MerchantIntegrationOffline::SGD);

// Cancels a pending payment
$cancelPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$posCancel = $merchantIntegrationOffline->posCancel(MerchantIntegrationOffline::generateRandomString(), $cancelPartnerTxID, $partnerTxID, $posPerformQRCode->getBody()->txID, MerchantIntegrationOffline::SGD);

// Refunds a successful payment
$refundPartnerTxID = MerchantIntegrationOffline::generateRandomString();
$posRefund = $merchantIntegrationOffline->posRefund(MerchantIntegrationOffline::generateRandomString(), $refundPartnerTxID, $amount, MerchantIntegrationOffline::SGD, $partnerTxID, $refundDescription);

// Returns the refund transaction details
$posGetRefundStatus = $merchantIntegrationOffline->posGetRefundStatus(MerchantIntegrationOffline::generateRandomString(), $refundPartnerTxID, MerchantIntegrationOffline::SGD);
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
    <p>posPerformQRCode</p>
    <pre><?php print_r($posPerformQRCode->getBody()); ?></pre>
    <p>posGetTxnStatus</p>
    <pre><?php print_r($posGetTxnStatus->getBody()); ?></pre>
    <p>cancelPartnerTxID</p>
    <pre><?php print_r($cancelPartnerTxID); ?></pre>
    <p>posCancel</p>
    <pre><?php print_r($posCancel->getBody()); ?></pre>
    <p>refundPartnerTxID</p>
    <pre><?php print_r($refundPartnerTxID); ?></pre>
    <p>posRefund</p>
    <pre><?php print_r($posRefund->getBody()); ?></pre>
    <p>posGetRefundStatus</p>
    <pre><?php print_r($posGetRefundStatus->getBody()); ?></pre>
</body>

</html>