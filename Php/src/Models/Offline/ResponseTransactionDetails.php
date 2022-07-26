<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

class ResponseTransactionDetails extends TransactionDetails
{
    /**
     * This field contains the transaction amount, which is a positive integer in the smallest currency unit, with no decimal point i.e., $123.45 will be passed in as 12345 in this field.
     */
    public ?int $amount;

    /**
     * This field can be used to define the Bill Reference Number when a merchant is receiving payments via a Grab wallet, or non-Grab wallet.
     * When this field is not sent in the request, consumers will be able to enter the reference number themselves in the respective payment app.
     */
    public ?string $billRefNumber;

    /**
     * This field contains the unique transaction reference number issued by Grab for the refund. This field will be idempotent to Grab.
     */
    public ?string $grabTxID;

    /**
     * This field contains the original payment's merchant's primary transaction reference.
     */
    public ?string $originPartnerTxID;

    /**
     * This field contains the merchant's secondary transaction reference for the refund.
     */
    public ?string $partnerGroupTxID;

    /**
     * This field contains the merchant's primary transaction reference for the refund.
     */
    public ?string $partnerTxID;

    /**
     * This will allow both the merchant's and Grab's systems to expire the transaction after a specified time.
     * This field will be in Unix Epoch time.
     */
    public ?int $paymentExpiryTime;

    /**
     * This field contains the payment method which the consumer has selected during payment.
     * For a refund transaction, the payment method will be the same as what the consumer has selected during payment.
     */
    public ?string $paymentMethod;

    /**
     * This field contains the sum of past refund amounts, which is a positive integer in the smallest currency unit, with no decimal point i.e., $123.45 will be passed in as 12345 in this field.
     */
    public ?int $refundedAmount;

    /**
     * Transaction type, PAYMENT or REFUND.
     */
    public ?string $txType;

    /**
     * This field contains the time which the transaction was last updated in Grab's system.
     * For a successful transaction, this will be the time where Grab registers the transaction in its system. This field will be in Unix Epoch time.
     */
    public ?int $updatedTime;
}