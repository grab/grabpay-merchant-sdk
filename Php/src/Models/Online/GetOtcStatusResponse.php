<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\Response;

class GetOtcStatusResponse extends Response
{
    public GetOtcStatusData $data;
}