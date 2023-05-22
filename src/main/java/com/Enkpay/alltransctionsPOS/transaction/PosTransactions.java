package com.Enkpay.alltransctionsPOS.transaction;

import Var.Constants;
import Var.DateUtils;
import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWInit;
import com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction.ISWProcessPayment;
import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.nibbs.prep.DownloadNibsKeys;
import com.Enkpay.alltransctionsPOS.nibbs.prep.ProcessTransaction;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import generalModel.FundWalletRequestData;
import generalModel.FundWalletResponseData;
import generalModel.TransactionRequestData;
import generalModel.TransactionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.KeyValuePairStorage;

public class PosTransactions {

    public void  init(IntResult intResult){
        if(true){
            new DownloadNibsKeys().download(intResult);
        }else{
        new ISWInit().setUpIswToken("","", intResult);
        }

    }



    public  void  processTransaction(HostConfig hostConfig,  CardData cardData,TransactionRequestData requestData){
        selectTransaction(hostConfig,cardData,requestData,new TransactionResult() {
            @Override
            public void onSuccess(TransactionResponse response) {

                // fundwallet if successfull
                FundWalletRequestData fundWalletRequestData = new FundWalletRequestData(cardData, requestData,hostConfig );
                new RetrofitBuilder().isFundUserWallet("").fundCustomerWallet(fundWalletRequestData).enqueue(new Callback<FundWalletResponseData>() {
                    @Override
                    public void onResponse(Call<FundWalletResponseData> call, Response<FundWalletResponseData> response) {

                    }

                    @Override
                    public void onFailure(Call<FundWalletResponseData> call, Throwable throwable) {
                        //roll back
                    }
                });

            }

            @Override
            public void onError(String message) {

                // transaction fails;
            }
        });
    }




    public void  selectTransaction( HostConfig hostConfig, CardData cardData, TransactionRequestData requestData ,TransactionResult transactionResult){

        if(true){
        boolean keyPassedTime=   DateUtils.hourPassed(7, KeyValuePairStorage.getInstance().getLong(Constants.LAST_POS_CONFIGURATION_TIME));
        if(keyPassedTime){
            new DownloadNibsKeys().download(null);
        }
        new ProcessTransaction(hostConfig).process( cardData, requestData, transactionResult);
        }else{
            new ISWProcessPayment().ProcessPayment(cardData,requestData, transactionResult);
        }

    }




    public interface IntResult{
        public void onSuccess();
        public void onError(String message);
    }
    public interface TransactionResult{
        public void onSuccess(TransactionResponse response);
        public void onError(String message);

    }
}
