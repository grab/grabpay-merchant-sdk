<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class AmountBreakdown extends DTO
{
    /**
     * Details an integer value of the number of bonus points awarded.
     */
    public ?int $bonusPointsAwarded;

    /**
     * Details the multiplier of bonus points awarded.
     */
    public ?string $bonusPointsMultiplier;

    /**
     * The promo amount applied.
     * This parameter encapsulates the value of either a Grab funded or a merchant funded promo.
     * You may use this parameter if you do not need to differentiate between Grab and merchant funded promos.
     */
    public ?int $discountAmount;

    /**
     * The merchant acquisition promo amount applied.
     */
    public ?int $merchantAcquisitionPromoAmount;

    /**
     * The merchant funded promo amount applied.
     * This is a catch-all parameter that will be sent as long as a merchant funded promo is successfully applied in the transaction.
     * You may also use this parameter if you do not need to differentiate between Acquisition and Retention promos.
     */
    public ?int $merchantFundedPromo;

    /**
     * The merchant retention promo amount applied.
     */
    public ?int $merchantRetentionPromoAmount;

    /**
     * Amount paid by the user.
     */
    public ?int $paidAmount;

    /**
     * Refunded charge amount.
     */
    public ?int $refundedChargeAmount;

    /**
     * Revoked payout amount.
     */
    public ?int $revokePayoutAmount;
}