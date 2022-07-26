<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class InitiateParamsPaymentMethod extends DTO
{
    /**
     * This field will allow merchants to specify the minimum amount before consumers are allowed to pay using PayLater 4 Instalments as a payment method.
     */
    public ?int $minAmt4Instalment;

    /**
     * This field will allow merchants to specify the minimum amount before consumers are allowed to pay using PayLater Postpaid as a payment method.
     */
    public ?int $minAmtPostpaid;

    /**
     * This field will allow merchants to specify specific payment methods to exclude during consumer selection. Do note that default wallet GPWALLET cannot be excluded.
     * If this field is not sent in the request, all available payment methods to the consumer and the merchant will be offered for consumer selection.
     */
    public ?array $paymentMethodExclusion;
}