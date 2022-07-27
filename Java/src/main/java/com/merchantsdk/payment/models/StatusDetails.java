package com.merchantsdk.payment.models;

/**
 * statusDetails
 * <p>
 * 
 * 
 */
public class StatusDetails {

    /**
     * status
     * <p>
     * This field contains the status of a transaction.
     * (Required)
     * 
     */
    private String status;
    /**
     * statusCode
     * <p>
     * This field contains the status code of a transaction. The status code will be
     * unique to each of the available status.
     * (Required)
     * 
     */
    private String statusCode;
    /**
     * statusReason
     * <p>
     * This field will provide the reason for the status of a transaction, and will
     * be useful for understanding the reason for non-successful transaction
     * statuses.
     * (Required)
     * 
     */
    private String statusReason;

    /**
     * status
     * <p>
     * This field contains the status of a transaction.
     * (Required)
     * 
     */
    public String getStatus() {
        return status;
    }

    /**
     * status
     * <p>
     * This field contains the status of a transaction.
     * (Required)
     * 
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * statusCode
     * <p>
     * This field contains the status code of a transaction. The status code will be
     * unique to each of the available status.
     * (Required)
     * 
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * statusCode
     * <p>
     * This field contains the status code of a transaction. The status code will be
     * unique to each of the available status.
     * (Required)
     * 
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * statusReason
     * <p>
     * This field will provide the reason for the status of a transaction, and will
     * be useful for understanding the reason for non-successful transaction
     * statuses.
     * (Required)
     * 
     */
    public String getStatusReason() {
        return statusReason;
    }

    /**
     * statusReason
     * <p>
     * This field will provide the reason for the status of a transaction, and will
     * be useful for understanding the reason for non-successful transaction
     * statuses.
     * (Required)
     * 
     */
    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatusDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null) ? "<null>" : this.status));
        sb.append(',');
        sb.append("statusCode");
        sb.append('=');
        sb.append(((this.statusCode == null) ? "<null>" : this.statusCode));
        sb.append(',');
        sb.append("statusReason");
        sb.append('=');
        sb.append(((this.statusReason == null) ? "<null>" : this.statusReason));
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
        result = ((result * 31) + ((this.statusReason == null) ? 0 : this.statusReason.hashCode()));
        result = ((result * 31) + ((this.status == null) ? 0 : this.status.hashCode()));
        result = ((result * 31) + ((this.statusCode == null) ? 0 : this.statusCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StatusDetails) == false) {
            return false;
        }
        StatusDetails rhs = ((StatusDetails) other);
        return ((((this.statusReason == rhs.statusReason)
                || ((this.statusReason != null) && this.statusReason.equals(rhs.statusReason)))
                && ((this.status == rhs.status) || ((this.status != null) && this.status.equals(rhs.status))))
                && ((this.statusCode == rhs.statusCode)
                        || ((this.statusCode != null) && this.statusCode.equals(rhs.statusCode))));
    }

}
