<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOnline;
use GrabPay\Merchant\Models\Online\ChargeCompleteParams;
use GrabPay\Merchant\Models\Online\ChargeCompleteResponse;
use GrabPay\Merchant\Models\Online\ChargeInitParams;
use GrabPay\Merchant\Models\Online\ChargeInitResponse;
use GrabPay\Merchant\Models\Online\GenerateWebUrlParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusParams;
use GrabPay\Merchant\Models\Online\GetChargeStatusResponse;
use GrabPay\Merchant\Models\Online\GetOtcStatusParams;
use GrabPay\Merchant\Models\Online\GetOtcStatusResponse;
use GrabPay\Merchant\Models\Online\GetRefundStatusParams;
use GrabPay\Merchant\Models\Online\GetRefundStatusResponse;
use GrabPay\Merchant\Models\Online\Oauth2TokenParams;
use GrabPay\Merchant\Models\Online\Oauth2TokenResponse;
use GrabPay\Merchant\Models\Online\RefundParams;
use GrabPay\Merchant\Models\Online\RefundResponse;

/**
 * @internal
 * @coversNothing
 */
final class MerchantIntegrationOnlineUnitTest extends MerchantIntegrationTest
{
    private MerchantIntegrationOnline $merchantIntegrationOnline;

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
            ->onlyMethods(['sendGetRequest', 'sendPostRequest', 'sendPutRequest', 'sendRawPostRequest'])
            ->getMock()
        ;
    }

    public function testChargeComplete(): void
    {
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $params = new ChargeCompleteParams([
            'accessToken' => $accessToken,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'msgID'         => $msgID,
                'txID'          => $txID,
                'status'        => 'success',
                'description'   => '',
                'paymentMethod' => 'GPWALLET',
                'txStatus'      => 'success',
                'reason'        => '',
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new ChargeCompleteResponse($response));
        $chargeComplete = $this->merchantIntegrationOnline->chargeComplete($params);

        static::assertInstanceOf(ChargeCompleteResponse::class, $chargeComplete);
    }

    public function testChargeInit(): void
    {
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();

        $params = new ChargeInitParams([
            'amount'           => self::AMOUNT,
            'currency'         => MerchantIntegrationOnline::SGD,
            'description'      => 'testing chargeInit',
            'partnerGroupTxID' => $partnerGroupTxID,
            'partnerTxID'      => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'partnerTxID' => $partnerTxID,
                'request'     => $request,
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new ChargeInitResponse($response));
        $chargeInit = $this->merchantIntegrationOnline->chargeInit($params);

        static::assertInstanceOf(ChargeInitResponse::class, $chargeInit);
        static::assertSame($partnerTxID, $chargeInit->data->partnerTxID);
    }

    public function testChargeInitFull(): void
    {
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();
        $hidePaymentMethods = ['CARD'];
        $metaInfo = [
            'brandName' => 'brandNameValue',
            'location'  => [
                'ipAddress' => '127.0.0.1',
            ],
            'device' => [
                'deviceID' => MerchantIntegration::generateRandomString(),
            ],
            'subMerchant' => [
                'name' => 'subMerchantNameValue',
            ],
            'partnerUserInfo' => [
                'id' => MerchantIntegration::generateRandomString(),
            ],
        ];
        $items = [['supplierName' => 'supplierNameValue']];
        $shippingDetails = ['firstName' => 'firstNameValue'];

        $params = new ChargeInitParams([
            'amount'             => self::AMOUNT,
            'currency'           => MerchantIntegrationOnline::SGD,
            'description'        => 'testing chargeInit',
            'partnerGroupTxID'   => $partnerGroupTxID,
            'partnerTxID'        => $partnerTxID,
            'metaInfo'           => $metaInfo,
            'items'              => $items,
            'shippingDetails'    => $shippingDetails,
            'hidePaymentMethods' => $hidePaymentMethods,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'partnerTxID' => $partnerTxID,
                'request'     => $request,
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new ChargeInitResponse($response));
        $chargeInit = $this->merchantIntegrationOnline->chargeInit($params);

        static::assertInstanceOf(ChargeInitResponse::class, $chargeInit);
        static::assertSame($partnerTxID, $chargeInit->data->partnerTxID);
    }

    public function testGenerateWebUrl(): void
    {
        $codeVerifier = MerchantIntegration::generateRandomString();
        $request = MerchantIntegration::generateRandomString();

        $params = new GenerateWebUrlParams([
            'currency'     => MerchantIntegration::SGD,
            'codeVerifier' => $codeVerifier,
            'request'      => $request,
        ]);

        $generateWebUrl = $this->merchantIntegrationOnline->generateWebUrl($params);
        parse_str(parse_url($generateWebUrl, PHP_URL_QUERY), $query);

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
        $msgID = MerchantIntegration::generateRandomString();
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $params = new GetChargeStatusParams([
            'accessToken' => $accessToken,
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'msgID'         => $msgID,
                'txID'          => $txID,
                'status'        => 'success',
                'description'   => '',
                'paymentMethod' => 'GPWALLET',
                'txStatus'      => 'success',
                'reason'        => '',
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new GetChargeStatusResponse($response));
        $getChargeStatus = $this->merchantIntegrationOnline->getChargeStatus($params);

        static::assertInstanceOf(GetChargeStatusResponse::class, $getChargeStatus);
    }

    public function testGetOtcStatus(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();

        $params = new GetOtcStatusParams([
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'txID'          => $txID,
                'oAuthCode'     => '',
                'status'        => 'success',
                'paymentMethod' => 'GPWALLET',
                'txStatus'      => 'success',
                'reason'        => '',
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new GetOtcStatusResponse($response));
        $getOtcStatus = $this->merchantIntegrationOnline->getOtcStatus($params);

        static::assertInstanceOf(GetOtcStatusResponse::class, $getOtcStatus);
    }

    public function testGetRefundStatus(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $params = new GetRefundStatusParams([
            'accessToken' => $accessToken,
            'currency'    => MerchantIntegrationOnline::SGD,
            'partnerTxID' => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'msgID'         => '',
                'txID'          => $txID,
                'status'        => 'success',
                'description'   => '',
                'paymentMethod' => 'GPWALLET',
                'txStatus'      => 'success',
                'reason'        => '',
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendGetRequest')->willReturn(new GetRefundStatusResponse($response));
        $getRefundStatus = $this->merchantIntegrationOnline->getRefundStatus($params);

        static::assertInstanceOf(GetRefundStatusResponse::class, $getRefundStatus);
    }

    public function testOauth2Token(): void
    {
        $code = MerchantIntegration::generateRandomString();
        $codeVerifier = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $params = new Oauth2TokenParams([
            'code'         => $code,
            'codeVerifier' => $codeVerifier,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'access_token' => $accessToken,
                'token_type'   => 'Bearer',
                'expires_in'   => 31535999,
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendRawPostRequest')->willReturn(new Oauth2TokenResponse($response));
        $oauth2Token = $this->merchantIntegrationOnline->oauth2Token($params);

        static::assertInstanceOf(Oauth2TokenResponse::class, $oauth2Token);
    }

    public function testRefund(): void
    {
        $txID = MerchantIntegration::generateRandomString();
        $partnerTxID = MerchantIntegration::generateRandomString();
        $partnerGroupTxID = MerchantIntegration::generateRandomString();
        $originTxID = MerchantIntegration::generateRandomString();
        $accessToken = MerchantIntegration::generateRandomString();

        $params = new RefundParams([
            'accessToken'      => $accessToken,
            'amount'           => self::AMOUNT,
            'currency'         => MerchantIntegrationOnline::SGD,
            'description'      => 'testing refund',
            'originTxID'       => $originTxID,
            'partnerGroupTxID' => $partnerGroupTxID,
            'partnerTxID'      => $partnerTxID,
        ]);

        $response = [
            'status'  => 200,
            'headers' => [],
            'data'    => [
                'msgID'         => '',
                'txID'          => $txID,
                'status'        => 'success',
                'description'   => '',
                'paymentMethod' => 'GPWALLET',
                'txStatus'      => 'success',
                'reason'        => '',
            ],
        ];

        $this->merchantIntegrationOnline->expects(static::once())->method('sendPostRequest')->willReturn(new RefundResponse($response));
        $refund = $this->merchantIntegrationOnline->refund($params);

        static::assertInstanceOf(RefundResponse::class, $refund);
    }
}