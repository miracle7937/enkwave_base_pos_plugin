package com.Enkpay.alltransctionsPOS.interswitch.models;

public enum MessageReasonCode {
    CustomerCancellation("4000"),
    UnSpecified("4001"),
    CompletedPartially("4004"),
    Timeout("4021");

    private final String code;

    MessageReasonCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
