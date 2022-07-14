<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\Response;

class ChargeCompleteResponse extends Response
{
    public ChargeCompleteData $data;
}