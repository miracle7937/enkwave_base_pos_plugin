package com.Enkpay.alltransctionsPOS.interswitch.models;

import lombok.Data;

@Data
public class IswParameters {
    String pinKey, token;
   String destinationAccountNumber;
    String merchantId;
    String merchantNameLocation ;
    String terminalId = "";
    String terminalSerial = "";
    String receivingInstitutionId= "";
    Long interSwitchThreshold = 0L;
    String remark= "";
}
