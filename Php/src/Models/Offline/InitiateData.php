<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InitiateData extends DTO
{
    /**
     * Initiate POS details.
     */
    public PosDetails $POSDetails;

    /**
     * Initiate status details.
     */
    public StatusDetails $statusDetails;

    /**
     * Initiate transaction details.
     */
    public ResponseTransactionDetails $transactionDetails;
}
