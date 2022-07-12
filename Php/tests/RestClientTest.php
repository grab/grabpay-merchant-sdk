<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use GrabPay\Merchant\MerchantIntegration;
use GrabPay\Merchant\RestClient;

/**
 * @internal
 * @coversNothing
 */
final class RestClientTest extends MerchantIntegrationTest
{
    public function testSendGetRequest(): void
    {
        $response = RestClient::sendRequest(MerchantIntegration::METHOD_GET, 'https://grab.com', [], []);

        static::assertSame(200, $response->code);
        static::assertNotEmpty($response->raw_body);
        static::assertNotEmpty($response->body);
        static::assertNotEmpty($response->headers);
    }

    public function testSendPostRequest(): void
    {
        $response = RestClient::sendRequest(MerchantIntegration::METHOD_POST, 'https://grab.com', [], []);

        static::assertSame(200, $response->code);
        static::assertNotEmpty($response->raw_body);
        static::assertNotEmpty($response->body);
        static::assertNotEmpty($response->headers);
    }

    public function testSendPutRequest(): void
    {
        $response = RestClient::sendRequest(MerchantIntegration::METHOD_PUT, 'https://grab.com', [], []);

        static::assertSame(405, $response->code);
        static::assertNotEmpty($response->raw_body);
        static::assertNotEmpty($response->body);
        static::assertNotEmpty($response->headers);
    }
}
