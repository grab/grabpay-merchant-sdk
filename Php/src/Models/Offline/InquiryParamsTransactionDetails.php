<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

class InquiryParamsTransactionDetails extends TransactionDetails
{
    /**
     * This field will contain the actual transaction reference that the merchant wants to inquire.
     */
    public string $txRefID;

    /**
     * This field will differentiate the type of transaction reference that the merchant is making an inquiry with.
     */
    public string $txRefType;

    /**
     * This field will differentiate a payment inquiry from a refund inquiry.
     */
    public string $txType;
}