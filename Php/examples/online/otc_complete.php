<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOnline;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Session start
session_start();

// Redirect URL
$redirectUrl = 'http://localhost:8080/examples/online/otc_complete.php';

// Amount to refund
$amount = 168;

// Description of the refund
$description = 'Test OTC Refund Payment';

// Init MerchantIntegrationOnline
$merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegrationOnline::STAGING, MerchantIntegrationOnline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, CLIENT_ID, CLIENT_SECRET, $redirectUrl);

// Session variables
$_SESSION['refundPartnerTxID'] = MerchantIntegrationOnline::generateRandomString();

// Check for OAuth2 code
$onaOAuth2Token = null;
if (! empty($_REQUEST['code'])) {
    $onaOAuth2Token = $merchantIntegrationOnline->onaOAuth2Token($_REQUEST['code'], $_SESSION['codeVerifier']);
    $_SESSION['accessToken'] = $onaOAuth2Token->getBody()->access_token;
}

// Complete the charge
$completeCharge = null;
if (! empty($_SESSION['accessToken'])) {
    $onaChargeComplete = $merchantIntegrationOnline->onaChargeComplete($_SESSION['partnerTxID'], $_SESSION['accessToken']);
    $_SESSION['originTxID'] = $onaChargeComplete->getBody()->txID;
}

//  Get the status of the transaction with oauth access token
$onaGetChargeStatus = $merchantIntegrationOnline->onaGetChargeStatus($_SESSION['partnerTxID'], MerchantIntegrationOnline::SGD, $_SESSION['accessToken']);

//  Get the status of the transaction without the need of an oauth access token
$onaGetOTCStatus = $merchantIntegrationOnline->onaGetOTCStatus($_SESSION['partnerTxID'], MerchantIntegrationOnline::SGD);

//  Get the status of the transaction with oauth access token
$onaRefund = $merchantIntegrationOnline->onaRefund($_SESSION['refundPartnerTxID'], $_SESSION['partnerGroupTxID'], $amount, MerchantIntegrationOnline::SGD, $_SESSION['originTxID'], $description, $_SESSION['accessToken']);

//  Get the status of the transaction without the need of an oauth access token
$onaGetRefundStatus = $merchantIntegrationOnline->onaGetRefundStatus($_SESSION['refundPartnerTxID'], MerchantIntegrationOnline::SGD, $_SESSION['accessToken']);
?>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>One-Time Charge Complete</title>
    </head>
    <body>
        <p>onaOAuth2Token</p>
        <pre><?php print_r($onaOAuth2Token->getBody()); ?></pre>
        <p>accessToken</p>
        <pre><?php print_r($_SESSION['accessToken']); ?></pre>
        <p>onaChargeComplete</p>
        <pre><?php print_r($onaChargeComplete->getBody()); ?></pre>
        <p>originTxID</p>
        <pre><?php print_r($_SESSION['originTxID']); ?></pre>
        <p>onaGetChargeStatus</p>
        <pre><?php print_r($onaGetChargeStatus->getBody()); ?></pre>
        <p>onaGetOTCStatus</p>
        <pre><?php print_r($onaGetOTCStatus->getBody()); ?></pre>
        <p>refundPartnerTxID</p>
        <pre><?php print_r($_SESSION['refundPartnerTxID']); ?></pre>
        <p>onaRefund</p>
        <pre><?php print_r($onaRefund->getBody()); ?></pre>
        <p>onaGetRefundStatus</p>
        <pre><?php print_r($onaGetRefundStatus->getBody()); ?></pre>
    </body>
</html>
