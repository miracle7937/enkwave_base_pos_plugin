package generalModel;

import Var.Constants;
import Var.PosMode;
import com.Enkpay.alltransctionsPOS.utils.APPUtils;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import enums.IsoAccountType;
import enums.TransactionType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionResponse {
    public TransactionType transactionType;
    public String maskedPan;
    public long amount;
    public String transmissionDateTime;
    public String transmissionTime;
    public String transmissionDate;
    public String STAN;

    public String RRN;
    public String localTime_12;
    public String localDate_13;
    public long otherAmount = 0;
    public String acquiringInstCode = "";
    public String originalForwardingInstCode = "";
    public String authCode = "";
    public String responseCode = "";
    public String additionalAmount_54 = "";
    public String echoData = null;

    public String cardLabel = "";
    public String cardExpiry = "";
    public String cardHolder = "";
    public String TVR = "";
    public String TSI = "";
    public String AID = "";
    public String appCryptogram = "";
    public long transactionTimeInMillis = 0L;
    public  boolean refresh = false;
    public IsoAccountType accountType = IsoAccountType.DEFAULT_UNSPECIFIED;

    public String terminalId = "";
    public String merchantId = "";
    public String otherId = "";

    public long id = 0;

    public String responseDE55 = null;
    public PosMode source = PosMode.EPMS;
    public long interSwitchThreshold = 0L;
    public String errorMessage= null;
    public boolean isSuccessful;


   public TransactionResponse parseNibssMessage(String[] receiving, TransactionType transactionType, boolean refresh   ){
       this.transactionType = transactionType;
       this.accountType = IsoAccountType.parseIntAccountType(
              Integer.parseInt( receiving[4].substring(
                      2,
                      4
              ))
       );
    maskedPan =  CardData.maskedPan( receiving[2]);
    amount = Long.parseLong(receiving[4]);
    otherAmount = 0;
    transmissionDateTime = receiving[7];
    STAN = receiving[11];
     cardExpiry = !APPUtils.isEmpty(receiving[14]) ? receiving[14] : "";
    RRN = receiving[37];
    localTime_12= receiving[12];
    localDate_13= receiving[13];
    acquiringInstCode = receiving[32];
    terminalId = receiving[41];
    merchantId = receiving[42];
    originalForwardingInstCode = APPUtils.isEmpty(receiving[33]) ? receiving[33] : "";
    authCode =  !APPUtils.isEmpty(receiving[38]) ? receiving[38] : "";
    responseCode = !APPUtils.isEmpty(receiving[39]) ? receiving[39] : "20";
    additionalAmount_54 = !APPUtils.isEmpty(receiving[54]) ? receiving[54] : "";
    responseDE55 = !APPUtils.isEmpty(receiving[55]) ? receiving[55] : "";
    echoData = !APPUtils.isEmpty(receiving[59]) ? Constants.getResponseMessageFromCode(responseCode) : "";
     transmissionDate = receiving[13];
    transmissionTime = receiving[12];
    isSuccessful =  !APPUtils.isEmpty(receiving[39]) ? (receiving[39].equalsIgnoreCase("00") || receiving[39].equalsIgnoreCase("00")): false;
    this.refresh = refresh;
    return  this;

}



}