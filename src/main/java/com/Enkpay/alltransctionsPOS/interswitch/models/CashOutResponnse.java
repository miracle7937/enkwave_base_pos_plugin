package com.Enkpay.alltransctionsPOS.interswitch.models;


import Var.PosMode;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import enums.IsoAccountType;
import enums.TransactionType;
import generalModel.TransactionResponse;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;

@Data
@Root(strict = false, name = "transferResponse")
public class CashOutResponnse {

    @Element(name = "description")
    String description  = "";

    @Element(name = "field39")
    String field39  = "A3";

    @Element(name = "authId" ,required = false)
    String authId  = "";



    @Path("hostEmvData")
    @Element(name = "AmountAuthorized", required = false)
    Long amountAuthorized  = 0L;


    @Path("hostEmvData")
    @Element(name = "AmountOther", required = false)
    Long AmountOther  = 0L;

    @Path("hostEmvData")
    @Element(name = "atc", required = false)
    String atc  = "";

    @Path("hostEmvData")
    @Element(name = "iad", required = false)
    String iad  = "";
    @Path("hostEmvData")
    @Element(name = "rc", required = false)
    Integer rc  = 0;
    @Element(name = "referenceNumber", required = false)
    String referenceNumber  = "";

    @Element(name = "transactionChannelName", required = false)
    String transactionChannelName  = "";

    @Element(name = "wasReceived", required = false)
    Boolean wasReceived  = false;

    @Element(name = "wasSent", required = false)
    Boolean wasSent  = false;



   public TransactionResponse toTransactionResponse(String stan,
                                                    CardData cardData,
                                                    Long transactionTime,
                                                    IswParameters iswParameters,
                                                    Long amount,
                                                    Long interswitchThreshold,
                                                    String errorMessage,
                                                    String reqRRN ){

       TransactionResponse res= new TransactionResponse();
       res.setAmount(amount);
       if (this.referenceNumber.isEmpty()){
           res.setRRN( reqRRN );
       }else {
           res.setRRN(  this.referenceNumber );
       }

       res.setResponseCode(this.field39);
        res.setAuthCode(this.authId);
        res.setSTAN(stan);
        res.setEchoData(iswParameters.remark);
        res.setMaskedPan( CardData.maskedPan(cardData.pan));
        res.setCardExpiry( cardData.expiryDate);
        res.setCardLabel("" );
       res.setTransactionTimeInMillis(transactionTime );
       res.setTransactionType(TransactionType.PURCHASE );
       res.setTerminalId(iswParameters.terminalId );
       res.setMerchantId(iswParameters.merchantId );
       res.setTransmissionDateTime( new SimpleDateFormat("MMddhhmmss", Locale.getDefault()).format(transactionTime));
       res.setLocalTime_12( new SimpleDateFormat("hhmmss", Locale.getDefault()).format(transactionTime));
       res.setLocalDate_13( new SimpleDateFormat("MMdd", Locale.getDefault()).format(transactionTime));
       res.setAccountType(IsoAccountType.DEFAULT_UNSPECIFIED );
       res.setSource(PosMode.ISW );
       res.setErrorMessage(errorMessage );
       res.setInterSwitchThreshold( interswitchThreshold);
       return  res;


   }
}
