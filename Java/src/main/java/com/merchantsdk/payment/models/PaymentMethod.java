package com.merchantsdk.payment.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentMethod {

    /**
     * paymentMethodExclusion
     * <p>
     * This field will allow merchants to specify specific payment methods to
     * exclude during consumer selection. Do note that default wallet GPWALLET
     * cannot be excluded.
     * If this field is not sent in the request, all available payment methods to
     * the consumer and the merchant will be offered for consumer selection.
     * 
     */
    private PaymentMethod.PaymentMethodExclusion paymentMethodExclusion;
    /**
     * minAmtPostpaid
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater Postpaid as a payment method.
     * 
     */
    private Integer minAmtPostpaid;
    /**
     * minAmt4Instalment
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater 4 Instalments as a payment
     * method.
     * 
     */
    private Integer minAmt4Instalment;

    /**
     * paymentMethodExclusion
     * <p>
     * This field will allow merchants to specify specific payment methods to
     * exclude during consumer selection. Do note that default wallet GPWALLET
     * cannot be excluded.
     * If this field is not sent in the request, all available payment methods to
     * the consumer and the merchant will be offered for consumer selection.
     * 
     */
    public PaymentMethod.PaymentMethodExclusion getPaymentMethodExclusion() {
        return paymentMethodExclusion;
    }

    /**
     * paymentMethodExclusion
     * <p>
     * This field will allow merchants to specify specific payment methods to
     * exclude during consumer selection. Do note that default wallet GPWALLET
     * cannot be excluded.
     * If this field is not sent in the request, all available payment methods to
     * the consumer and the merchant will be offered for consumer selection.
     * 
     */
    public void setPaymentMethodExclusion(PaymentMethod.PaymentMethodExclusion paymentMethodExclusion) {
        this.paymentMethodExclusion = paymentMethodExclusion;
    }

    /**
     * minAmtPostpaid
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater Postpaid as a payment method.
     * 
     */
    public Integer getMinAmtPostpaid() {
        return minAmtPostpaid;
    }

    /**
     * minAmtPostpaid
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater Postpaid as a payment method.
     * 
     */
    public void setMinAmtPostpaid(Integer minAmtPostpaid) {
        this.minAmtPostpaid = minAmtPostpaid;
    }

    /**
     * minAmt4Instalment
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater 4 Instalments as a payment
     * method.
     * 
     */
    public Integer getMinAmt4Instalment() {
        return minAmt4Instalment;
    }

    /**
     * minAmt4Instalment
     * <p>
     * This field will allow merchants to specify the minimum amount before
     * consumers are allowed to pay using PayLater 4 Instalments as a payment
     * method.
     * 
     */
    public void setMinAmt4Instalment(Integer minAmt4Instalment) {
        this.minAmt4Instalment = minAmt4Instalment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PaymentMethod.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("paymentMethodExclusion");
        sb.append('=');
        sb.append(((this.paymentMethodExclusion == null) ? "<null>" : this.paymentMethodExclusion));
        sb.append(',');
        sb.append("minAmtPostpaid");
        sb.append('=');
        sb.append(((this.minAmtPostpaid == null) ? "<null>" : this.minAmtPostpaid));
        sb.append(',');
        sb.append("minAmt4Instalment");
        sb.append('=');
        sb.append(((this.minAmt4Instalment == null) ? "<null>" : this.minAmt4Instalment));
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
        result = ((result * 31) + ((this.minAmtPostpaid == null) ? 0 : this.minAmtPostpaid.hashCode()));
        result = ((result * 31) + ((this.paymentMethodExclusion == null) ? 0 : this.paymentMethodExclusion.hashCode()));
        result = ((result * 31) + ((this.minAmt4Instalment == null) ? 0 : this.minAmt4Instalment.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaymentMethod) == false) {
            return false;
        }
        PaymentMethod rhs = ((PaymentMethod) other);
        return ((((this.minAmtPostpaid == rhs.minAmtPostpaid)
                || ((this.minAmtPostpaid != null) && this.minAmtPostpaid.equals(rhs.minAmtPostpaid)))
                && ((this.paymentMethodExclusion == rhs.paymentMethodExclusion)
                        || ((this.paymentMethodExclusion != null)
                                && this.paymentMethodExclusion.equals(rhs.paymentMethodExclusion))))
                && ((this.minAmt4Instalment == rhs.minAmt4Instalment)
                        || ((this.minAmt4Instalment != null) && this.minAmt4Instalment.equals(rhs.minAmt4Instalment))));
    }

    /**
     * paymentMethodExclusion
     * <p>
     * This field will allow merchants to specify specific payment methods to
     * exclude during consumer selection. Do note that default wallet GPWALLET
     * cannot be excluded.
     * If this field is not sent in the request, all available payment methods to
     * the consumer and the merchant will be offered for consumer selection.
     * 
     */
    public enum PaymentMethodExclusion {

        POSTPAID(null),
        INSTALMENT_4(null);

        private final List<Object> value;
        private final static Map<List<Object>, PaymentMethod.PaymentMethodExclusion> CONSTANTS = new HashMap<List<Object>, PaymentMethod.PaymentMethodExclusion>();

        static {
            for (PaymentMethod.PaymentMethodExclusion c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PaymentMethodExclusion(List<Object> value) {
            this.value = value;
        }

        public List<Object> value() {
            return this.value;
        }

        public static PaymentMethod.PaymentMethodExclusion fromValue(List<Object> value) {
            PaymentMethod.PaymentMethodExclusion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException((value + ""));
            } else {
                return constant;
            }
        }

    }

}
