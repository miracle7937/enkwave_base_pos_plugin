package com.Enkpay.alltransctionsPOS.implementation;

import com.Enkpay.alltransctionsPOS.interswitch.service.IswService;
import generalModel.FundWalletResponseData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import services.FundWalletService;

import java.util.concurrent.TimeUnit;

public class RetrofitBuilder {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    final String live = "https://kimono.interswitchng.com/kmw/kimonoservice/";
final String test = "https://qa.interswitchng.com/kmw/kimonoservice/";
final String KSN_TEST = "";
final String IPEK_TEST = "";
final String KSN_LIVE = "";
final String IPEK_LIVE = "";






    final Retrofit.Builder   baseBuilder= new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                    getBaseOkhttpClientBuilder()
                            .build());



    private  OkHttpClient.Builder getBaseOkhttpClientBuilder() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
                .callTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES);
        return okHttpClientBuilder;
    }


   public IswService getTokenClient() {
       return  new Retrofit.Builder()
            .client(getBaseOkhttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://saturn.interswitchng.com:443/")
    .build().create(IswService.class);}



   public IswService iswTransactionClient() {

        return  new Retrofit.Builder()
            .client(getBaseOkhttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(live)
    .build().create(IswService.class);}

  public IswService iswTransactionClientTest(){
        return baseBuilder
            .baseUrl(test)
            .build().create(IswService.class);
    }

    public  FundWalletService isFundUserWallet(String baseUrl){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

       return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(FundWalletService.class);
    }

}
