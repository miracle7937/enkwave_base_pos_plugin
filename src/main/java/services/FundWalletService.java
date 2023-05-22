package services;

import generalModel.FundWalletRequestData;
import generalModel.FundWalletResponseData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FundWalletService {
    @POST("/fund_userWallet")
    Call<FundWalletResponseData> fundCustomerWallet(@Body FundWalletRequestData user);
}
