<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class GetOtcStatusParams extends DTO
{
    /**
     * Currency that is associated with the payment amount. Specify the three-letter ISO currency code.
     */
    public string $currency;

    /**
     * The partner's transaction ID.
     */
    public string $partnerTxID;
}