<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class OriginTxDetails extends DTO
{
    /**
     * This field contains the original payment's transaction amount.
     */
    public int $originAmount;

    /**
     * This field contains the original payment's Grab's transaction reference.
     */
    public string $originGrabTxID;

    /**
     * This field contains the original payment's merchant's secondary transaction reference.
     */
    public string $originPartnerGroupTxID;

    /**
     * This field contains the original payment's merchant's primary transaction reference.
     */
    public string $originPartnerTxID;
}