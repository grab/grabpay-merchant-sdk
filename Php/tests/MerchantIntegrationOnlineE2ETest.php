<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOnline;
use GrabPay\Merchant\Models\Online\ChargeCompleteParams;
use GrabPay\Merchant\Models\Online\ChargeInitParams;
use GrabPay\Merchant\Models\Online\GenerateWebUrlParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusParams;
use GrabPay\Merchant\Models\Online\GetOtcStatusParams;
use GrabPay\Merchant\Models\Online\GetRefundStatusParams;
use GrabPay\Merchant\Models\Online\Oauth2TokenParams;
use GrabPay\Merchant\Models\Online\RefundParams;

/**
 * @internal
 * @coversNothing
 */
final class MerchantIntegrationOnlineE2ETest extends MerchantIntegrationTest
{
    private MerchantIntegrationOnline $merchantIntegrationOnline;

    protected function setUp(): void
    {
        parent::setUp();

        $this->merchantIntegrationOnline = new merchantIntegrationOnline(MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_OTC_PARTNER_ID'], $_ENV['SG_STG_OTC_PARTNER_SECRET'], $_ENV['SG_STG_OTC_MERCHANT_ID'], $_ENV['SG_STG_OTC_CLIENT_ID'], $_ENV['SG_STG_OTC_CLIENT_SECRET'], self::REDIRECT_URL);
    }

    public function testChargeComplete(): void
    {
        $chargeCompleteParams = new ChargeCompleteParams([
            'accessToken' => MerchantIntegration::generateRandomString(),
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->chargeComplete($chargeCompleteParams);

        static::assertSame(401, $response->status);
    }

    public function testChargeInit(): void
    {
        $chargeInitParams = new ChargeInitParams([
            'amount'           => self::AMOUNT,
            'currency'         => MerchantIntegrationOnline::SGD,
            'description'      => 'testing chargeInit',
            'partnerGroupTxID' => MerchantIntegration::generateRandomString(),
            'partnerTxID'      => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->chargeInit($chargeInitParams);
        $chargeInit = $response->data;

        static::assertSame(200, $response->status);
        static::assertNotEmpty($chargeInit->partnerTxID);
        static::assertNotEmpty($chargeInit->request);
    }

    public function testGenerateWebUrl(): void
    {
        $codeVerifier = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();

        $generateWebUrlParams = new GenerateWebUrlParams([
            'currency'     => MerchantIntegration::SGD,
            'codeVerifier' => $codeVerifier,
            'request'      => $request,
        ]);
        $onaCreateWebUrl = $this->merchantIntegrationOnline->generateWebUrl($generateWebUrlParams);
        parse_str(parse_url($onaCreateWebUrl, PHP_URL_QUERY), $query);

        static::assertSame('consent_ctx:countryCode=' . MerchantIntegration::SG . ',currency=' . MerchantIntegration::SGD, $query['acr_values']);
        static::assertSame($_ENV['SG_STG_OTC_CLIENT_ID'], $query['client_id']);
        static::assertSame(MerchantIntegration::base64URLEncode(hash('sha256', $codeVerifier, true)), $query['code_challenge']);
        static::assertSame('S256', $query['code_challenge_method']);
        static::assertSame(self::REDIRECT_URL, $query['redirect_uri']);
        static::assertSame($request, $query['request']);
        static::assertSame('code', $query['response_type']);
        static::assertSame('payment.one_time_charge', $query['scope']);
    }

    public function testGetChargeStatus(): void
    {
        $getChargeStatusParams = new GetChargeStatusParams([
            'accessToken' => MerchantIntegration::generateRandomString(),
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->getChargeStatus($getChargeStatusParams);

        static::assertSame(401, $response->status);
    }

    public function testGetOtcStatus(): void
    {
        $getOtcStatusParams = new GetOtcStatusParams([
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->getOtcStatus($getOtcStatusParams);

        static::assertSame(404, $response->status);
    }

    public function testGetRefundStatus(): void
    {
        $getRefundStatusParams = new GetRefundStatusParams([
            'accessToken' => MerchantIntegration::generateRandomString(),
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->getRefundStatus($getRefundStatusParams);

        static::assertSame(401, $response->status);
    }

    public function testOauth2Token(): void
    {
        $oauth2TokenParams = new Oauth2TokenParams([
            'code'         => MerchantIntegration::generateRandomString(),
            'codeVerifier' => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->oauth2Token($oauth2TokenParams);

        static::assertSame(400, $response->status);
    }

    public function testRefund(): void
    {
        $refundParams = new RefundParams([
            'accessToken'      => MerchantIntegration::generateRandomString(),
            'amount'           => self::AMOUNT,
            'currency'         => MerchantIntegrationOnline::SGD,
            'description'      => 'testing refund',
            'originTxID'       => MerchantIntegration::generateRandomString(),
            'partnerGroupTxID' => MerchantIntegration::generateRandomString(),
            'partnerTxID'      => MerchantIntegration::generateRandomString(),
        ]);
        $response = $this->merchantIntegrationOnline->refund($refundParams);

        static::assertSame(401, $response->status);
    }
}
