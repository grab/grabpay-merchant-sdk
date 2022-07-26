<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models;

use GrabPay\Merchant\Models\Offline\StatusDetails;

class ErrorData extends DTO
{
    /**
     * Online: code.
     */
    public ?int $code;

    /**
     * Online: Returned when there is an OAuth error.
     */
    public ?string $error;

    /**
     * Online: Returned when there is an OAuth error.
     */
    public ?string $error_description;

    /**
     * Online: Returned when there is an error.
     */
    public ?string $message;

    /**
     * Offline v3.
     */
    public ?StatusDetails $statusDetails;
}