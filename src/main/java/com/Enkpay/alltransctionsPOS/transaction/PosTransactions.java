package com.Enkpay.alltransctionsPOS.transaction;

import Var.Constants;
import Var.DateUtils;
import Var.Debug;
import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWInit;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWProcessPayment;
import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.nibbs.prep.DownloadNibsKeys;
import com.Enkpay.alltransctionsPOS.nibbs.prep.ProcessTransaction;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import enums.TransactionType;
import generalModel.FundWalletRequestData;
import generalModel.FundWalletResponseData;
import generalModel.TransactionRequestData;
import generalModel.TransactionResponse;
import retrofit2.Response;
import services.PreferenceBase;

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
        selectTransaction(hostConfig,cardData,requestData, (transactionResponse, requestData) -> {

            Debug.print("First Request====================>"+ transactionResponse.toString());
            if( transactionResponse.refresh ==false){
                FundWalletRequestData fundWalletRequestData = new FundWalletRequestData(PosTransactions.this.cardData, requestData,hostConfig,  transactionResponse );
                try {
                    Response<FundWalletResponseData>     fundCustomerWallet =   new RetrofitBuilder().isFundUserWallet().fundCustomerWallet(fundWalletRequestData).execute();

                    if (false) {
                        sdkTransactionResult.onSuccess(transactionResponse, requestData);

                    } else {

                        if(requestData.getOriginalDataElements() != null){
                            TransactionResponse rollBackTransactionResponse= rollBack(hostConfig, cardData, requestData);

                            sdkTransactionResult.onSuccess(rollBackTransactionResponse, requestData);
//
                        }else{
                            sdkTransactionResult.onSuccess(transactionResponse, requestData);
                        }


                    }

                }catch (Exception e){
                    sdkTransactionResult.onError(e.getLocalizedMessage(), requestData);
                }

            }else {
                new DownloadNibsKeys(PosTransactions.this.preferenceBase).download(null, hostConfig.getTerminalId());
                sdkTransactionResult.onError("Refresh new key", requestData);

            }



        });
    }




    public void  selectTransaction( HostConfig hostConfig, CardData cardData, TransactionRequestData requestData ,TransactionResult transactionResult){

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

        public void onTransactionCompleted(TransactionResponse response, TransactionRequestData requestData);

    }


    public interface SDKTransactionResult{

        public void onSuccess(TransactionResponse response, TransactionRequestData requestData);
        public void onError(String message, TransactionRequestData requestData);

    }
}
