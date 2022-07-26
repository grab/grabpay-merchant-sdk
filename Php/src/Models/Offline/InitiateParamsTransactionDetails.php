<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

class InitiateParamsTransactionDetails extends TransactionDetails
{
    /**
     * This field contains the transaction amount, which is a positive integer in the smallest currency unit, with no decimal point i.e., $123.45 will be passed in as 12345 in this field.
     */
    public int $amount;

    /**
     * This field can be used to define the Bill Reference Number when a merchant is receiving payments via a Grab wallet, or non-Grab wallet.
     * When this field is not sent in the request, consumers will be able to enter the reference number themselves in the respective payment app.
     */
    public ?string $billRefNumber;

    /**
     * This is the merchant's secondary transaction reference.
     * This field does not have an idempotency check.
     */
    public ?string $partnerGroupTxID;

    /**
     * This is the merchant's primary transaction reference.
     * This field has to be idempotent (i.e., Different transactions cannot share the same transaction reference).
     */
    public string $partnerTxID;

    /**
     * This will allow both the merchant's and Grab's systems to expire the transaction after a specified time. This field will be in Unix Epoch time.
     */
    public int $paymentExpiryTime;
}