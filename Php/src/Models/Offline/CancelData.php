<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class CancelData extends DTO
{
    /**
     * Cancel status details.
     */
    public StatusDetails $statusDetails;

    /**
     * Cancel transaction details.
     */
    public ResponseTransactionDetails $transactionDetails;
}