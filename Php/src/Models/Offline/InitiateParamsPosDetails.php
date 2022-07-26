<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InitiateParamsPosDetails extends DTO
{
    /**
     * This field is the consumer identifier derived from scanning the barcode or the QR code flashed by the consumer during payment.
     * This field is only required when paymentChannel in the request is "CPQR".
     */
    public ?string $consumerIdentifier;

    /**
     * This field is the unique terminal identifier used to differentiate between multiple terminals.
     * It is required for every POS terminal transaction.
     */
    public ?string $terminalID;
}