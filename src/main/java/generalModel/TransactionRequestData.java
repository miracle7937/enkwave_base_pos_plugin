package generalModel;

import com.Enkpay.alltransctionsPOS.interswitch.models.AdditionalTransParams;
import com.Enkpay.alltransctionsPOS.interswitch.models.IswParameters;
import com.Enkpay.alltransctionsPOS.interswitch.models.MessageReasonCode;
import com.Enkpay.alltransctionsPOS.interswitch.models.OriginalDataElements;
import enums.IsoAccountType;
import enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionRequestData {
    public  String deviceNO;
    public TransactionType transactionType;
    public    Long amount;
    public  Long otherAmount =0L;
    public  Long refundAmount;
    public  IsoAccountType accountType = IsoAccountType.DEFAULT_UNSPECIFIED;
    public  String RRN;
    public  String STAN;
    public  AdditionalTransParams additionalTransParams = null;
    public  String echoData  = null;
    public  OriginalDataElements originalDataElements = null;
    public  IswParameters iswParameters;
    public  TLVTags tlvTags;


   public TransactionRequestData(
            TransactionType transactionType ,
            Long amount,
            Long otherAmount,
            Long refundAmount,
            IsoAccountType accountType,
            OriginalDataElements originalDataElements,
            TLVTags tags
    ){
        this.transactionType=transactionType;
        this.amount= (amount == null) ? 0 : amount;
        this.otherAmount=otherAmount;
        this.accountType= (amount == null) ?IsoAccountType.DEFAULT_UNSPECIFIED :  accountType  ;
        this.originalDataElements=originalDataElements;
        this.tlvTags = tags;
        this.refundAmount= refundAmount;

    }


    public    void   setReversalData( String[] receiving ){
        additionalTransParams.setTransmissionDateF7(receiving[7]);
        additionalTransParams.setStanF11(receiving[11]);
        additionalTransParams.setLocalTimeF12(receiving[12]);
        additionalTransParams.setLocalDateF13(receiving[13]);
        additionalTransParams.setPosConditionCodeF25(receiving[25]);
        additionalTransParams.setPinCaptureModeF26(receiving[26]);
        additionalTransParams.setAmountTransactionFeeF28(receiving[28]);
        additionalTransParams.setRrnF37(receiving[37]);
        additionalTransParams.setPosDataCodeF123(receiving[123]);

    }


    public    void   setOriginalElement( String[] receiving ){
       if(receiving != null || receiving.length > 0) {
           originalDataElements = new OriginalDataElements();
           originalDataElements.setTransmissionDateF7(receiving[7]);
           originalDataElements.setOriginalTransmissionDateTime(receiving[7]);
           originalDataElements.setOriginalSTAN(receiving[11]);
           originalDataElements.setLocalTimeF12(receiving[12]);
           originalDataElements.setOriginalTransmissionTime(receiving[12]);
           originalDataElements.setLocalDateF13(receiving[13]);
           originalDataElements.setOriginalAcquiringInstCode(receiving[32]);
           originalDataElements.setOriginalForwardingInstCode(receiving[33]);
           originalDataElements.setOriginalRRN(receiving[37]);
           originalDataElements.setOriginalAuthorizationCode(receiving[38]);
       }
    }




}

