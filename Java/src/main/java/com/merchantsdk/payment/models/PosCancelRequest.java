package com.merchantsdk.payment.models;

/**
 * PosCancelRequest
 * <p>
 * 
 * 
 */
public class PosCancelRequest {
    public static class TransactionDetails {

        /**
         * Payment channel
         * <p>
         * This field will be used to differentiate the different POS payment channels,
         * and will determine which other Conditional parameters will need to be
         * required in the request, or processed in the response.
         * (Required)
         * 
         */
        private PosPaymentChannel paymentChannel;

        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * (Required)
         * 
         */
        private String originPartnerTxID;
        /**
         * currency
         * <p>
         * Currency that is associated with the payment amount. Specify the three-letter
         * [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) currency code.
         * 
         * (Required)
         * 
         */
        private Currency currency;

        /**
         * Payment channel
         * <p>
         * This field will be used to differentiate the different POS payment channels,
         * and will determine which other Conditional parameters will need to be
         * required in the request, or processed in the response.
         * (Required)
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
         * (Required)
         * 
         */
        public void setPaymentChannel(PosPaymentChannel paymentChannel) {
            this.paymentChannel = paymentChannel;
        }

        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * (Required)
         * 
         */
        public String getOriginPartnerTxID() {
            return originPartnerTxID;
        }

        /**
         * This field contains the original payment's merchant's primary transaction
         * reference.
         * (Required)
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
         * (Required)
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
         * (Required)
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
                            || ((this.paymentChannel != null) && this.paymentChannel.equals(rhs.paymentChannel)))));
        }

    }

    private TransactionDetails transactionDetails;

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PosCancelRequest.class.getName()).append('@')
                .append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("transactionDetails");
        sb.append('=');
        sb.append(((this.transactionDetails == null) ? "<null>" : this.transactionDetails));
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PosCancelRequest) == false) {
            return false;
        }
        PosCancelRequest rhs = ((PosCancelRequest) other);
        return ((this.transactionDetails == rhs.transactionDetails)
                || ((this.transactionDetails != null) && this.transactionDetails.equals(rhs.transactionDetails)));
    }

}
