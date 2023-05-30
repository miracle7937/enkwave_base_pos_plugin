package com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction;

import Var.Constants;
import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.interswitch.models.TokenPassportRequest;
import com.Enkpay.alltransctionsPOS.interswitch.models.TokenPassportResponse;
import com.Enkpay.alltransctionsPOS.transaction.PosTransactions;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.KeyValuePairStorage;
import services.PreferenceBase;

@AllArgsConstructor
public class ISWInit {
    final PreferenceBase preferenceBase;
    public  void  setUpIswToken(String mid, String terminalID, final PosTransactions.IntResult onISWTokenComplete){
        TokenPassportRequest tokenPassportRequest     = new TokenPassportRequest(mid, terminalID);

        new RetrofitBuilder().getTokenClient().getToken(tokenPassportRequest).enqueue(new Callback<TokenPassportResponse>() {
            @Override
            public void onResponse(Call<TokenPassportResponse> call, Response<TokenPassportResponse> response) {


                if(response.body() != null){
                   new  KeyValuePairStorage(preferenceBase).put(Constants.TOKEN_RESPONSE_TAG,response.body().getToken());
                }

            }

            @Override
            public void onFailure(Call<TokenPassportResponse> call, Throwable throwable) {
                 onISWTokenComplete.onError(throwable.getLocalizedMessage());

            }
        });
    }



}
