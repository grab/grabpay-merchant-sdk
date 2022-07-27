package com.merchantsdk.payment.models;

/**
 * promoRefundDetails
 * <p>
 * This is only applicable if the `txType` is REFUND.
 * 
 */
public class PromoRefund {

    /**
     * This field contains the amount that was refunded to the consumer during a
     * refund.
     * 
     */
    private Integer consumerRefundedAmt;
    /**
     * This field contains that amount that is not refunded to the consumer because
     * the merchant has subsidized it during payment as part of a merchant-funded
     * promo.
     * 
     */
    private Integer merchantFundedPromoRefundAmt;
    /**
     * This field contains the bonus points that was clawed back from the consumer
     * during a refund.
     * 
     */
    private Integer pointsRefunded;

    /**
     * This field contains the amount that was refunded to the consumer during a
     * refund.
     * 
     */
    public Integer getConsumerRefundedAmt() {
        return consumerRefundedAmt;
    }

    /**
     * This field contains the amount that was refunded to the consumer during a
     * refund.
     * 
     */
    public void setConsumerRefundedAmt(Integer consumerRefundedAmt) {
        this.consumerRefundedAmt = consumerRefundedAmt;
    }

    /**
     * This field contains that amount that is not refunded to the consumer because
     * the merchant has subsidized it during payment as part of a merchant-funded
     * promo.
     * 
     */
    public Integer getMerchantFundedPromoRefundAmt() {
        return merchantFundedPromoRefundAmt;
    }

    /**
     * This field contains that amount that is not refunded to the consumer because
     * the merchant has subsidized it during payment as part of a merchant-funded
     * promo.
     * 
     */
    public void setMerchantFundedPromoRefundAmt(Integer merchantFundedPromoRefundAmt) {
        this.merchantFundedPromoRefundAmt = merchantFundedPromoRefundAmt;
    }

    /**
     * This field contains the bonus points that was clawed back from the consumer
     * during a refund.
     * 
     */
    public Integer getPointsRefunded() {
        return pointsRefunded;
    }

    /**
     * This field contains the bonus points that was clawed back from the consumer
     * during a refund.
     * 
     */
    public void setPointsRefunded(Integer pointsRefunded) {
        this.pointsRefunded = pointsRefunded;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PromoRefund.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("consumerRefundedAmt");
        sb.append('=');
        sb.append(((this.consumerRefundedAmt == null) ? "<null>" : this.consumerRefundedAmt));
        sb.append(',');
        sb.append("merchantFundedPromoRefundAmt");
        sb.append('=');
        sb.append(((this.merchantFundedPromoRefundAmt == null) ? "<null>" : this.merchantFundedPromoRefundAmt));
        sb.append(',');
        sb.append("pointsRefunded");
        sb.append('=');
        sb.append(((this.pointsRefunded == null) ? "<null>" : this.pointsRefunded));
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
        result = ((result * 31)
                + ((this.merchantFundedPromoRefundAmt == null) ? 0 : this.merchantFundedPromoRefundAmt.hashCode()));
        result = ((result * 31) + ((this.pointsRefunded == null) ? 0 : this.pointsRefunded.hashCode()));
        result = ((result * 31) + ((this.consumerRefundedAmt == null) ? 0 : this.consumerRefundedAmt.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PromoRefund) == false) {
            return false;
        }
        PromoRefund rhs = ((PromoRefund) other);
        return ((((this.merchantFundedPromoRefundAmt == rhs.merchantFundedPromoRefundAmt)
                || ((this.merchantFundedPromoRefundAmt != null)
                        && this.merchantFundedPromoRefundAmt.equals(rhs.merchantFundedPromoRefundAmt)))
                && ((this.pointsRefunded == rhs.pointsRefunded)
                        || ((this.pointsRefunded != null) && this.pointsRefunded.equals(rhs.pointsRefunded))))
                && ((this.consumerRefundedAmt == rhs.consumerRefundedAmt) || ((this.consumerRefundedAmt != null)
                        && this.consumerRefundedAmt.equals(rhs.consumerRefundedAmt))));
    }

}
