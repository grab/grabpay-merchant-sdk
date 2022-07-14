<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\Response;

class RefundTxnResponse extends Response
{
    public RefundTxnData $data;
}
