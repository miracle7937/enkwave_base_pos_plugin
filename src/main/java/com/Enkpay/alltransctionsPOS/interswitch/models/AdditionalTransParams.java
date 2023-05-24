package com.Enkpay.alltransctionsPOS.interswitch.models;

import lombok.Data;

@Data
public class AdditionalTransParams {
    private String transmissionDateF7 = "";
    private String stanF11 ="";
    private String localTimeF12="";
    private String localDateF13="";
    private String posConditionCodeF25="";
    private String pinCaptureModeF26="";
    private String amountTransactionFeeF28="";
    private String rrnF37="";
    private String posDataCodeF123="";}