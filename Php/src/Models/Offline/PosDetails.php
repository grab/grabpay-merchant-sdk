<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class PosDetails extends DTO
{
    /**
     * If paymentChannel=MPQR
     * This field contains the Merchant-Present QR payload, and will only be returned when paymentChannel in the request is "MPQR".
     */
    public ?string $qrPayload;

    /**
     * This field is the unique terminal identifier used to differentiate between multiple terminals.
     * It is required for every POS terminal transaction.
     */
    public string $terminalID;
}