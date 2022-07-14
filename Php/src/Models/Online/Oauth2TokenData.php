<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class Oauth2TokenData extends DTO
{
    /**
     * The access token issued by the authorization server.
     */
    public string $access_token;

    /**
     * The lifetime in seconds of the access token.
     */
    public int $expires_in;

    /**
     * Identity token.
     * Not in used for now.
     */
    public ?string $id_token;

    /**
     * The type of the token issued.
     */
    public string $token_type;
}