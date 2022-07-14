<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Online;

use GrabPay\Merchant\Models\DTO;

class MetaInfo extends DTO
{
    /**
     * User-friendly merchant name to show in the transaction user flow.
     */
    public ?string $brandName;

    /**
     * Device information.
     */
    public ?Device $device;

    /**
     * Merchant own reference, it will returned back to merchant via webhook.
     */
    public ?string $echo;

    /**
     * Location information.
     */
    public ?Location $location;

    /**
     * Partner user information.
     */
    public ?PartnerUserInfo $partnerUserInfo;

    /**
     * Sub-merchant information
     */
    public ?SubMerchant $subMerchant;
}