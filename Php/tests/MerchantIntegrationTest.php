<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use PHPUnit\Framework\TestCase;

abstract class MerchantIntegrationTest extends TestCase
{
    // Variables
    protected const AMOUNT = 10;
    protected const QR_CODE = '650000000000000000';
    protected const REDIRECT_URL = 'http://localhost:8080';
}
