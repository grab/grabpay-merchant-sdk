<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class PartnerUserInfo extends DTO
{
    /**
     * Specify the email address.
     */
    public ?string $email;

    /**
     * Specify the partner user identifier.
     */
    public ?string $id;

    /**
     * Specify the phone number.
     */
    public ?string $phone;
}