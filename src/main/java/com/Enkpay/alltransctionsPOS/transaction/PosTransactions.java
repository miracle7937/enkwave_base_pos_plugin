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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.KeyValuePairStorage;

public class PosTransactions {
    final HostConfig hostConfig;
    final CardData cardData;
    final  TransactionRequestData requestData;

    public PosTransactions(HostConfig hostConfig, CardData cardData, TransactionRequestData requestData) {
        this.hostConfig = hostConfig;
        this.cardData = cardData;
        this.requestData = requestData;
    }

    public static void  init(IntResult intResult){
        if(true){
            boolean keyPassedTime=   DateUtils.hourPassed(7, KeyValuePairStorage.getInstance().getLong(Constants.LAST_POS_CONFIGURATION_TIME));
            if(keyPassedTime){
                new DownloadNibsKeys().download(intResult);
            }else {
                Debug.print("You cant get key because your current key haven't expired");
            }
        }else{
        new ISWInit().setUpIswToken("","", intResult);
        }

    }



    public  void  processTransaction(SDKTransactionResult sdkTransactionResult){
        selectTransaction(hostConfig,cardData,requestData,new TransactionResult() {

            @Override
            public void onSuccess(TransactionResponse transactionResponse, TransactionRequestData requestData) {

                if( transactionResponse.responseCode=="00" && transactionResponse.responseCode=="11"){

                    FundWalletRequestData fundWalletRequestData = new FundWalletRequestData(PosTransactions.this.cardData, requestData,hostConfig );
                    new RetrofitBuilder().isFundUserWallet("https://jsonplaceholder.typicode.com").fundCustomerWallet(fundWalletRequestData).enqueue(new Callback<FundWalletResponseData>() {
                        @Override
                        public void onResponse(Call<FundWalletResponseData> call, Response<FundWalletResponseData> response) {
                            if((response.code() != 200 || response.code() != 201) &&( transactionResponse.responseCode=="00" || transactionResponse.responseCode=="11") ){

                                TransactionResponse transactionResponse= rollBack(hostConfig, cardData, requestData);

                                sdkTransactionResult.onSuccess(transactionResponse, requestData);

                            }

                        }

                        @Override
                        public void onFailure(Call<FundWalletResponseData> call, Throwable throwable) {

                            sdkTransactionResult.onError(throwable.getMessage(), requestData);

                            //roll back
                        }
                    });
                }else {
                    sdkTransactionResult.onSuccess(transactionResponse, requestData);

                }

                // fundwallet if successfull


            }

            @Override
            public void onError(String message, TransactionRequestData requestData) {

                // transaction fails;
            }
        });
    }




    public void  selectTransaction( HostConfig hostConfig, CardData cardData, TransactionRequestData requestData ,TransactionResult transactionResult){

        if(true){
        boolean keyPassedTime=   DateUtils.hourPassed(7, KeyValuePairStorage.getInstance().getLong(Constants.LAST_POS_CONFIGURATION_TIME));
        if(keyPassedTime){
            //fetch new keys
            new DownloadNibsKeys().download(null);
        }
         TransactionResponse response=   new ProcessTransaction(hostConfig).process( cardData, requestData);
        if(response.responseCode == "00" || response.responseCode == "11"){
            transactionResult.onSuccess(response, requestData);
        }else{
            transactionResult.onError("Error occurred", requestData);

        }

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

        public void onSuccess(TransactionResponse response, TransactionRequestData requestData);
        public void onError(String message, TransactionRequestData requestData);

    }


    public interface SDKTransactionResult{

        public void onSuccess(TransactionResponse response, TransactionRequestData requestData);
        public void onError(String message, TransactionRequestData requestData);

    }
}
