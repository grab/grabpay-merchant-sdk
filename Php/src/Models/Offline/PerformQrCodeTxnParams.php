<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class PerformQrCodeTxnParams extends DTO
{
    /**
     * An array of JSON objects of additional information needed in the response.
     */
    public ?array $additionalInfo;

    /**
     * Transaction amount as integer. A positive integer in the smallest currency unit, with no decimal point.
     */
    public int $amount;

    /**
     * The barcode or QR code generated on the consumer's Grab app.
     * The maximum length is 18.
     */
    public string $code;

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
     * A unique payment or refund reference sent from the terminal with a fixed length of 32 characters.
     */
    public string $partnerTxID;
}