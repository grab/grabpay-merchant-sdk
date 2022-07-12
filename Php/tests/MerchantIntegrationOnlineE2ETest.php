<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOnline;

/**
 * @internal
 */
final class MerchantIntegrationOnlineE2ETest extends MerchantIntegrationTest
{
    private MerchantIntegrationOnline $merchantIntegrationOnline;

    protected function setUp(): void
    {
        parent::setUp();

        $this->merchantIntegrationOnline = new merchantIntegrationOnline(MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_OTC_PARTNER_ID'], $_ENV['SG_STG_OTC_PARTNER_SECRET'], $_ENV['SG_STG_OTC_MERCHANT_ID'], $_ENV['SG_STG_OTC_CLIENT_ID'], $_ENV['SG_STG_OTC_CLIENT_SECRET'], self::REDIRECT_URL);
    }

    public function testOnaChargeInit(): void
    {
        $response = $this->merchantIntegrationOnline->onaChargeInit(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD, 'testing onaChargeInit');
        $onaChargeInit = $response->getBody();

        // Just to get the code coverage
        static::assertNotEmpty($response->getHeaders());

        static::assertSame(200, $response->getCode());
        static::assertNotEmpty($onaChargeInit->partnerTxID);
        static::assertNotEmpty($onaChargeInit->request);
    }

    public function testOnaCreateWebUrl(): void
    {
        $codeVerifier = MerchantIntegration::generateRandomString();
        $requestToken = MerchantIntegration::generateRandomString();

        $onaCreateWebUrl = $this->merchantIntegrationOnline->onaCreateWebUrl(MerchantIntegration::SGD, $codeVerifier, $requestToken);
        parse_str(parse_url($onaCreateWebUrl, PHP_URL_QUERY), $query);

        static::assertSame('consent_ctx:countryCode=' . MerchantIntegration::SG . ',currency=' . MerchantIntegration::SGD, $query['acr_values']);
        static::assertSame($_ENV['SG_STG_OTC_CLIENT_ID'], $query['client_id']);
        static::assertSame(MerchantIntegration::base64URLEncode(hash('sha256', $codeVerifier, true)), $query['code_challenge']);
        static::assertSame('S256', $query['code_challenge_method']);
        static::assertSame(self::REDIRECT_URL, $query['redirect_uri']);
        static::assertSame($requestToken, $query['request']);
        static::assertSame('code', $query['response_type']);
        static::assertSame('payment.one_time_charge', $query['scope']);
    }

    public function testOnaOAuth2Token(): void
    {
        $response = $this->merchantIntegrationOnline->onaOAuth2Token(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString());

        static::assertSame(400, $response->getCode());
    }

    public function testOnaChargeComplete(): void
    {
        $response = $this->merchantIntegrationOnline->onaChargeComplete(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString());

        static::assertSame(401, $response->getCode());
    }

    public function testOnaGetChargeStatus(): void
    {
        $response = $this->merchantIntegrationOnline->onaGetChargeStatus(MerchantIntegration::generateRandomString(), MerchantIntegrationOnline::SGD, MerchantIntegration::generateRandomString());

        static::assertSame(401, $response->getCode());
    }

    public function testOnaGetOTCStatus(): void
    {
        $response = $this->merchantIntegrationOnline->onaGetOTCStatus(MerchantIntegration::generateRandomString(), MerchantIntegrationOnline::SGD);

        static::assertSame(404, $response->getCode());
    }

    public function testOnaRefund(): void
    {
        $response = $this->merchantIntegrationOnline->onaRefund(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegrationOnline::SGD, MerchantIntegration::generateRandomString(), 'testing onaRefund', MerchantIntegration::generateRandomString());

        static::assertSame(401, $response->getCode());
    }

    public function testOnaGetRefundStatus(): void
    {
        $response = $this->merchantIntegrationOnline->onaGetRefundStatus(MerchantIntegration::generateRandomString(), MerchantIntegrationOnline::SGD, MerchantIntegration::generateRandomString());

        static::assertSame(401, $response->getCode());
    }
}
