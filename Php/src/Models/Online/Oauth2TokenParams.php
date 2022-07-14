<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class Oauth2TokenParams extends DTO
{
    /**
     * Get from the URL parameter in the previous step.
     */
    public string $code;

    /**
     * A random string generated during the Web URL building stage.
     */
    public string $codeVerifier;
}