package com.merchantsdk.payment.models;

/**
 * originTxDetails
 * <p>
 * This object is only applicable if the `txType` is REFUND.
 * 
 */
public class OriginTxDetails {

    /**
     * This field contains the original payment's Grab's transaction reference, if
     * an inquiry is made on a refund.
     * 
     */
    private String originGrabTxID;
    /**
     * This field contains the original payment's merchant's primary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    private String originPartnerTxID;
    /**
     * This field contains the original payment's merchant's secondary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    private String originPartnerGroupTxID;
    /**
     * This field contains the original payment's transaction amount, if an inquiry
     * is made on a refund.
     * 
     */
    private Integer originAmount;

    /**
     * This field contains the original payment's Grab's transaction reference, if
     * an inquiry is made on a refund.
     * 
     */
    public String getOriginGrabTxID() {
        return originGrabTxID;
    }

    /**
     * This field contains the original payment's Grab's transaction reference, if
     * an inquiry is made on a refund.
     * 
     */
    public void setOriginGrabTxID(String originGrabTxID) {
        this.originGrabTxID = originGrabTxID;
    }

    /**
     * This field contains the original payment's merchant's primary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    public String getOriginPartnerTxID() {
        return originPartnerTxID;
    }

    /**
     * This field contains the original payment's merchant's primary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    public void setOriginPartnerTxID(String originPartnerTxID) {
        this.originPartnerTxID = originPartnerTxID;
    }

    /**
     * This field contains the original payment's merchant's secondary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    public String getOriginPartnerGroupTxID() {
        return originPartnerGroupTxID;
    }

    /**
     * This field contains the original payment's merchant's secondary transaction
     * reference, if an inquiry is made on a refund.
     * 
     */
    public void setOriginPartnerGroupTxID(String originPartnerGroupTxID) {
        this.originPartnerGroupTxID = originPartnerGroupTxID;
    }

    /**
     * This field contains the original payment's transaction amount, if an inquiry
     * is made on a refund.
     * 
     */
    public Integer getOriginAmount() {
        return originAmount;
    }

    /**
     * This field contains the original payment's transaction amount, if an inquiry
     * is made on a refund.
     * 
     */
    public void setOriginAmount(Integer originAmount) {
        this.originAmount = originAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OriginTxDetails.class.getName()).append('@')
                .append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("originGrabTxID");
        sb.append('=');
        sb.append(((this.originGrabTxID == null) ? "<null>" : this.originGrabTxID));
        sb.append(',');
        sb.append("originPartnerTxID");
        sb.append('=');
        sb.append(((this.originPartnerTxID == null) ? "<null>" : this.originPartnerTxID));
        sb.append(',');
        sb.append("originPartnerGroupTxID");
        sb.append('=');
        sb.append(((this.originPartnerGroupTxID == null) ? "<null>" : this.originPartnerGroupTxID));
        sb.append(',');
        sb.append("originAmount");
        sb.append('=');
        sb.append(((this.originAmount == null) ? "<null>" : this.originAmount));
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
        result = ((result * 31) + ((this.originAmount == null) ? 0 : this.originAmount.hashCode()));
        result = ((result * 31) + ((this.originGrabTxID == null) ? 0 : this.originGrabTxID.hashCode()));
        result = ((result * 31) + ((this.originPartnerGroupTxID == null) ? 0 : this.originPartnerGroupTxID.hashCode()));
        result = ((result * 31) + ((this.originPartnerTxID == null) ? 0 : this.originPartnerTxID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OriginTxDetails) == false) {
            return false;
        }
        OriginTxDetails rhs = ((OriginTxDetails) other);
        return (((((this.originAmount == rhs.originAmount)
                || ((this.originAmount != null) && this.originAmount.equals(rhs.originAmount)))
                && ((this.originGrabTxID == rhs.originGrabTxID)
                        || ((this.originGrabTxID != null) && this.originGrabTxID.equals(rhs.originGrabTxID))))
                && ((this.originPartnerGroupTxID == rhs.originPartnerGroupTxID)
                        || ((this.originPartnerGroupTxID != null)
                                && this.originPartnerGroupTxID.equals(rhs.originPartnerGroupTxID))))
                && ((this.originPartnerTxID == rhs.originPartnerTxID)
                        || ((this.originPartnerTxID != null) && this.originPartnerTxID.equals(rhs.originPartnerTxID))));
    }

}
