package com.Enkpay.alltransctionsPOS.nibbs.prep;

import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.nibbs.model.ExpressPaymentRequestBody;
import com.Enkpay.alltransctionsPOS.nibbs.remote_interface.ExpressPayAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class ClearMasterKeyFromExpressPayment {

   public  String request(String masterKey) {
       ExpressPayAPI expressPayAPI = new RetrofitBuilder().expressMasterKeyRequest();
       Call<String> call = expressPayAPI.getPlainMasterKey(new ExpressPaymentRequestBody(masterKey));

       String responseBody = null;
       try {
           Response<String> response = call.execute();
           if (response.isSuccessful()) {
               responseBody  = response.body();
               System.out.println(responseBody);
           } else {
               System.out.println(response.message());
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   
       return  responseBody;
   }
}
