<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class AdditionalInfo extends DTO
{
    /**
     * Details the payment combinations that a user can apply in a transaction.
     */
    public AmountBreakdown $amountBreakdown;
}