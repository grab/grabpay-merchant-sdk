<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\Response;

class GetChargeStatusResponse extends Response
{
    public GetChargeStatusData $data;
}