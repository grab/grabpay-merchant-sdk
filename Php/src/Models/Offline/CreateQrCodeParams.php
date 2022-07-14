<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class CreateQrCodeParams extends DTO
{
    /**
     * Transaction amount as integer.
     * A positive integer in the smallest currency unit, with no decimal point.
     */
    public int $amount;

    /**
     * Currency that is associated with the payment amount.
     * Specify the three-letter ISO currency code following the ISO 4217 standard.
     */
    public string $currency;

    /**
     * The UUID of the request with a fixed length of 32 characters.
     */
    public string $msgID;

    /**
     * A unique order number generated by the terminal with a fixed length of 32 characters.
     * Also known as the partner transaction ID. This is used as the idempotency key of the request.
     * Should be the same as the partner transaction ID used while generating the signature.
     */
    public string $partnerTxID;
}
