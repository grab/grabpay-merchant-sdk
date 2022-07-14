<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\Response;

class GetRefundStatusResponse extends Response
{
    public GetRefundStatusData $data;
}