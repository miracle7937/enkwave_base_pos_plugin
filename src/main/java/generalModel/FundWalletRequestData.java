package generalModel;

import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import enums.TransactionType;

public class FundWalletRequestData {
    String terminalID;
    Long amount;
    TransactionType transactionType;
    String RRN, STAN, pan, cardName, deviceNO;
    public FundWalletRequestData(CardData cardData, TransactionRequestData requestData, HostConfig hostConfig){
        this.terminalID = hostConfig.getTerminalId();
        this.amount = requestData.amount;
        this.transactionType = requestData.getTransactionType();
        this.STAN = requestData.getSTAN();
        this.RRN = requestData.getRRN();
        this.deviceNO = requestData.getDeviceNO();
        this.pan = cardData.getMaskedPan();
        this.cardName = cardData.getCardName();

    }
}
