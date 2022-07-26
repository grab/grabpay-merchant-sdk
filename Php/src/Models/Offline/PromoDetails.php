<?php

declare(strict_types=1);

namespace GrabPay\Merchant\Models\Offline;

use GrabPay\Merchant\Models\DTO;

class PromoDetails extends DTO
{
    /**
     * If txType=PAYMENT
     * This field contains the amount that the consumer is paying after applying a merchant-funded promo code.
     */
    public ?int $consumerPaidAmt;

    /**
     * If txType=REFUND
     * This field contains the amount that was refunded to the consumer during a refund.
     */
    public ?int $consumerRefundedAmt;

    /**
     * If txType=PAYMENT
     * This field contains the amount that the merchant is subsidizing after the consumer applies a merchant-funded promo code.
     */
    public ?int $merchantFundedPromoAmt;

    /**
     * If txType=REFUND
     * This field contains that amount that is not refunded to the consumer because the merchant has subsidized it during payment as part of a merchant-funded promo.
     */
    public ?int $merchantFundedPromoRefundAmt;

    /**
     * If txType=PAYMENT
     * This field contains the bonus points issued to the consumer, based on the bonus multiplier applied.
     */
    public ?int $pointsAwarded;

    /**
     * If txType=PAYMENT
     * This field contains the points multiplier applied in deriving the bonus points issued to the consumer.
     */
    public ?string $pointsMultiplier;

    /**
     * If txType=REFUND
     * This field contains the bonus points that was clawed back from the consumer during a refund.
     */
    public ?int $pointsRefunded;

    /**
     * If txType=PAYMENT
     * This field contains the merchant-funded promo code that has been applied by a consumer during payment.
     */
    public ?string $promoCode;
}