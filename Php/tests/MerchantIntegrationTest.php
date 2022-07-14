<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Tests;

use PHPUnit\Framework\TestCase;

abstract class MerchantIntegrationTest extends TestCase
{
    // Variables
    protected const AMOUNT = 10;
    protected const GRABID_V1_OAUTH2_AUTHORIZE = '/grabid/v1/oauth2/authorize';

    // Paths
    protected const GRABID_V1_OAUTH2_TOKEN = '/grabid/v1/oauth2/token';
    protected const MOCA_PARTNER_V2_PATH = '/mocapay/partner/v2';
    protected const MOCA_PARTNERS_V1_PATH = '/mocapay/partners/v1';
    protected const MOCA_PRD_API_URL = 'https://partner-gw.moca.vn';
    protected const MOCA_STG_API_URL = 'https://stg-paysi.moca.vn';
    protected const QR_CODE = '650000000000000000';
    protected const REDIRECT_URL = 'http://localhost:8080';
    protected const REGIONAL_PARTNER_V1_PATH = '/grabpay/partner/v1';
    protected const REGIONAL_PARTNER_V2_PATH = '/grabpay/partner/v2';

    // Production URLs
    protected const REGIONAL_PRD_API_URL = 'https://partner-api.grab.com';

    // Staging URLs
    protected const REGIONAL_STG_API_URL = 'https://partner-api.stg-myteksi.com';
}
