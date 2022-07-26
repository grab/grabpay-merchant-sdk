<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

class CancelParamsTransactionDetails extends TransactionDetails
{
    /**
     * This field contains the original payment's merchant's primary transaction reference.
     */
    public string $originPartnerTxID;
}