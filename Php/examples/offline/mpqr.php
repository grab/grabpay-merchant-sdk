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

// Partner transaction ID
$partnerTxID = MerchantIntegrationOffline::generateRandomString();

// Creates a payment order from a Merchant Presented QR (MPQR) code.
$posCreateQRCode = $merchantIntegrationOffline->posCreateQRCode(MerchantIntegrationOffline::generateRandomString(), $partnerTxID, $amount, MerchantIntegrationOffline::SGD);

// Display QR Code using Google Charts
$googleChartQrCodeImgUrl = '';
if (!empty($posCreateQRCode->getBody()->qrcode)) {
    $googleChartQrCodeImgUrl = 'https://chart.googleapis.com/chart?' . http_build_query([
        'cht' => 'qr',
        'chs' => '300x300',
        'chl' => $posCreateQRCode->getBody()->qrcode ?? '',
    ]);
}
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
    <p>posCreateQRCode</p>
    <img src="<?php echo $googleChartQrCodeImgUrl; ?>" title="Merchant Present QR (MPQR) Code" />
    <pre><?php print_r($posCreateQRCode->getBody()); ?></pre>
</body>

</html>