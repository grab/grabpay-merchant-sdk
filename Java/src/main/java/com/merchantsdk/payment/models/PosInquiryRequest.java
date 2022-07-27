package com.merchantsdk.payment.models;

import java.util.HashMap;
import java.util.Map;

/**
 * PosInqRequest
 * <p>
 * 
 * 
 */
public class PosInquiryRequest {
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
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * (Required)
         * 
         */
        private String storeGrabID;
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
         * Transaction type
         * <p>
         * This field will differentiate a payment inquiry from a refund inquiry.
         * (Required)
         * 
         */
        private TxType txType;
        /**
         * Transaction type
         * <p>
         * This field will differentiate the type of transaction reference that the
         * merchant is making an inquiry with.
         * (Required)
         * 
         */
        private TxRefType txRefType;
        /**
         * Transaction ID
         * <p>
         * GrabPay transaction ID that is formatted as 32-char UUID string without
         * dashes.
         * 
         * When initiating a refund of this transaction, you will need to specify this
         * `txID` to identify the transaction marked for refund.
         * 
         * (Required)
         * 
         */
        private String txID;

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
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * (Required)
         * 
         */
        public String getStoreGrabID() {
            return storeGrabID;
        }

        /**
         * storeGrabID
         * <p>
         * This field is the unique store identifier issued by Grab to the merchant.
         * (Required)
         * 
         */
        public void setStoreGrabID(String storeGrabID) {
            this.storeGrabID = storeGrabID;
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

        /**
         * Transaction type
         * <p>
         * This field will differentiate a payment inquiry from a refund inquiry.
         * (Required)
         * 
         */
        public TxType getTxType() {
            return txType;
        }

        /**
         * Transaction type
         * <p>
         * This field will differentiate a payment inquiry from a refund inquiry.
         * (Required)
         * 
         */
        public void setTxType(TxType txType) {
            this.txType = txType;
        }

        /**
         * Transaction type
         * <p>
         * This field will differentiate the type of transaction reference that the
         * merchant is making an inquiry with.
         * (Required)
         * 
         */
        public TxRefType getTxRefType() {
            return txRefType;
        }

        /**
         * Transaction type
         * <p>
         * This field will differentiate the type of transaction reference that the
         * merchant is making an inquiry with.
         * (Required)
         * 
         */
        public void setTxRefType(TxRefType txRefType) {
            this.txRefType = txRefType;
        }

        /**
         * Transaction ID
         * <p>
         * GrabPay transaction ID that is formatted as 32-char UUID string without
         * dashes.
         * 
         * When initiating a refund of this transaction, you will need to specify this
         * `txID` to identify the transaction marked for refund.
         * 
         * (Required)
         * 
         */
        public String getTxID() {
            return txID;
        }

        /**
         * Transaction ID
         * <p>
         * GrabPay transaction ID that is formatted as 32-char UUID string without
         * dashes.
         * 
         * When initiating a refund of this transaction, you will need to specify this
         * `txID` to identify the transaction marked for refund.
         * 
         * (Required)
         * 
         */
        public void setTxID(String txID) {
            this.txID = txID;
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
            sb.append("currency");
            sb.append('=');
            sb.append(((this.currency == null) ? "<null>" : this.currency));
            sb.append(',');
            sb.append("txType");
            sb.append('=');
            sb.append(((this.txType == null) ? "<null>" : this.txType));
            sb.append(',');
            sb.append("txRefType");
            sb.append('=');
            sb.append(((this.txRefType == null) ? "<null>" : this.txRefType));
            sb.append(',');
            sb.append("txID");
            sb.append('=');
            sb.append(((this.txID == null) ? "<null>" : this.txID));
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
            result = ((result * 31) + ((this.txRefType == null) ? 0 : this.txRefType.hashCode()));
            result = ((result * 31) + ((this.txID == null) ? 0 : this.txID.hashCode()));
            result = ((result * 31) + ((this.currency == null) ? 0 : this.currency.hashCode()));
            result = ((result * 31) + ((this.txType == null) ? 0 : this.txType.hashCode()));
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
            return (((((((this.txRefType == rhs.txRefType)
                    || ((this.txRefType != null) && this.txRefType.equals(rhs.txRefType)))
                    && ((this.txID == rhs.txID) || ((this.txID != null) && this.txID.equals(rhs.txID))))
                    && ((this.currency == rhs.currency)
                            || ((this.currency != null) && this.currency.equals(rhs.currency))))
                    && ((this.txType == rhs.txType) || ((this.txType != null) && this.txType.equals(rhs.txType))))
                    && ((this.paymentChannel == rhs.paymentChannel)
                            || ((this.paymentChannel != null) && this.paymentChannel.equals(rhs.paymentChannel))))
                    && ((this.storeGrabID == rhs.storeGrabID)
                            || ((this.storeGrabID != null) && this.storeGrabID.equals(rhs.storeGrabID))));
        }
    }

    /**
     * Transaction type
     * <p>
     * This field will differentiate the type of transaction reference that the
     * merchant is making an inquiry with.
     * 
     */
    public static enum TxRefType {

        GRABTXID("GRABTXID"),
        PARTNERTXID("PARTNERTXID");

        private final String value;
        private final static Map<String, TxRefType> CONSTANTS = new HashMap<String, TxRefType>();

        static {
            for (TxRefType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TxRefType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TxRefType fromValue(String value) {
            TxRefType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    /**
     * Transaction type
     * <p>
     * This field will differentiate a payment inquiry from a refund inquiry.
     * 
     */
    public static enum TxType {

        PAYMENT("PAYMENT"),
        REFUND("REFUND");

        private final String value;
        private final static Map<String, TxType> CONSTANTS = new HashMap<String, TxType>();

        static {
            for (TxType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TxType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TxType fromValue(String value) {
            TxType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
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
        sb.append(PosInquiryRequest.class.getName()).append('@')
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
        if ((other instanceof PosInquiryRequest) == false) {
            return false;
        }
        PosInquiryRequest rhs = ((PosInquiryRequest) other);
        return ((this.transactionDetails == rhs.transactionDetails)
                || ((this.transactionDetails != null) && this.transactionDetails.equals(rhs.transactionDetails)));
    }

}
