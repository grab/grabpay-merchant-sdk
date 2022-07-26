<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class RefundParams extends DTO
{
    /**
     * Refund transaction details.
     */
    public RefundParamsTransactionDetails $transactionDetails;
}