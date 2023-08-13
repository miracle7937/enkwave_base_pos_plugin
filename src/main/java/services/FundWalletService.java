package services;

import generalModel.FundWalletRequestData;
import generalModel.FundWalletResponseData;
import generalModel.RequestModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FundWalletService {


    @Headers({
            "dataKey: $2y$10$Dzn4DW2CvCf7sfMUxq6.QOussHZE/jXrE8SFSu7EoJjjJy/LYlphe",
    })
    @POST("pos")
    Call<FundWalletResponseData> fundCustomerWallet(@Body FundWalletRequestData encryptedBody);



    @Headers({
            "dataKey: $2y$10$Dzn4DW2CvCf7sfMUxq6.QOussHZE/jXrE8SFSu7EoJjjJy/LYlphe",
    })
    @POST("pos-logs")
    Call<FundWalletResponseData> logTransaction(@Body FundWalletRequestData encryptedBody);
}
