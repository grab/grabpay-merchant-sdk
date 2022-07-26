<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InquiryParams extends DTO
{
    /**
     * Inquiry transaction details.
     */
    public InquiryParamsTransactionDetails $transactionDetails;
}