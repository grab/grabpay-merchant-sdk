<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOffline;
use GrabPay\Merchant\Models\Offline\InitiateParams;
use GrabPay\Merchant\Models\Offline\InquiryParams;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Init MerchantIntegrationOffline
$merchantIntegrationOffline = new MerchantIntegrationOffline(MerchantIntegrationOffline::STAGING, MerchantIntegrationOffline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, TERMINAL_ID);

// Amount to charge
$amount = 10;

// Partner transaction ID
$partnerTxID = MerchantIntegrationOffline::generateRandomString();

// Creates a payment order from a Merchant Presented QR (MPQR) code.
$initiateParams = new InitiateParams([
    'transactionDetails' => [
        'paymentChannel'    => MerchantIntegrationOffline::PAYMENT_CHANNEL_MPQR,
        'partnerTxID'       => $partnerTxID,
        'partnerGroupTxID'  => MerchantIntegrationOffline::generateRandomString(),
        'amount'            => $amount,
        'currency'          => MerchantIntegrationOffline::SGD,
        'paymentExpiryTime' => strtotime('+5 minutes'),
    ],
]);
$initiate = $merchantIntegrationOffline->initiate($initiateParams);

// Display QR Code using Google Charts
$googleChartQrCodeImgUrl = '';
if (! empty($initiate->data->POSDetails->qrPayload)) {
    $googleChartQrCodeImgUrl = 'https://chart.googleapis.com/chart?' . http_build_query([
        'chl' => $initiate->data->POSDetails->qrPayload ?? '',
        'chs' => '300x300',
        'cht' => 'qr',
    ]);
}

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
?>
<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Merchant Present QR (MPQR) Code</title>
</head>

<body>
    <p>partnerTxID</p>
    <pre><?php print_r($partnerTxID); ?></pre>
    <p>initiate</p>
    <img src="<?php echo $googleChartQrCodeImgUrl; ?>" title="Merchant Present QR (MPQR) Code" />
    <pre><?php print_r($initiate->data); ?></pre>
    <p>inquiry</p>
    <pre><?php print_r($inquiry->data); ?></pre>
</body>

</html>