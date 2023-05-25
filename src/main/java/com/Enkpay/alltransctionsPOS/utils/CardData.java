package com.Enkpay.alltransctionsPOS.utils;


import lombok.Data;

@Data
public class CardData {
    public String track2Data;
    public String iccData;
    public String panSequenceNumber;
    public String posEntryMode;
    public String pinBlock;
    public  String pan;
    public String serviceCode;
    public String expiryDate;
    public String acquiringInstitutionIdCode;
    public String CardName;
    public String aid;

    public static String maskedPan(String cardNo)
    {
        if(cardNo == null)
            return "*****";
        int cardLength = cardNo.length();
        String firCardNo = cardNo.substring(0,6);
        String lastCardNo = cardNo.substring(cardLength - 4);
        String mid = "******";
        return (firCardNo + mid + lastCardNo);
    }

    public  String getMaskedPan()
    {
        if(this.pan == null)
            return "*****";
        int cardLength = pan.length();
        String firCardNo = pan.substring(0,6);
        String lastCardNo = pan.substring(cardLength - 4);
        String mid = "******";
        return (firCardNo + mid + lastCardNo);
    }

}