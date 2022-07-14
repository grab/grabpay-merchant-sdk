<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class ChargeCompleteParams extends DTO
{
    /**
     * OAuth access token.
     */
    public string $accessToken;

    /**
     * The partner's transaction ID.
     */
    public string $partnerTxID;
}