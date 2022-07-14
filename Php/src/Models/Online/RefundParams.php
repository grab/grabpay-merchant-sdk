<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class RefundParams extends DTO
{
    /**
     * OAuth access token.
     */
    public string $accessToken;

    /**
     * Transaction amount as integer.
     * A positive integer in the smallest currency unit (e.g., 100 cents to charge S$1.00, a zero-decimal currency).
     */
    public int $amount;

    /**
     * Currency that is associated with the payment amount.
     * Specify the three-letter ISO 4217 currency code.
     */
    public string $currency;

    /**
     * Description of the charge.
     */
    public string $description;

    /**
     * Merchant's own reference that is returned to the merchant via the webhook.
     */
    public ?string $echo;

    /**
     * GrabPay transaction ID that is formatted as 32-char UUID string without dashes.
     * When initiating a refund of this transaction, you will need to specify this txID to identify the transaction marked for refund.
     */
    public ?string $originTxID;

    /**
     * This is the unique identifier of each transaction generated by the partner.
     * Each transaction might consist of several charges.
     * Usually the value is a single receipt ID from the partner and can be displayed to the user.
     */
    public string $partnerGroupTxID;

    /**
     * The partner's transaction ID.
     */
    public string $partnerTxID;
}