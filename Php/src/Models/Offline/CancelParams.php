<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class CancelParams extends DTO
{
    /**
     * Cancel transaction details.
     */
    public CancelParamsTransactionDetails $transactionDetails;
}