<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\Response;

class InitiateResponse extends Response
{
    public InitiateData $data;
}