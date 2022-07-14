<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\Response;

class CancelTxnResponse extends Response
{
    public ?CancelTxnData $data;
}