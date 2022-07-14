<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

class GetRefundStatusData extends ChargeCompleteData
{
    /**
     * Merchant's own reference that is returned to the merchant via the webhook
     */
    public ?string $echo;
}