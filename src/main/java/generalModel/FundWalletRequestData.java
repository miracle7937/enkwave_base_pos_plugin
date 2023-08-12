package generalModel;

import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import enums.TransactionType;
import lombok.ToString;

@ToString
public class FundWalletRequestData {
    String terminalID;
    Long amount;
    TransactionType transactionType;
    String RRN, STAN, pan, cardName, deviceNO, responseCode;
    Boolean status;
    String userID;
    public FundWalletRequestData(CardData cardData, TransactionRequestData requestData,
                                 HostConfig hostConfig, TransactionResponse transactionResponse){
        this.terminalID = hostConfig.getTerminalId();
        this.amount = requestData.amount;
        this.transactionType = requestData.getTransactionType();
        this.deviceNO = requestData.getDeviceNO();
        this.pan = cardData.getMaskedPan();
        this.cardName = cardData.getCardName();
        this.RRN  =   requestData.getRRN() != null? requestData.getRRN(): "" ;
        this.userID = requestData.getUserID();
        if(transactionResponse != null){
            this.STAN = transactionResponse.getSTAN();
            this.RRN = transactionResponse.getRRN();
            this.responseCode = transactionResponse.responseCode;

        }

    }
}


