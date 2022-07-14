<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class GenerateWebUrlParams extends DTO
{
    /**
     * A random string generated during the Web URL building stage.
     */
    public string $codeVerifier;

    /**
     * Currency that is associated with the payment amount.
     * Specify the three-letter ISO 4217 currency code.
     */
    public string $currency;

    /**
     * The request is passed as an unsigned base64 encoded JWT.
     * This value is the request received in the JSON response of the /charge/init API.
     */
    public string $request;
}