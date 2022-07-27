package com.merchantsdk.payment.models;

/**
 * PosCancelResponse
 * <p>
 * 
 * 
 */
public class PosCancelResponse {
    public static class TransactionDetails {

        /**
         * Payment channel
         * <p>
         * This field will be used to differentiate the different POS payment channels,
         * and will determine which other Conditional parameters will need to be
         * required in the request, or processed in the response.
         * 
         */
        private PosPaymentChannel paymentChannel;
        /**
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * 
         */
        private String storeGrabID;
        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * 
         */
        private String originPartnerTxID;
        /**
         * currency
         * <p>
         * Currency that is associated with the payment amount. Specify the three-letter
         * [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) currency code.
         * 
         * 
         */
        private Currency currency;

        /**
         * Payment channel
         * <p>
         * This field will be used to differentiate the different POS payment channels,
         * and will determine which other Conditional parameters will need to be
         * required in the request, or processed in the response.
         * 
         */
        public PosPaymentChannel getPaymentChannel() {
            return paymentChannel;
        }

        /**
         * Payment channel
         * <p>
         * This field will be used to differentiate the different POS payment channels,
         * and will determine which other Conditional parameters will need to be
         * required in the request, or processed in the response.
         * 
         */
        public void setPaymentChannel(PosPaymentChannel paymentChannel) {
            this.paymentChannel = paymentChannel;
        }

        /**
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * 
         */
        public String getStoreGrabID() {
            return storeGrabID;
        }

        /**
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * 
         */
        public void setStoreGrabID(String storeGrabID) {
            this.storeGrabID = storeGrabID;
        }

        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * 
         */
        public String getOriginPartnerTxID() {
            return originPartnerTxID;
        }

        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * 
         */
        public void setOriginPartnerTxID(String originPartnerTxID) {
            this.originPartnerTxID = originPartnerTxID;
        }

        /**
         * currency
         * <p>
         * Currency that is associated with the payment amount. Specify the three-letter
         * [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) currency code.
         * 
         * 
         */
        public Currency getCurrency() {
            return currency;
        }

        /**
         * currency
         * <p>
         * Currency that is associated with the payment amount. Specify the three-letter
         * [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) currency code.
         * 
         * 
         */
        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(TransactionDetails.class.getName()).append('@')
                    .append(Integer.toHexString(System.identityHashCode(this))).append('[');
            sb.append("paymentChannel");
            sb.append('=');
            sb.append(((this.paymentChannel == null) ? "<null>" : this.paymentChannel));
            sb.append(',');
            sb.append("storeGrabID");
            sb.append('=');
            sb.append(((this.storeGrabID == null) ? "<null>" : this.storeGrabID));
            sb.append(',');
            sb.append("originPartnerTxID");
            sb.append('=');
            sb.append(((this.originPartnerTxID == null) ? "<null>" : this.originPartnerTxID));
            sb.append(',');
            sb.append("currency");
            sb.append('=');
            sb.append(((this.currency == null) ? "<null>" : this.currency));
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
            result = ((result * 31) + ((this.currency == null) ? 0 : this.currency.hashCode()));
            result = ((result * 31) + ((this.originPartnerTxID == null) ? 0 : this.originPartnerTxID.hashCode()));
            result = ((result * 31) + ((this.paymentChannel == null) ? 0 : this.paymentChannel.hashCode()));
            result = ((result * 31) + ((this.storeGrabID == null) ? 0 : this.storeGrabID.hashCode()));
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof TransactionDetails) == false) {
                return false;
            }
            TransactionDetails rhs = ((TransactionDetails) other);
            return (((((this.currency == rhs.currency)
                    || ((this.currency != null) && this.currency.equals(rhs.currency)))
                    && ((this.originPartnerTxID == rhs.originPartnerTxID)
                            || ((this.originPartnerTxID != null)
                                    && this.originPartnerTxID.equals(rhs.originPartnerTxID))))
                    && ((this.paymentChannel == rhs.paymentChannel)
                            || ((this.paymentChannel != null) && this.paymentChannel.equals(rhs.paymentChannel))))
                    && ((this.storeGrabID == rhs.storeGrabID)
                            || ((this.storeGrabID != null) && this.storeGrabID.equals(rhs.storeGrabID))));
        }

    }

    private TransactionDetails transactionDetails;
    /**
     * statusDetails
     * <p>
     * 
     * 
     */
    private StatusDetails statusDetails;

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    /**
     * statusDetails
     * <p>
     * 
     * 
     */
    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    /**
     * statusDetails
     * <p>
     * 
     * 
     */
    public void setStatusDetails(StatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PosCancelResponse.class.getName()).append('@')
                .append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("transactionDetails");
        sb.append('=');
        sb.append(((this.transactionDetails == null) ? "<null>" : this.transactionDetails));
        sb.append(',');
        sb.append("statusDetails");
        sb.append('=');
        sb.append(((this.statusDetails == null) ? "<null>" : this.statusDetails));
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
        result = ((result * 31) + ((this.transactionDetails == null) ? 0 : this.transactionDetails.hashCode()));
        result = ((result * 31) + ((this.statusDetails == null) ? 0 : this.statusDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PosCancelResponse) == false) {
            return false;
        }
        PosCancelResponse rhs = ((PosCancelResponse) other);
        return (((this.transactionDetails == rhs.transactionDetails)
                || ((this.transactionDetails != null) && this.transactionDetails.equals(rhs.transactionDetails)))
                && ((this.statusDetails == rhs.statusDetails)
                        || ((this.statusDetails != null) && this.statusDetails.equals(rhs.statusDetails))));
    }

}
