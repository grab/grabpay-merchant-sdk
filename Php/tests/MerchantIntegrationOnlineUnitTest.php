<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOnline;
use GrabPay\Merchant\Response;

/**
 * @internal
 */
final class MerchantIntegrationOnlineUnitTest extends MerchantIntegrationTest
{
    private MerchantIntegrationOnline $merchantIntegrationOnline;
    private MerchantIntegrationOnline $merchantIntegrationOnlineException;

    protected function setUp(): void
    {
        parent::setUp();

        $mockBuilder = $this->getMockBuilder(MerchantIntegrationOnline::class)
            ->setConstructorArgs([MerchantIntegration::STAGING, MerchantIntegration::SG, $_ENV['SG_STG_OTC_PARTNER_ID'], $_ENV['SG_STG_OTC_PARTNER_SECRET'], $_ENV['SG_STG_OTC_MERCHANT_ID'], $_ENV['SG_STG_OTC_CLIENT_ID'], $_ENV['SG_STG_OTC_CLIENT_SECRET'], self::REDIRECT_URL])
            ->disableOriginalClone()
            ->disableArgumentCloning()
            ->disallowMockingUnknownTypes()
        ;

        $this->merchantIntegrationOnline = $mockBuilder
            ->onlyMethods(['sendGetRequest', 'sendPostRequest', 'sendPutRequest'])
            ->getMock()
        ;

        $this->merchantIntegrationOnlineException = $mockBuilder
            ->onlyMethods(['getPartnerInfo'])
            ->getMock()
        ;
    }

    public function testOnaChargeInit(): void
    {
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();

        $response = (object) [
            'partnerTxID' => $partnerTxID,
            'request'     => $request,
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $onaChargeInit = $this->merchantIntegrationOnline->onaChargeInit($partnerTxID, $partnerGroupTxID, self::AMOUNT, MerchantIntegration::SGD, 'testing onaChargeInit');

        static::assertSame($response, $onaChargeInit->getBody());
    }

    public function testOnaChargeInitFull(): void
    {
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();
        $hidePaymentMethods = ['CARD'];
        $metaInfo = ['key' => 'value'];
        $items = ['itemName' => 'itemNameValue'];
        $shippingDetails = ['firstName' => 'firstNameValue'];

        $response = (object) [
            'partnerTxID'        => $partnerTxID,
            'request'            => $request,
            'hidePaymentMethods' => $hidePaymentMethods,
            'metaInfo'           => $metaInfo,
            'items'              => $items,
            'shippingDetails'    => $shippingDetails,
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $onaChargeInit = $this->merchantIntegrationOnline->onaChargeInit($partnerTxID, $partnerGroupTxID, self::AMOUNT, MerchantIntegration::SGD, 'testing onaChargeInit', $metaInfo, $items, $shippingDetails, $hidePaymentMethods);

        static::assertSame($response, $onaChargeInit->getBody());
    }

    public function testOnaChargeInitException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaChargeInit = $this->merchantIntegrationOnlineException->onaChargeInit(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD, 'testing onaChargeInit');

        static::assertSame('Caught exception: ', $onaChargeInit->getBody());
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
        $code = MerchantIntegration::generateRandomString();
        $codeVerifier = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'access_token' => $accessToken,
            'token_type'   => 'Bearer',
            'expires_in'   => 31535999,
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $onaOAuth2Token = $this->merchantIntegrationOnline->onaOAuth2Token($code, $codeVerifier);

        static::assertSame($response, $onaOAuth2Token->getBody());
    }

    public function testOnaOAuth2TokenException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaOAuth2Token = $this->merchantIntegrationOnlineException->onaOAuth2Token(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString());

        static::assertSame('Caught exception: ', $onaOAuth2Token->getBody());
    }

    public function testOnaChargeComplete(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'         => $msgID,
            'txID'          => $txID,
            'status'        => 'success',
            'description'   => '',
            'paymentMethod' => 'GPWALLET',
            'txStatus'      => 'success',
            'reason'        => '',
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $onaChargeComplete = $this->merchantIntegrationOnline->onaChargeComplete($partnerTxID, $accessToken);

        static::assertSame($response, $onaChargeComplete->getBody());
    }

    public function testOnaChargeCompleteException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaChargeComplete = $this->merchantIntegrationOnlineException->onaChargeComplete(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString());

        static::assertSame('Caught exception: ', $onaChargeComplete->getBody());
    }

    public function testOnaGetChargeStatus(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'         => $msgID,
            'txID'          => $txID,
            'status'        => 'success',
            'description'   => '',
            'paymentMethod' => 'GPWALLET',
            'txStatus'      => 'success',
            'reason'        => '',
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new Response(200, [], $response));
        $onaGetChargeStatus = $this->merchantIntegrationOnline->onaGetChargeStatus($partnerTxID, MerchantIntegration::SGD, $accessToken);

        static::assertSame($response, $onaGetChargeStatus->getBody());
    }

    public function testOnaGetChargeStatusException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaGetChargeStatus = $this->merchantIntegrationOnlineException->onaGetChargeStatus(MerchantIntegration::generateRandomString(), MerchantIntegration::SGD, MerchantIntegration::generateRandomString());

        static::assertSame('Caught exception: ', $onaGetChargeStatus->getBody());
    }

    public function testOnaGetOTCStatus(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'txID'          => $txID,
            'oAuthCode'     => '',
            'status'        => 'success',
            'paymentMethod' => 'GPWALLET',
            'txStatus'      => 'success',
            'reason'        => '',
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new Response(200, [], $response));
        $onaGetOTCStatus = $this->merchantIntegrationOnline->onaGetOTCStatus($partnerTxID, MerchantIntegration::SGD);

        static::assertSame($response, $onaGetOTCStatus->getBody());
    }

    public function testOnaGetOTCStatusException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaGetOTCStatus = $this->merchantIntegrationOnlineException->onaGetOTCStatus(MerchantIntegration::generateRandomString(), MerchantIntegration::SGD);

        static::assertSame('Caught exception: ', $onaGetOTCStatus->getBody());
    }

    public function testOnaRefund(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $originTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'         => '',
            'txID'          => $txID,
            'status'        => 'success',
            'description'   => '',
            'paymentMethod' => 'GPWALLET',
            'txStatus'      => 'success',
            'reason'        => '',
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new Response(200, [], $response));
        $onaRefund = $this->merchantIntegrationOnline->onaRefund($partnerTxID, $partnerGroupTxID, self::AMOUNT, MerchantIntegration::SGD, $originTxID, 'testing onaRefund', $accessToken);

        static::assertSame($response, $onaRefund->getBody());
    }

    public function testOnaRefundException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaRefund = $this->merchantIntegrationOnlineException->onaRefund(MerchantIntegration::generateRandomString(), MerchantIntegration::generateRandomString(), self::AMOUNT, MerchantIntegration::SGD, MerchantIntegration::generateRandomString(), 'testing onaRefund', MerchantIntegration::generateRandomString());

        static::assertSame('Caught exception: ', $onaRefund->getBody());
    }

    public function testOnaGetRefundStatus(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $response = (object) [
            'msgID'         => '',
            'txID'          => $txID,
            'status'        => 'success',
            'description'   => '',
            'paymentMethod' => 'GPWALLET',
            'txStatus'      => 'success',
            'reason'        => '',
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new Response(200, [], $response));
        $onaGetRefundStatus = $this->merchantIntegrationOnline->onaGetRefundStatus($partnerTxID, MerchantIntegration::SGD, $accessToken);

        static::assertSame($response, $onaGetRefundStatus->getBody());
    }

    public function testOnaGetRefundStatusException(): void
    {
        $this->merchantIntegrationOnlineException->expects(static::once())->method('getPartnerInfo')->will(static::throwException(new \Exception()));
        $onaGetRefundStatus = $this->merchantIntegrationOnlineException->onaGetRefundStatus(MerchantIntegration::generateRandomString(), MerchantIntegration::SGD, MerchantIntegration::generateRandomString());

        static::assertSame('Caught exception: ', $onaGetRefundStatus->getBody());
    }
}