<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOnline;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Session Start
session_start();

// Redirect URL
$redirectUrl = 'http://localhost:8080/examples/online/otc_complete.php';

// Amount to charge
$amount = 168;

// Description of the charge
$description = 'Test OTC Payment';

// Init MerchantIntegrationOnline
$merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegrationOnline::STAGING, MerchantIntegrationOnline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, CLIENT_ID, CLIENT_SECRET, $redirectUrl);

// Session variables
$_SESSION['partnerTxID'] = MerchantIntegrationOnline::generateRandomString();
$_SESSION['partnerGroupTxID'] = MerchantIntegrationOnline::generateRandomString();
$_SESSION['codeVerifier'] = $merchantIntegrationOnline->generateRandomString(128);

// Init charge
$onaChargeInit = $merchantIntegrationOnline->onaChargeInit($_SESSION['partnerTxID'], $_SESSION['partnerGroupTxID'], $amount, MerchantIntegrationOnline::SGD, $description);

// Get OAuth authorize url
$onaCreateWebUrl = $merchantIntegrationOnline->onaCreateWebUrl(MerchantIntegrationOnline::SGD, $_SESSION['codeVerifier'], $onaChargeInit->getBody()->request);
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
        <p>onaChargeInit</p>
        <pre><?php print_r($onaChargeInit->getBody()); ?></pre>
        <p>onaCreateWebUrl</p>
        <pre><?php print_r($onaCreateWebUrl); ?></pre>
        <?php if (! empty($onaCreateWebUrl)) { ?>
            <p><a href="<?php echo $onaCreateWebUrl; ?>">Click here to complete OTC charge</a></p>
        <?php } ?>
    </body>
</html>
