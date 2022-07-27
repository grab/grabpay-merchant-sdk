package com.merchantsdk.payment.models;

/**
 * promoDetails
 * <p>
 * This is only applicable if the `txType` is PAYMENT, unless otherwise stated.
 * 
 */
public class PromoDetails {

    /**
     * This field contains the merchant-funded promo code that has been applied by a
     * consumer during payment.
     * 
     */
    private String promoCode;
    /**
     * This field contains the amount that the consumer is paying after applying a
     * merchant- funded promo code.
     * 
     */
    private Integer consumerPaidAmt;
    /**
     * This field contains the amount that the merchant is subsidizing after the
     * consumer applies a merchant-funded promo code.
     * 
     */
    private Integer merchantFundedPromoAmt;
    /**
     * This field contains the points multiplier applied in deriving the bonus
     * points issued to the consumer.
     * 
     */
    private String pointsMultiplier;
    /**
     * This field contains the bonus points issued to the consumer, based on the
     * bonus multiplier applied.
     * 
     */
    private Integer pointsAwarded;
    /**
     * promoRefundDetails
     * <p>
     * This is only applicable if the `txType` is REFUND.
     * 
     */
    private PromoRefund promoRefund;

    /**
     * This field contains the merchant-funded promo code that has been applied by a
     * consumer during payment.
     * 
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * This field contains the merchant-funded promo code that has been applied by a
     * consumer during payment.
     * 
     */
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    /**
     * This field contains the amount that the consumer is paying after applying a
     * merchant- funded promo code.
     * 
     */
    public Integer getConsumerPaidAmt() {
        return consumerPaidAmt;
    }

    /**
     * This field contains the amount that the consumer is paying after applying a
     * merchant- funded promo code.
     * 
     */
    public void setConsumerPaidAmt(Integer consumerPaidAmt) {
        this.consumerPaidAmt = consumerPaidAmt;
    }

    /**
     * This field contains the amount that the merchant is subsidizing after the
     * consumer applies a merchant-funded promo code.
     * 
     */
    public Integer getMerchantFundedPromoAmt() {
        return merchantFundedPromoAmt;
    }

    /**
     * This field contains the amount that the merchant is subsidizing after the
     * consumer applies a merchant-funded promo code.
     * 
     */
    public void setMerchantFundedPromoAmt(Integer merchantFundedPromoAmt) {
        this.merchantFundedPromoAmt = merchantFundedPromoAmt;
    }

    /**
     * This field contains the points multiplier applied in deriving the bonus
     * points issued to the consumer.
     * 
     */
    public String getPointsMultiplier() {
        return pointsMultiplier;
    }

    /**
     * This field contains the points multiplier applied in deriving the bonus
     * points issued to the consumer.
     * 
     */
    public void setPointsMultiplier(String pointsMultiplier) {
        this.pointsMultiplier = pointsMultiplier;
    }

    /**
     * This field contains the bonus points issued to the consumer, based on the
     * bonus multiplier applied.
     * 
     */
    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    /**
     * This field contains the bonus points issued to the consumer, based on the
     * bonus multiplier applied.
     * 
     */
    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    /**
     * promoRefundDetails
     * <p>
     * This is only applicable if the `txType` is REFUND.
     * 
     */
    public PromoRefund getPromoRefund() {
        return promoRefund;
    }

    /**
     * promoRefundDetails
     * <p>
     * This is only applicable if the `txType` is REFUND.
     * 
     */
    public void setPromoRefund(PromoRefund promoRefund) {
        this.promoRefund = promoRefund;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PromoDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("promoCode");
        sb.append('=');
        sb.append(((this.promoCode == null) ? "<null>" : this.promoCode));
        sb.append(',');
        sb.append("consumerPaidAmt");
        sb.append('=');
        sb.append(((this.consumerPaidAmt == null) ? "<null>" : this.consumerPaidAmt));
        sb.append(',');
        sb.append("merchantFundedPromoAmt");
        sb.append('=');
        sb.append(((this.merchantFundedPromoAmt == null) ? "<null>" : this.merchantFundedPromoAmt));
        sb.append(',');
        sb.append("pointsMultiplier");
        sb.append('=');
        sb.append(((this.pointsMultiplier == null) ? "<null>" : this.pointsMultiplier));
        sb.append(',');
        sb.append("pointsAwarded");
        sb.append('=');
        sb.append(((this.pointsAwarded == null) ? "<null>" : this.pointsAwarded));
        sb.append(',');
        sb.append("promoRefund");
        sb.append('=');
        sb.append(((this.promoRefund == null) ? "<null>" : this.promoRefund));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.merchantFundedPromoAmt == null) ? 0 : this.merchantFundedPromoAmt.hashCode()));
        result = ((result * 31) + ((this.promoRefund == null) ? 0 : this.promoRefund.hashCode()));
        result = ((result * 31) + ((this.pointsMultiplier == null) ? 0 : this.pointsMultiplier.hashCode()));
        result = ((result * 31) + ((this.pointsAwarded == null) ? 0 : this.pointsAwarded.hashCode()));
        result = ((result * 31) + ((this.promoCode == null) ? 0 : this.promoCode.hashCode()));
        result = ((result * 31) + ((this.consumerPaidAmt == null) ? 0 : this.consumerPaidAmt.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PromoDetails) == false) {
            return false;
        }
        PromoDetails rhs = ((PromoDetails) other);
        return (((((((this.merchantFundedPromoAmt == rhs.merchantFundedPromoAmt)
                || ((this.merchantFundedPromoAmt != null)
                        && this.merchantFundedPromoAmt.equals(rhs.merchantFundedPromoAmt)))
                && ((this.promoRefund == rhs.promoRefund)
                        || ((this.promoRefund != null) && this.promoRefund.equals(rhs.promoRefund))))
                && ((this.pointsMultiplier == rhs.pointsMultiplier)
                        || ((this.pointsMultiplier != null) && this.pointsMultiplier.equals(rhs.pointsMultiplier))))
                && ((this.pointsAwarded == rhs.pointsAwarded)
                        || ((this.pointsAwarded != null) && this.pointsAwarded.equals(rhs.pointsAwarded))))
                && ((this.promoCode == rhs.promoCode)
                        || ((this.promoCode != null) && this.promoCode.equals(rhs.promoCode))))
                && ((this.consumerPaidAmt == rhs.consumerPaidAmt)
                        || ((this.consumerPaidAmt != null) && this.consumerPaidAmt.equals(rhs.consumerPaidAmt))));
    }

}
