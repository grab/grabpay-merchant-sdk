<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class TransactionDetails extends DTO
{
    /**
     * This field will contain the three-letter ISO currency code for the transaction.
     */
    public string $currency;

    /**
     * This field will be used to differentiate the different POS payment channels, and will determine which other Conditional parameters will need to be required in the request, or processed in the response.
     */
    public string $paymentChannel;

    /**
     * This field is the unique store identifier issued by Grab to the merchant.
     * Marking this as nullable because for request, we are adding it automatically.
     */
    public ?string $storeGrabID;
}