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
    public FundWalletRequestData(CardData cardData, TransactionRequestData requestData,
                                 HostConfig hostConfig, TransactionResponse transactionResponse){
        this.terminalID = hostConfig.getTerminalId();
        this.amount = requestData.amount;
        this.transactionType = requestData.getTransactionType();
        this.STAN = transactionResponse.getSTAN();
        this.RRN = transactionResponse.getRRN();
        this.deviceNO = requestData.getDeviceNO();
        this.pan = cardData.getMaskedPan();
        this.cardName = cardData.getCardName();
        this.responseCode = transactionResponse.responseCode;

    }
}


