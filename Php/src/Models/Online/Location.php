<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class Location extends DTO
{
    /**
     * Specify the accuracy level of the latitude and longitude, in meters.
     */
    public ?int $accuracy;

    /**
     * Specify the IP address of the current user's device which initiated the transaction.
     */
    public ?string $ipAddress;

    /**
     * Specify the latitude of the current user's location, in decimal degrees.
     */
    public ?float $latitude;

    /**
     * Specify the longitude of the current user's location, in decimal degrees.
     */
    public ?float $longitude;
}