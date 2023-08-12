package com.Enkpay.alltransctionsPOS.transaction;

import Var.Constants;
import Var.DateUtils;
import Var.Debug;
import Var.Encryption;
import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWInit;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWProcessPayment;
import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.nibbs.prep.DownloadNibsKeys;
import com.Enkpay.alltransctionsPOS.nibbs.prep.ProcessTransaction;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import com.google.gson.Gson;
import enums.TransactionType;
import generalModel.*;
import retrofit2.Response;
import services.PreferenceBase;

import java.io.IOException;

public class PosTransactions {
     HostConfig hostConfig;
   CardData cardData;
    TransactionRequestData requestData;
    PreferenceBase preferenceBase;



    public PosTransactions(HostConfig hostConfig, CardData cardData, TransactionRequestData requestData,  PreferenceBase preferenceBase) {
        this.hostConfig = hostConfig;
        this.cardData = cardData;
        this.requestData = requestData;
        this.preferenceBase = preferenceBase;
    }

    public PosTransactions(HostConfig hostConfig, PreferenceBase preferenceBase ){
        this.hostConfig= hostConfig;
        this.preferenceBase = preferenceBase;
    }

    public  void  init(IntResult intResult){
        if(true){
            boolean keyPassedTime=   DateUtils.hourPassed(7, preferenceBase.getLongData(Constants.LAST_POS_CONFIGURATION_TIME));
            if(keyPassedTime){
                new DownloadNibsKeys(this.preferenceBase).download(intResult, hostConfig.getTerminalId());
            }else {
                Debug.print("You cant get key because your current key haven't expired");
            }
        }else{
        new ISWInit(preferenceBase).setUpIswToken("", hostConfig.getTerminalId(), intResult);
        }

    }

    public  void  processTransaction(SDKTransactionResult sdkTransactionResult){
        try {
            FundWalletRequestData fundWalletRequestData = new FundWalletRequestData(PosTransactions.this.cardData, requestData,hostConfig,  null );
            Response<FundWalletResponseData>  logTransaction=
                    new RetrofitBuilder().logTransaction().logTransaction(fundWalletRequestData).execute();
            if(logTransaction.code() == 200){
                sdkTransactionResult.onError("Transaction Fail to log", requestData);
                return;
            }
        selectTransaction(hostConfig,cardData,requestData, (transactionResponse, requestData) -> {
            if( transactionResponse.refresh ==false){
                FundWalletRequestData fundWalletRequestDataWithResponse = new FundWalletRequestData(PosTransactions.this.cardData, requestData,hostConfig,  null );
                Response<FundWalletResponseData>     fundCustomerWallet =
                            new RetrofitBuilder().isFundUserWallet().fundCustomerWallet(fundWalletRequestDataWithResponse).execute();
                    if (fundCustomerWallet.body().status == true &&
                            (transactionResponse.responseCode.equals("00")
                            || transactionResponse.responseCode.equals("11"))) {
                        sdkTransactionResult.onSuccess(transactionResponse, requestData);

                    } else {
                        sdkTransactionResult.onError(transactionResponse.errorMessage , requestData);

                    }

            }else {
                new DownloadNibsKeys(PosTransactions.this.preferenceBase).download(null, hostConfig.getTerminalId());
                sdkTransactionResult.onError("Refresh new key", requestData);

            }
        });

        }catch (Exception e){
            sdkTransactionResult.onError(e.getLocalizedMessage(), requestData);
        }
    }




    public void  selectTransaction( HostConfig hostConfig, CardData cardData, TransactionRequestData requestData ,TransactionResult transactionResult) throws IOException {

        if(true){
        boolean keyPassedTime=   DateUtils.hourPassed(7, preferenceBase.getLongData(Constants.LAST_POS_CONFIGURATION_TIME));
        if(keyPassedTime){
            Debug.print("Key =======================> expired");

            //fetch new keys
            new DownloadNibsKeys(preferenceBase).download(null,  hostConfig.getTerminalId());
        }

         TransactionResponse response=   new ProcessTransaction(hostConfig).process( cardData, requestData);

            transactionResult.onTransactionCompleted(response, requestData);

        }else{
            new ISWProcessPayment().ProcessPayment(cardData,requestData, transactionResult);
        }

    }




    private  TransactionResponse  rollBack(HostConfig hostConfig, CardData cardData, TransactionRequestData requestData ){

        TransactionRequestData requestDataForReversal =  requestData;
        requestDataForReversal.transactionType = TransactionType.REVERSAL;
        //roll back
       return new ProcessTransaction(hostConfig).process(cardData, requestData);
    }

    public interface IntResult{
        public void onSuccess();
        public void onError(String message);
    }

    /*
the TransactionRequestData request data is for roll backs
*/
    public interface TransactionResult{

        public void onTransactionCompleted(TransactionResponse response, TransactionRequestData requestData) throws IOException;

    }


    public interface SDKTransactionResult{

        public void onSuccess(TransactionResponse response, TransactionRequestData requestData);
        public void onError(String message, TransactionRequestData requestData);

    }
}
