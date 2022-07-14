<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class ShippingDetails extends DTO
{
    /**
     * Customer's postal address.
     */
    public ?string $address;

    /**
     * Customer's postal address city name.
     */
    public ?string $city;

    /**
     * Customer's country code.
     */
    public ?string $countryCode;

    /**
     * Customer's email address.
     */
    public ?string $email;

    /**
     * Customer's first name.
     */
    public ?string $firstName;

    /**
     * Customer's last name.
     */
    public ?string $lastName;

    /**
     * Customer's phone number.
     */
    public ?string $phone;

    /**
     * Customer's postal code.
     */
    public ?string $postalCode;
}