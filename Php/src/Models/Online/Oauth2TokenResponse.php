<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\Response;

class Oauth2TokenResponse extends Response
{
    public Oauth2TokenData $data;
}