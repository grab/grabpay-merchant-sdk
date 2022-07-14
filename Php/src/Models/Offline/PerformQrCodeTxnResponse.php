<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\Response;

class PerformQrCodeTxnResponse extends Response
{
    public PerformQrCodeTxnData $data;
}
