<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class ChargeCompleteData extends DTO
{
    /**
     * If the transaction is successful, that is, if the status: success, then description will be empty.
     * However, if the transaction failed (i.e. status: failed), the API provides a description of the error.
     */
    public ?string $description;

    /**
     * Request ID.
     */
    public ?string $msgID;

    /**
     * GrabPay payment method.
     */
    public string $paymentMethod;

    /**
     * The reason code is for additional information on the payment.
     * In some cases, reason could be empty. For example, when txStatus is success.
     */
    public ?string $reason;

    /**
     * Please use txStatus instead.
     *
     * @deprecated
     */
    public ?string $status;

    /**
     * Partner transaction ID used as the idempotency key for subsequent API calls.
     */
    public string $txID;

    /**
     * Status of the transaction.
     */
    public string $txStatus;
}