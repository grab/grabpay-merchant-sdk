<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\MerchantIntegrationOnline;
use ValueError;

/**
 * @internal
 */
final class MerchantIntegrationUnitTest extends MerchantIntegrationTest
{
    public function testGenerateRandomString(): void
    {
        $result = MerchantIntegration::generateRandomString();
        static::assertSame(32, \strlen($result));

        $result2 = MerchantIntegration::generateRandomString(64);
        static::assertSame(64, \strlen($result2));

        $result3 = MerchantIntegration::generateRandomString(128);
        static::assertSame(128, \strlen($result3));

        // ValueError is only available in PHP 8
        if (\defined('PHP_VERSION_ID') && \PHP_VERSION_ID > 80000) {
            $this->expectException(ValueError::class);
            $result4 = MerchantIntegration::generateRandomString(-1);
            static::assertSame(0, \strlen($result4));
        }
    }

    public function testBase64URLEncode(): void
    {
        $result = MerchantIntegration::base64URLEncode('test');
        static::assertSame('dGVzdA', $result);

        $result = MerchantIntegration::base64URLEncode('test=');
        static::assertSame('dGVzdD0', $result);

        $result = MerchantIntegration::base64URLEncode('test+');
        static::assertSame('dGVzdCs', $result);

        $result = MerchantIntegration::base64URLEncode('test/');
        static::assertSame('dGVzdC8', $result);
    }

    public function testGenerateHmacSignatureGet(): void
    {
        $result = MerchantIntegration::generateHmacSignature('partnerSecret', 'GET', '/', MerchantIntegration::HEADER_APPLICATION_FORM, [], 'Wed, 06 Jul 2022 06:30:32 GMT');
        static::assertSame('H/Eytzn6c0EUfqWMRWM9v9b4TsuQ90VdNs2hXMP+Ggc=', $result);
    }

    public function testGenerateHmacSignaturePost(): void
    {
        $result = MerchantIntegration::generateHmacSignature('partnerSecret', 'POST', '/post', MerchantIntegration::HEADER_APPLICATION_JSON, ['foo' => 'bar'], 'Wed, 06 Jul 2022 06:30:32 GMT');
        static::assertSame('kiTxYZn2sFaLHihx4AW0N3DcA7q29b75FirBvq60h3o=', $result);
    }

    public function testGeneratePopSignature(): void
    {
        $result = MerchantIntegration::generatePopSignature('clientSecret', 'accessToken', '1657089032');
        static::assertSame('eyJ0aW1lX3NpbmNlX2Vwb2NoIjpmYWxzZSwic2lnIjoiUFhCSUFybmlQdFNiQ09SUkJRcVc5YVo5U0kzZVUweW1zM3dMaGJmYy1jTSJ9', $result);
    }

    public function testGetPartnerInfoOnlineRegional(): void
    {
        // getPartnerInfo for Regional
        $expectedPartnerInfoRegional = [
            'sdkVersion'            => MerchantIntegration::SDK_VERSION,
            'sdkSignature'          => MerchantIntegration::SDK_SIGNATURE,
            'environment'           => MerchantIntegration::STAGING,
            'partnerID'             => 'SG_STG_OTC_PARTNER_ID',
            'partnerSecret'         => 'SG_STG_OTC_PARTNER_SECRET',
            'merchantID'            => 'SG_STG_OTC_MERCHANT_ID',
            'terminalID'            => '',
            'clientID'              => 'SG_STG_OTC_CLIENT_ID',
            'clientSecret'          => 'SG_STG_OTC_CLIENT_SECRET',
            'country'               => MerchantIntegration::SG,
            'url'                   => self::REGIONAL_STG_API_URL,
            'chargeInit'            => self::REGIONAL_PARTNER_V2_PATH . '/charge/init',
            'OAuth2Token'           => self::GRABID_V1_OAUTH2_TOKEN,
            'chargeComplete'        => self::REGIONAL_PARTNER_V2_PATH . '/charge/complete',
            'onaChargeStatus'       => self::REGIONAL_PARTNER_V2_PATH . '/charge/{partnerTxID}/status?currency={currency}',
            'onaRefundTxn'          => self::REGIONAL_PARTNER_V2_PATH . '/refund',
            'onaCheckRefundTxn'     => self::REGIONAL_PARTNER_V2_PATH . '/refund/{refundPartnerTxID}/status?currency={currency}',
            'oneTimeChargeStatus'   => self::REGIONAL_PARTNER_V2_PATH . '/one-time-charge/{partnerTxID}/status?currency={currency}',
            'createQrCode'          => self::REGIONAL_PARTNER_V1_PATH . '/terminal/qrcode/create',
            'cancelQrTxn'           => self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{origPartnerTxID}/cancel',
            'posRefundTxn'          => self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{origPartnerTxID}/refund',
            'performTxn'            => self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/perform',
            'posChargeStatus'       => self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType=P2M',
            'posChargeRefundStatus' => self::REGIONAL_PARTNER_V1_PATH . '/terminal/transaction/{refundPartnerTxID}?currency={currency}&msgID={msgID}&txType=Refund',
        ];

        // getPartnerInfo for Regional staging
        $merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegration::STAGING, MerchantIntegration::SG, 'SG_STG_OTC_PARTNER_ID', 'SG_STG_OTC_PARTNER_SECRET', 'SG_STG_OTC_MERCHANT_ID', 'SG_STG_OTC_CLIENT_ID', 'SG_STG_OTC_CLIENT_SECRET', self::REDIRECT_URL);
        $getPartnerInfo = $merchantIntegrationOnline->getPartnerInfo();
        static::assertSame($expectedPartnerInfoRegional, $getPartnerInfo);

        // getPartnerInfo for Regional Production
        $merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegration::PRODUCTION, MerchantIntegration::SG, 'SG_PRD_OTC_PARTNER_ID', 'SG_PRD_OTC_PARTNER_SECRET', 'SG_PRD_OTC_MERCHANT_ID', 'SG_PRD_OTC_CLIENT_ID', 'SG_PRD_OTC_CLIENT_SECRET', self::REDIRECT_URL);
        $getPartnerInfo = $merchantIntegrationOnline->getPartnerInfo();
        $expectedPartnerInfoRegional['environment'] = MerchantIntegration::PRODUCTION;
        $expectedPartnerInfoRegional['partnerID'] = 'SG_PRD_OTC_PARTNER_ID';
        $expectedPartnerInfoRegional['partnerSecret'] = 'SG_PRD_OTC_PARTNER_SECRET';
        $expectedPartnerInfoRegional['merchantID'] = 'SG_PRD_OTC_MERCHANT_ID';
        $expectedPartnerInfoRegional['clientID'] = 'SG_PRD_OTC_CLIENT_ID';
        $expectedPartnerInfoRegional['clientSecret'] = 'SG_PRD_OTC_CLIENT_SECRET';
        $expectedPartnerInfoRegional['url'] = self::REGIONAL_PRD_API_URL;
        static::assertSame($expectedPartnerInfoRegional, $getPartnerInfo);
    }

    public function testGetPartnerInfoOnlineMoca(): void
    {
        // getPartnerInfo for Moca
        $expectedPartnerInfoMoca = [
            'sdkVersion'            => MerchantIntegration::SDK_VERSION,
            'sdkSignature'          => MerchantIntegration::SDK_SIGNATURE,
            'environment'           => MerchantIntegration::STAGING,
            'partnerID'             => 'VN_STG_OTC_PARTNER_ID',
            'partnerSecret'         => 'VN_STG_OTC_PARTNER_SECRET',
            'merchantID'            => 'VN_STG_OTC_MERCHANT_ID',
            'terminalID'            => '',
            'clientID'              => 'VN_STG_OTC_CLIENT_ID',
            'clientSecret'          => 'VN_STG_OTC_CLIENT_SECRET',
            'country'               => MerchantIntegration::VN,
            'url'                   => self::MOCA_STG_API_URL,
            'chargeInit'            => self::MOCA_PARTNER_V2_PATH . '/charge/init',
            'OAuth2Token'           => self::GRABID_V1_OAUTH2_TOKEN,
            'chargeComplete'        => self::MOCA_PARTNER_V2_PATH . '/charge/complete',
            'onaChargeStatus'       => self::MOCA_PARTNER_V2_PATH . '/charge/{partnerTxID}/status?currency={currency}',
            'onaRefundTxn'          => self::MOCA_PARTNER_V2_PATH . '/refund',
            'onaCheckRefundTxn'     => self::MOCA_PARTNER_V2_PATH . '/refund/{refundPartnerTxID}/status?currency={currency}',
            'oneTimeChargeStatus'   => self::MOCA_PARTNER_V2_PATH . '/one-time-charge/{partnerTxID}/status?currency={currency}',
            'createQrCode'          => self::MOCA_PARTNERS_V1_PATH . '/terminal/qrcode/create',
            'cancelQrTxn'           => self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{origPartnerTxID}/cancel',
            'posRefundTxn'          => self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{origPartnerTxID}/refund',
            'performTxn'            => self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/perform',
            'posChargeStatus'       => self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{partnerTxID}?currency={currency}&msgID={msgID}&txType=P2M',
            'posChargeRefundStatus' => self::MOCA_PARTNERS_V1_PATH . '/terminal/transaction/{refundPartnerTxID}?currency={currency}&msgID={msgID}&txType=Refund',
        ];

        // getPartnerInfo for Moca Staging
        $merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegration::STAGING, MerchantIntegration::VN, 'VN_STG_OTC_PARTNER_ID', 'VN_STG_OTC_PARTNER_SECRET', 'VN_STG_OTC_MERCHANT_ID', 'VN_STG_OTC_CLIENT_ID', 'VN_STG_OTC_CLIENT_SECRET', self::REDIRECT_URL);
        $getPartnerInfo = $merchantIntegrationOnline->getPartnerInfo();
        static::assertSame($expectedPartnerInfoMoca, $getPartnerInfo);

        // getPartnerInfo for Moca Production
        $merchantIntegrationOnline = new MerchantIntegrationOnline(MerchantIntegration::PRODUCTION, MerchantIntegration::VN, 'VN_PRD_OTC_PARTNER_ID', 'VN_PRD_OTC_PARTNER_SECRET', 'VN_PRD_OTC_MERCHANT_ID', 'VN_PRD_OTC_CLIENT_ID', 'VN_PRD_OTC_CLIENT_SECRET', self::REDIRECT_URL);
        $getPartnerInfo = $merchantIntegrationOnline->getPartnerInfo();
        $expectedPartnerInfoMoca['environment'] = MerchantIntegration::PRODUCTION;
        $expectedPartnerInfoMoca['partnerID'] = 'VN_PRD_OTC_PARTNER_ID';
        $expectedPartnerInfoMoca['partnerSecret'] = 'VN_PRD_OTC_PARTNER_SECRET';
        $expectedPartnerInfoMoca['merchantID'] = 'VN_PRD_OTC_MERCHANT_ID';
        $expectedPartnerInfoMoca['clientID'] = 'VN_PRD_OTC_CLIENT_ID';
        $expectedPartnerInfoMoca['clientSecret'] = 'VN_PRD_OTC_CLIENT_SECRET';
        $expectedPartnerInfoMoca['url'] = self::MOCA_PRD_API_URL;
        static::assertSame($expectedPartnerInfoMoca, $getPartnerInfo);
    }
}

