package com.merchantsdk.payment.models;

public class PosDetailsResponse {

    private String terminalID;

    private String qrPayload;

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getQrPayload() {
        return qrPayload;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PosDetailsResponse.class.getName()).append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("terminalID");
        sb.append('=');
        sb.append(((this.terminalID == null) ? "<null>" : this.terminalID));
        sb.append(',');
        sb.append("qrPayload");
        sb.append('=');
        sb.append(((this.qrPayload == null) ? "<null>" : this.qrPayload));
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
        result = ((result * 31) + ((this.terminalID == null) ? 0 : this.terminalID.hashCode()));
        result = ((result * 31) + ((this.qrPayload == null) ? 0 : this.qrPayload.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PosDetailsResponse) == false) {
            return false;
        }
        PosDetailsResponse rhs = ((PosDetailsResponse) other);
        return ((this.terminalID == rhs.terminalID)
                || ((this.terminalID != null) && this.terminalID.equals(rhs.terminalID)))
                && ((this.qrPayload == rhs.qrPayload)
                        || ((this.qrPayload != null) && this.qrPayload.equals(rhs.qrPayload)));
    }

}