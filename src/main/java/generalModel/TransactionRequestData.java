package generalModel;

import com.Enkpay.alltransctionsPOS.interswitch.models.AdditionalTransParams;
import com.Enkpay.alltransctionsPOS.interswitch.models.IswParameters;
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
}

