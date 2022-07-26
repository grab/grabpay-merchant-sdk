<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

class RefundParamsTransactionDetails extends TransactionDetails
{
    /**
     * This field contains the transaction amount, which is a positive integer in the smallest currency unit, with no decimal point i.e., $123.45 will be passed in as 12345 in this field.
     */
    public int $amount;

    /**
     * This field contains the original payment's merchant's primary transaction reference.
     */
    public string $originPartnerTxID;

    /**
     * This field contains the merchant's secondary transaction reference for the refund.
     */
    public ?string $partnerGroupTxID;

    /**
     * This field contains the merchant's primary transaction reference for the refund.
     */
    public string $partnerTxID;

    /**
     * This field will be used to pass the reason for refund to Grab.
     */
    public ?string $reason;
}