<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class CreateQrCodeData extends DTO
{
    /**
     * QR code expiry time.
     */
    public string $expiryTime;

    /**
     * The UUID of the request with a fixed length of 32 characters.
     */
    public string $msgID;

    /**
     * The QR code to be displayed at the terminal for the consumer to scan and pay using the Grab app.
     */
    public string $qrcode;

    /**
     * The QR code UUID with a fixed length of 32 characters.
     */
    public ?string $qrid;

    /**
     * A unique transaction ID generated by GrabPay.
     */
    public string $txID;
}
