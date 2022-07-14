<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use ValueError;

/**
 * @internal
 * @coversNothing
 */
final class MerchantIntegrationUnitTest extends MerchantIntegrationTest
{
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
}

