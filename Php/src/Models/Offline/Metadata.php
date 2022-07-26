<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class Metadata extends DTO
{
    /**
     * This field contains the OFFUS transaction ID issued when a consumer uses a non-Grab wallet to make a payment to a merchant's OFFUS enabled QR code.
     */
    public ?string $offusTxID;

    /**
     * This field contains the OFFUS transaction ID issued for the original OFFUS payment when a merchant makes a refund to a non-Grab wallet.
     */
    public ?string $originOffusTxID;
}