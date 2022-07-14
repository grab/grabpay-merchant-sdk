<?php

declare(strict_types=1);

use GrabPay\Merchant\MerchantIntegrationOnline;
use GrabPay\Merchant\Models\Online\ChargeCompleteParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusParams;
use GrabPay\Merchant\Models\Online\GetOtcStatusParams;
use GrabPay\Merchant\Models\Online\GetRefundStatusParams;
use GrabPay\Merchant\Models\Online\Oauth2TokenParams;
use GrabPay\Merchant\Models\Online\RefundParams;

// Requires
include_once '../config.php';

include_once '../../vendor/autoload.php';

// Session start
session_start();

// Redirect URL
$redirectUrl = 'http://localhost:8080/examples/online/otc_complete.php';

// Amount to refund
$amount = 10;

// Description of the refund
$description = 'Test OTC Refund Payment';

// Init MerchantIntegrationOnline
$merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegrationOnline::STAGING, MerchantIntegrationOnline::SG, PARTNER_ID, PARTNER_SECRET, MERCHANT_ID, CLIENT_ID, CLIENT_SECRET, $redirectUrl);

// Session variables
$_SESSION['refundPartnerTxID'] = MerchantIntegrationOnline::generateRandomString();

// Check for OAuth2 code
$oauth2Token = null;
if (! empty($_REQUEST['code'])) {
    $oauth2TokenParams = new Oauth2TokenParams([
        'code'         => $_REQUEST['code'],
        'codeVerifier' => $_SESSION['codeVerifier'],
    ]);
    $oauth2Token = $merchantIntegrationOnline->oauth2Token($oauth2TokenParams);
    $_SESSION['accessToken'] = $oauth2Token->data->access_token;
}

// Complete the charge
$chargeComplete = null;
if (! empty($_SESSION['accessToken'])) {
    $chargeCompleteParams = new ChargeCompleteParams([
        'partnerTxID' => $_SESSION['partnerTxID'],
        'accessToken' => $_SESSION['accessToken'],
    ]);
    $chargeComplete = $merchantIntegrationOnline->chargeComplete($chargeCompleteParams);
    $_SESSION['originTxID'] = $chargeComplete->data->txID;
}

//  Get the status of the transaction with oauth access token
$getChargeStatusParams = new GetChargeStatusParams([
    'partnerTxID' => $_SESSION['partnerTxID'],
    'currency'    => MerchantIntegrationOnline::SGD,
    'accessToken' => $_SESSION['accessToken'],
]);
$getChargeStatus = $merchantIntegrationOnline->getChargeStatus($getChargeStatusParams);

//  Get the status of the transaction without the need of an oauth access token
$getOtcStatusParams = new GetOtcStatusParams([
    'partnerTxID' => $_SESSION['partnerTxID'],
    'currency'    => MerchantIntegrationOnline::SGD,
]);
$getOtcStatus = $merchantIntegrationOnline->getOtcStatus($getOtcStatusParams);

// Refund the transaction
$refundParams = new RefundParams([
    'partnerTxID'      => $_SESSION['refundPartnerTxID'],
    'partnerGroupTxID' => $_SESSION['partnerGroupTxID'],
    'amount'           => $amount,
    'currency'         => MerchantIntegrationOnline::SGD,
    'originTxID'       => $_SESSION['originTxID'],
    'description'      => $description,
    'accessToken'      => $_SESSION['accessToken'],
]);
$refund = $merchantIntegrationOnline->refund($refundParams);

// Get the status of the refund transaction
$getRefundStatusParams = new GetRefundStatusParams([
    'partnerTxID' => $_SESSION['refundPartnerTxID'],
    'currency'    => MerchantIntegrationOnline::SGD,
    'accessToken' => $_SESSION['accessToken'],
]);
$getRefundStatus = $merchantIntegrationOnline->getRefundStatus($getRefundStatusParams);
?>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>One-Time Charge Complete</title>
    </head>
    <body>
        <p>oauth2Token</p>
        <pre><?php print_r($oauth2Token->data); ?></pre>
        <p>accessToken</p>
        <pre><?php print_r($_SESSION['accessToken']); ?></pre>
        <p>chargeComplete</p>
        <pre><?php print_r($chargeComplete->data); ?></pre>
        <p>originTxID</p>
        <pre><?php print_r($_SESSION['originTxID']); ?></pre>
        <p>getChargeStatus</p>
        <pre><?php print_r($getChargeStatus->data); ?></pre>
        <p>getOtcStatus</p>
        <pre><?php print_r($getOtcStatus->data); ?></pre>
        <p>refundPartnerTxID</p>
        <pre><?php print_r($_SESSION['refundPartnerTxID']); ?></pre>
        <p>refund</p>
        <pre><?php print_r($refund->data); ?></pre>
        <p>getRefundStatus</p>
        <pre><?php print_r($getRefundStatus->data); ?></pre>
    </body>
</html>
