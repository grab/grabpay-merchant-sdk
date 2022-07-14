<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class SubMerchant extends DTO
{
    /**
     * Specify the category of the merchant.
     */
    public ?string $category;

    /**
     * Specify the merchant name.
     */
    public ?string $name;
}