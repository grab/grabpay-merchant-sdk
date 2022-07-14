<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class Device extends DTO
{
    /**
     * Specify the brand of the user's device.
     */
    public ?string $deviceBrand;

    /**
     * The unique device identifier.
     */
    public ?string $deviceID;

    /**
     * Specify the model of the user's device.
     */
    public ?string $deviceModel;

    /**
     * Specify the IMEI of the device required for android devices.
     */
    public ?string $imei;

    /**
     * Specify the IOS UDID of the device required for IOS devices.
     */
    public ?string $iosUDID;
}