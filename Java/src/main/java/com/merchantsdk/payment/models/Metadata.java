package com.merchantsdk.payment.models;

/**
 * metadata
 * <p>
 * 
 * 
 */
public class Metadata {

    /**
     * This field contains the OFFUS transaction ID issued when a consumer uses a
     * non-Grab wallet to make a payment to a merchant's OFFUS enabled QR code.
     * Condition: "paymentChannel": "MPQR" "paymentMethod": ("PAYNOW", "DUITNOW",
     * "QRPH")
     * 
     * 
     */
    private String offusTxID;
    /**
     * This field contains the OFFUS transaction ID issued for the original OFFUS
     * payment when a merchant makes a refund to a non-Grab wallet. Condition:
     * "paymentChannel": "MPQR" "txType": "REFUND" "paymentMethod": ("PAYNOW",
     * "DUITNOW", "QRPH")
     * 
     * 
     */
    private String originOffusTxID;

    /**
     * This field contains the OFFUS transaction ID issued when a consumer uses a
     * non-Grab wallet to make a payment to a merchant's OFFUS enabled QR code.
     * Condition: "paymentChannel": "MPQR" "paymentMethod": ("PAYNOW", "DUITNOW",
     * "QRPH")
     * 
     * 
     */
    public String getOffusTxID() {
        return offusTxID;
    }

    /**
     * This field contains the OFFUS transaction ID issued when a consumer uses a
     * non-Grab wallet to make a payment to a merchant's OFFUS enabled QR code.
     * Condition: "paymentChannel": "MPQR" "paymentMethod": ("PAYNOW", "DUITNOW",
     * "QRPH")
     * 
     * 
     */
    public void setOffusTxID(String offusTxID) {
        this.offusTxID = offusTxID;
    }

    /**
     * This field contains the OFFUS transaction ID issued for the original OFFUS
     * payment when a merchant makes a refund to a non-Grab wallet. Condition:
     * "paymentChannel": "MPQR" "txType": "REFUND" "paymentMethod": ("PAYNOW",
     * "DUITNOW", "QRPH")
     * 
     * 
     */
    public String getOriginOffusTxID() {
        return originOffusTxID;
    }

    /**
     * This field contains the OFFUS transaction ID issued for the original OFFUS
     * payment when a merchant makes a refund to a non-Grab wallet. Condition:
     * "paymentChannel": "MPQR" "txType": "REFUND" "paymentMethod": ("PAYNOW",
     * "DUITNOW", "QRPH")
     * 
     * 
     */
    public void setOriginOffusTxID(String originOffusTxID) {
        this.originOffusTxID = originOffusTxID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Metadata.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("offusTxID");
        sb.append('=');
        sb.append(((this.offusTxID == null) ? "<null>" : this.offusTxID));
        sb.append(',');
        sb.append("originOffusTxID");
        sb.append('=');
        sb.append(((this.originOffusTxID == null) ? "<null>" : this.originOffusTxID));
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
        result = ((result * 31) + ((this.originOffusTxID == null) ? 0 : this.originOffusTxID.hashCode()));
        result = ((result * 31) + ((this.offusTxID == null) ? 0 : this.offusTxID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Metadata) == false) {
            return false;
        }
        Metadata rhs = ((Metadata) other);
        return (((this.originOffusTxID == rhs.originOffusTxID)
                || ((this.originOffusTxID != null) && this.originOffusTxID.equals(rhs.originOffusTxID)))
                && ((this.offusTxID == rhs.offusTxID)
                        || ((this.offusTxID != null) && this.offusTxID.equals(rhs.offusTxID))));
    }

}
