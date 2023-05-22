package com.Enkpay.alltransctionsPOS.interswitch.models;

import enums.TransactionType;
import lombok.Data;

@Data
public class OriginalDataElements {
    private TransactionType originalTransactionType;
    private long originalAmount;
    private String originalAuthorizationCode;
    private String originalTransmissionDateTime;
    private String originalTransmissionTime;
    private String originalSTAN;
    private String originalRRN;
    private String originalAcquiringInstCode;
    private MessageReasonCode reversalReasonCode;
    private String originalForwardingInstCode;
    private long originalInterSwitchThreshold;

    public OriginalDataElements(TransactionType originalTransactionType, long originalAmount, String originalAuthorizationCode, String originalTransmissionDateTime,String originalTransmissionTime, String originalSTAN, String originalRRN, String originalAcquiringInstCode, MessageReasonCode reversalReasonCode, String originalForwardingInstCode, long originalInterSwitchThreshold) {
        this.originalTransactionType = originalTransactionType;
        this.originalAmount = originalAmount;
        this.originalAuthorizationCode = originalAuthorizationCode;
        this.originalTransmissionDateTime = originalTransmissionDateTime;
        this.originalSTAN = originalSTAN;
        this.originalRRN = originalRRN;
        this.originalAcquiringInstCode = originalAcquiringInstCode;
        this.reversalReasonCode = reversalReasonCode;
        this.originalForwardingInstCode = originalForwardingInstCode;
        this.originalInterSwitchThreshold = originalInterSwitchThreshold;
        this.originalTransmissionTime = originalTransmissionTime;
    }}
