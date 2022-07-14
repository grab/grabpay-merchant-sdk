<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOnline;
use GrabPay\Merchant\Models\Online\ChargeInitParams;
use GrabPay\Merchant\Models\Online\GenerateWebUrlParams;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Session Start
session_start();

// Redirect URL
$redirectUrl = 'http://localhost:8080/examples/online/otc_complete.php';

// Amount to charge
$amount = 10;

// Description of the charge
$description = 'Test OTC Payment';

// Init MerchantIntegrationOnline
$merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegrationOnline::STAGING, MerchantIntegrationOnline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, CLIENT_ID, CLIENT_SECRET, $redirectUrl);

// Session variables
$_SESSION['partnerTxID'] = MerchantIntegrationOnline::generateRandomString();
$_SESSION['partnerGroupTxID'] = MerchantIntegrationOnline::generateRandomString();
$_SESSION['codeVerifier'] = $merchantIntegrationOnline->generateRandomString(128);

// Init charge
$chargeInitParams = new ChargeInitParams([
    'partnerTxID'      => $_SESSION['partnerTxID'],
    'partnerGroupTxID' => $_SESSION['partnerGroupTxID'],
    'amount'           => $amount,
    'currency'         => MerchantIntegrationOnline::SGD,
    'description'      => $description,
]);
$chargeInit = $merchantIntegrationOnline->chargeInit($chargeInitParams);

// Get OAuth authorize url
$generateWebUrlParams = new GenerateWebUrlParams([
    'currency'     => MerchantIntegrationOnline::SGD,
    'codeVerifier' => $_SESSION['codeVerifier'],
    'request'      => $chargeInit->data->request,
]);
$generateWebUrl = $merchantIntegrationOnline->generateWebUrl($generateWebUrlParams);
?>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>One-Time Charge Init</title>
    </head>
    <body>
        <p>partnerTxID</p>
        <pre><?php print_r($_SESSION['partnerTxID']); ?></pre>
        <p>partnerGroupTxID</p>
        <pre><?php print_r($_SESSION['partnerGroupTxID']); ?></pre>
        <p>chargeInit</p>
        <pre><?php print_r($chargeInit->data); ?></pre>
        <p>generateWebUrl</p>
        <pre><?php print_r($generateWebUrl); ?></pre>
        <?php if (! empty($generateWebUrl)) { ?>
            <p><a href="<?php echo $generateWebUrl; ?>">Click here to complete OTC charge</a></p>
        <?php } ?>
    </body>
</html>
