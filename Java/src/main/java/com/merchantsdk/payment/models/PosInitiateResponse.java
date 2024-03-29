package com.merchantsdk.payment.models;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * PosInitResponse
 * <p>
 * 
 * 
 */
public class PosInitiateResponse {
    public class TransactionDetails {

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
         * grabTxID
         * <p>
         * This field contains the unique transaction reference number issued by Grab.
         * This field will be idempotent to Grab.
         * 
         */
        private String grabTxID;
        /**
         * paymentTxID
         * <p>
         * The partner's transaction ID.
         * 
         */
        private String partnerTxID;
        /**
         * partnerGroupTxID
         * <p>
         * This is the unique identifier of each transaction generated by the partner.
         * Each transaction might consist of several charges. Usually the value is a
         * single receipt ID from the partner and can be displayed to the user.
         * 
         */
        private String partnerGroupTxID;
        /**
         * billRefNumber
         * <p>
         * This field can be used to define the Bill Reference Number when a merchant is
         * receiving payments via a Grab wallet, or non-Grab wallet. When this field is
         * not sent in the request, consumers will be able to enter the reference number
         * themselves in the respective payment app.
         * 
         */
        private String billRefNumber;
        /**
         * amount
         * <p>
         * Transaction amount as integer. A positive integer in the smallest currency
         * unit (e.g., 100 cents to charge S$1.00, a zero-decimal currency). For
         * example, S$5.34 will be represented as 534.
         * 
         */
        private Long amount;
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
         * paymentExpiryTime
         * <p>
         * This will allow both the merchant's and Grab's systems to expire the
         * transaction after a specified time. This field will be in Unix Epoch time.
         * 
         */
        private Long paymentExpiryTime;

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
         * grabTxID
         * <p>
         * This field contains the unique transaction reference number issued by Grab.
         * This field will be idempotent to Grab.
         * 
         */
        public String getGrabTxID() {
            return grabTxID;
        }

        /**
         * grabTxID
         * <p>
         * This field contains the unique transaction reference number issued by Grab.
         * This field will be idempotent to Grab.
         * 
         */
        public void setGrabTxID(String grabTxID) {
            this.grabTxID = grabTxID;
        }

        /**
         * paymentTxID
         * <p>
         * The partner's transaction ID.
         * 
         */
        public String getPartnerTxID() {
            return partnerTxID;
        }

        /**
         * paymentTxID
         * <p>
         * The partner's transaction ID.
         * 
         */
        public void setPartnerTxID(String partnerTxID) {
            this.partnerTxID = partnerTxID;
        }

        /**
         * partnerGroupTxID
         * <p>
         * This is the unique identifier of each transaction generated by the partner.
         * Each transaction might consist of several charges. Usually the value is a
         * single receipt ID from the partner and can be displayed to the user.
         * 
         */
        public String getPartnerGroupTxID() {
            return partnerGroupTxID;
        }

        /**
         * partnerGroupTxID
         * <p>
         * This is the unique identifier of each transaction generated by the partner.
         * Each transaction might consist of several charges. Usually the value is a
         * single receipt ID from the partner and can be displayed to the user.
         * 
         */
        public void setPartnerGroupTxID(String partnerGroupTxID) {
            this.partnerGroupTxID = partnerGroupTxID;
        }

        /**
         * billRefNumber
         * <p>
         * This field can be used to define the Bill Reference Number when a merchant is
         * receiving payments via a Grab wallet, or non-Grab wallet. When this field is
         * not sent in the request, consumers will be able to enter the reference number
         * themselves in the respective payment app.
         * 
         */
        public String getBillRefNumber() {
            return billRefNumber;
        }

        /**
         * billRefNumber
         * <p>
         * This field can be used to define the Bill Reference Number when a merchant is
         * receiving payments via a Grab wallet, or non-Grab wallet. When this field is
         * not sent in the request, consumers will be able to enter the reference number
         * themselves in the respective payment app.
         * 
         */
        public void setBillRefNumber(String billRefNumber) {
            this.billRefNumber = billRefNumber;
        }

        /**
         * amount
         * <p>
         * Transaction amount as integer. A positive integer in the smallest currency
         * unit (e.g., 100 cents to charge S$1.00, a zero-decimal currency). For
         * example, S$5.34 will be represented as 534.
         * 
         */
        public Long getAmount() {
            return amount;
        }

        /**
         * amount
         * <p>
         * Transaction amount as integer. A positive integer in the smallest currency
         * unit (e.g., 100 cents to charge S$1.00, a zero-decimal currency). For
         * example, S$5.34 will be represented as 534.
         * 
         */
        public void setAmount(Long amount) {
            this.amount = amount;
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

        /**
         * paymentExpiryTime
         * <p>
         * This will allow both the merchant's and Grab's systems to expire the
         * transaction after a specified time. This field will be in Unix Epoch time.
         * 
         */
        public Long getPaymentExpiryTime() {
            return paymentExpiryTime;
        }

        /**
         * paymentExpiryTime
         * <p>
         * This will allow both the merchant's and Grab's systems to expire the
         * transaction after a specified time. This field will be in Unix Epoch time.
         * 
         */
        public void setPaymentExpiryTime(Long paymentExpiryTime) {
            this.paymentExpiryTime = paymentExpiryTime;
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
            sb.append("grabTxID");
            sb.append('=');
            sb.append(((this.grabTxID == null) ? "<null>" : this.grabTxID));
            sb.append(',');
            sb.append("partnerTxID");
            sb.append('=');
            sb.append(((this.partnerTxID == null) ? "<null>" : this.partnerTxID));
            sb.append(',');
            sb.append("partnerGroupTxID");
            sb.append('=');
            sb.append(((this.partnerGroupTxID == null) ? "<null>" : this.partnerGroupTxID));
            sb.append(',');
            sb.append("billRefNumber");
            sb.append('=');
            sb.append(((this.billRefNumber == null) ? "<null>" : this.billRefNumber));
            sb.append(',');
            sb.append("amount");
            sb.append('=');
            sb.append(((this.amount == null) ? "<null>" : this.amount));
            sb.append(',');
            sb.append("currency");
            sb.append('=');
            sb.append(((this.currency == null) ? "<null>" : this.currency));
            sb.append(',');
            sb.append("paymentExpiryTime");
            sb.append('=');
            sb.append(((this.paymentExpiryTime == null) ? "<null>" : this.paymentExpiryTime));
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
            result = ((result * 31) + ((this.amount == null) ? 0 : this.amount.hashCode()));
            result = ((result * 31) + ((this.grabTxID == null) ? 0 : this.grabTxID.hashCode()));
            result = ((result * 31) + ((this.currency == null) ? 0 : this.currency.hashCode()));
            result = ((result * 31) + ((this.paymentExpiryTime == null) ? 0 : this.paymentExpiryTime.hashCode()));
            result = ((result * 31) + ((this.billRefNumber == null) ? 0 : this.billRefNumber.hashCode()));
            result = ((result * 31) + ((this.partnerTxID == null) ? 0 : this.partnerTxID.hashCode()));
            result = ((result * 31) + ((this.partnerGroupTxID == null) ? 0 : this.partnerGroupTxID.hashCode()));
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
            return ((((((((((this.amount == rhs.amount) || ((this.amount != null) && this.amount.equals(rhs.amount)))
                    && ((this.grabTxID == rhs.grabTxID)
                            || ((this.grabTxID != null) && this.grabTxID.equals(rhs.grabTxID))))
                    && ((this.currency == rhs.currency)
                            || ((this.currency != null) && this.currency.equals(rhs.currency))))
                    && ((this.paymentExpiryTime == rhs.paymentExpiryTime)
                            || ((this.paymentExpiryTime != null)
                                    && this.paymentExpiryTime.equals(rhs.paymentExpiryTime))))
                    && ((this.billRefNumber == rhs.billRefNumber)
                            || ((this.billRefNumber != null) && this.billRefNumber.equals(rhs.billRefNumber))))
                    && ((this.partnerTxID == rhs.partnerTxID)
                            || ((this.partnerTxID != null) && this.partnerTxID.equals(rhs.partnerTxID))))
                    && ((this.partnerGroupTxID == rhs.partnerGroupTxID)
                            || ((this.partnerGroupTxID != null) && this.partnerGroupTxID.equals(rhs.partnerGroupTxID))))
                    && ((this.paymentChannel == rhs.paymentChannel)
                            || ((this.paymentChannel != null) && this.paymentChannel.equals(rhs.paymentChannel))))
                    && ((this.storeGrabID == rhs.storeGrabID)
                            || ((this.storeGrabID != null) && this.storeGrabID.equals(rhs.storeGrabID))));
        }

    }

    private TransactionDetails transactionDetails;
    private PosDetailsResponse posDetails;
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

    public PosDetailsResponse getPosDetails() {
        return posDetails;
    }

    /**
     * 
     * @param posDetails
     */
    @JsonSetter("POSDetails")
    public void setPosDetails(PosDetailsResponse posDetails) {
        this.posDetails = posDetails;
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
        sb.append(PosInitiateResponse.class.getName()).append('@')
                .append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("transactionDetails");
        sb.append('=');
        sb.append(((this.transactionDetails == null) ? "<null>" : this.transactionDetails));
        sb.append(',');
        sb.append("posDetails");
        sb.append('=');
        sb.append(((this.posDetails == null) ? "<null>" : this.posDetails));
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
        result = ((result * 31) + ((this.posDetails == null) ? 0 : this.posDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PosInitiateResponse) == false) {
            return false;
        }
        PosInitiateResponse rhs = ((PosInitiateResponse) other);
        return ((((this.transactionDetails == rhs.transactionDetails)
                || ((this.transactionDetails != null) && this.transactionDetails.equals(rhs.transactionDetails)))
                && ((this.statusDetails == rhs.statusDetails)
                        || ((this.statusDetails != null) && this.statusDetails.equals(rhs.statusDetails))))
                && ((this.posDetails == rhs.posDetails)
                        || ((this.posDetails != null) && this.posDetails.equals(rhs.posDetails))));
    }

}
