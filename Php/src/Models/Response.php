<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models;

use Spatie\DataTransferObject\DataTransferObject;

abstract class Response extends DataTransferObject
{
    /**
     * HTTP response headers
     */
    public array $headers;

    /**
     * HTTP response status code
     */
    public int $status;
}