<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models;

class ErrorResponse extends Response
{
    public ErrorData $data;
}
