package com.Enkpay.alltransctionsPOS.implementation;

import com.Enkpay.alltransctionsPOS.interswitch.service.IswService;
import com.Enkpay.alltransctionsPOS.nibbs.remote_interface.ExpressPayAPI;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import services.FundWalletService;

import java.util.concurrent.TimeUnit;

public class RetrofitBuilder {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    final String Express_Payment= "http://80.88.8.56:552/";
    final String EPCashOutPoint= "http://testpos.enkpay.com/api/v1/cash-out-webhook";
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

    public  FundWalletService isFundUserWallet(){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

       return new Retrofit.Builder()
                .baseUrl(EPCashOutPoint)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(FundWalletService.class);
    }


    public  ExpressPayAPI expressMasterKeyRequest(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        String credentials = Credentials.basic("EnkwavePOS", "64994967-b854-4b9e-ab14-a0d4514a1e71");
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", credentials)
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });



        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Express_Payment)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        // Create service instance
        return  retrofit.create(ExpressPayAPI.class);
    }

}
