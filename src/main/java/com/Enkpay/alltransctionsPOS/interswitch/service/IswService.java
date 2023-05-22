package com.Enkpay.alltransctionsPOS.interswitch.service;

import com.Enkpay.alltransctionsPOS.interswitch.models.CashOutResponnse;
import com.Enkpay.alltransctionsPOS.interswitch.models.TokenPassportRequest;
import com.Enkpay.alltransctionsPOS.interswitch.models.TokenPassportResponse;
import com.Enkpay.alltransctionsPOS.interswitch.models.TransferRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public  interface IswService {
    @Headers("content-type:application/xml")
    @POST("kimonotms/requesttoken/perform-process")
    Call<TokenPassportResponse> getToken(@Body TokenPassportRequest tokenPassportRequest );

    @Headers({"content-type: application/xml", "Accept: application/xml"})
    @POST("amex")
    Call<CashOutResponnse> performTransaction(
            @Header("Authorization") String token,
            @Body TransferRequest transferRequest
    );
}

