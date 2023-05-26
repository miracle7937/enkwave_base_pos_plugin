package com.Enkpay.alltransctionsPOS.nibbs.remote_interface;

import com.Enkpay.alltransctionsPOS.nibbs.model.ExpressPaymentRequestBody;
import generalModel.FundWalletRequestData;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface ExpressPayAPI {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("api/GetPlainMasterKey")
    Call<String> getPlainMasterKey(@Body ExpressPaymentRequestBody body);
}
