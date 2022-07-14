<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models;

class ErrorData extends DTO
{
    /**
     * Arg
     */
    public ?string $arg;

    /**
     * Error code
     */
    public ?int $code;

    /**
     * Debug message
     */
    public ?string $devMessage;

    /**
     * Returned when there is an OAuth error.
     */
    public ?string $error;

    /**
     * Returned when there is an OAuth error.
     */
    public ?string $error_description;

    /**
     * Returned when there is an error for Online
     */
    public ?string $message;

    /**
     * Error reason
     */
    public ?string $reason;
}