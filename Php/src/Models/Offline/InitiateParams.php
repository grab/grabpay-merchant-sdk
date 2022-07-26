<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InitiateParams extends DTO
{
    /**
     * Initiate payment method.
     */
    public ?InitiateParamsPaymentMethod $paymentMethod;

    /**
     * Initiate POS details.
     */
    public ?InitiateParamsPosDetails $POSDetails;

    /**
     * Initiate transaction details.
     */
    public InitiateParamsTransactionDetails $transactionDetails;
}