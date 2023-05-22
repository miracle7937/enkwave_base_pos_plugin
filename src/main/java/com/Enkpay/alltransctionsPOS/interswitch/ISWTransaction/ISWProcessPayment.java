package com.Enkpay.alltransctionsPOS.interswitch.ISWTransaction;

import com.Enkpay.alltransctionsPOS.implementation.RetrofitBuilder;
import com.Enkpay.alltransctionsPOS.interswitch.models.CashOutResponnse;
import com.Enkpay.alltransctionsPOS.transaction.PosTransactions;
import com.google.gson.Gson;
import generalModel.TransactionRequestData;
import com.Enkpay.alltransctionsPOS.interswitch.models.TransferRequest;
import com.Enkpay.alltransctionsPOS.utils.*;
import enums.CVMETHOD;
import generalModel.TransactionResponse;
import interfaces.OnTransactionCompleted;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class ISWProcessPayment {
    private String merchId;
    private String terminalId;
    private Long amount;
    private Long otherAmount = 0L;
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    public void ProcessPayment(CardData cardData, TransactionRequestData transRequestData, PosTransactions.TransactionResult transactionResult) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
        Long transactionTime = System.currentTimeMillis();
        IsoTimeManager timeMgr = new IsoTimeManager();

        //set value
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setBatteryInformation(100);
        transferRequest.setCurrencyCode("566");
        transferRequest.setLanguageInfo("EN");
        transferRequest.setMerchantId(transRequestData.getIswParameters().getMerchantId()); //////////////////
        transferRequest.setMerchantLocation(transRequestData.getIswParameters().getMerchantNameLocation());//////////////////
        transferRequest.setPosConditionCode("00");
        transferRequest.setPosDataCode("510101511344101");
        transferRequest.setPosEntryMode("051");
        transferRequest.setPosGeoCode("00234000000000566");
        transferRequest.setPrinterStatus(1);
        transferRequest.setTerminalId(transRequestData.getIswParameters().getTerminalId()); /////////////////////////
        transferRequest.setTerminalType("22");
        transferRequest.setTransmissionDate(sdf.format( new Date(transactionTime)));
        transferRequest.setUniqueId(transRequestData.getIswParameters().getTerminalSerial()); /////////////////////////
        transferRequest.setCardSequenceNumber(cardData.getPanSequenceNumber()); /////////////////////////
        transferRequest.setAmountAuthorized(transRequestData.getTlvTags().getTLV9F02()); /////////////////////////9F02
        transferRequest.setAmountOther(transRequestData.getTlvTags().getTLV9F03()); /////////////////////////9F03
        transferRequest.setApplicationInterchangeProfile(transRequestData.getTlvTags().getTLV82()); /////////////////////////82
        transferRequest.setAtc(transRequestData.getTlvTags().getTLV9F36()); /////////////////////////9F36
        transferRequest.setCryptogram(transRequestData.getTlvTags().getTLV9F26()); /////////////////////////9F26
        transferRequest.setCryptogramInformationData(Integer.parseInt(transRequestData.getTlvTags().getTLV9F27())); /////////////////////////9F27
        transferRequest.setCvmResults(transRequestData.getTlvTags().getTLV9F34()); /////////////////////////9F34
        transferRequest.setIad(transRequestData.getTlvTags().getTLV9F10()); /////////////////////////9F10
        transferRequest.setTransactionCurrencyCode("566");
        transferRequest.setTerminalVerficationResult(transRequestData.getTlvTags().getTLV95()); /////////////////////////95
        transferRequest.setTerminalCountryCode("566");
        transferRequest.setTerminalType2(transRequestData.getTlvTags().getTLV9F35()); /////////////////////////9F35
        transferRequest.setTerminalCapabilities("E0F0C8");
        transferRequest.setTransationDate(transRequestData.getTlvTags().getTLV9A()); /////////////////////////"9A"
        transferRequest.setTransactionType(transRequestData.getTlvTags().getTLV9C()); /////////////////////////"9C"
        transferRequest.setUnpredictableNumber(transRequestData.getTlvTags().getTLV9F37()); /////////////////////////"9F37"

        String value85= "";
        if(value85.isEmpty()){
            value85 =  "00000000000000";
        }else {
            value85 =     transRequestData.getTlvTags().getTLV85();
        }
        transferRequest.setUnpredictableNumber(value85); /////////////////////////85

        transferRequest.setPan(cardData.getPan().replace("F", ""));
        transferRequest.setExpiryMonth(cardData.getExpiryDate().substring(0, 2));
        transferRequest.setExpiryYear(cardData.getExpiryDate().substring(2) );

        transferRequest.setTrack2(cardData.getTrack2Data());
        transferRequest.setOriginalTransmissionDate(sdf.format(new Date(transactionTime)));
        transferRequest.setStan(timeMgr.getTime()); //transactionRequestData!!.STAN
        transferRequest.setRrn( timeMgr.getFullDate().substring(2, 14)); //transactionRequestData!!.RRN

        transferRequest.setFromAccount("default"); //transactionRequestData?.accountType?.name
        transferRequest.setToAccount("");
        Long minorAmount = transRequestData.amount-1075;
        transferRequest.setMinorAmount(minorAmount.toString());
        transferRequest.setReceivingInstitutionId(""); //transactionRequestData!!.iswParameters!!.receivingInstitutionId
        transferRequest.setSurcharge("1075");
        transferRequest.setKsnd("605");


        if(getCVMMethod("9F34")== CVMETHOD.ONLINE_PIN){
            transferRequest.setKsn( "000002DDDDE00001");
        }else {
            transferRequest.setKsn( "");
        }
        transferRequest.setPinType("Dukpt");

        if(getCVMMethod("9F34")== CVMETHOD.ONLINE_PIN){
            String IPEK_TEST = "9F8011E7E71E483B";
            String KSN_TEST = "0000000006DDDDE01500";
             String value =  DukptHelper.DesEncryptDukpt(
                    DukptHelper.getSessionKey(IPEK_TEST,KSN_TEST ),
                    TripleDES.decrypt(
                            cardData.pinBlock,
                            transRequestData.getIswParameters().getPinKey()
                    ).toUpperCase(
                            Locale.getDefault()
                    )
            );
            transferRequest.setPinBlock( value);

        }{
            transferRequest.setPinBlock( "");

        }

        transferRequest.setKeyLabel( "000002");
        transferRequest.setDestinationAccountNumber( transRequestData.getIswParameters().getDestinationAccountNumber());
        transferRequest.setExtendedTransactionType( "6103");
        transferRequest.setHeader( transRequestData.getIswParameters().getToken());
        logger.info("ISW TRANS_DATA "+ transferRequest.toString());

          new RetrofitBuilder().iswTransactionClientTest().performTransaction(
                 "Bearer "+transRequestData.getIswParameters().getToken() ,
                 transferRequest
         ).enqueue(new Callback<>() {
              @Override
              public void onResponse(Call<CashOutResponnse> call, Response<CashOutResponnse> response) {
                  if (response.isSuccessful()) {
                      // handle successful response
                      CashOutResponnse cashOutResponnse = response.body();
                      String stan ;
                      if(APPUtils.isEmpty(transRequestData.getSTAN())){
                          stan= transRequestData.getSTAN();
                      }else{
                          stan= timeMgr.getTime();

                      }
                      String rrn ;
                      if(APPUtils.isEmpty(transRequestData.getRRN())){
                          rrn= transRequestData.getRRN();
                      }else{
                          rrn= timeMgr.getFullDate().substring(2, 14);

                      }


                      TransactionResponse transactionResponse = cashOutResponnse.toTransactionResponse(
                              stan,
                              cardData,
                              transactionTime,
                              transRequestData.getIswParameters(),
                              amount,
                              transRequestData.getIswParameters().getInterSwitchThreshold(),
                              "",
                              rrn
                      );
                      //onSuccess response
                      System.out.println(new Gson().toJson(cashOutResponnse)+ "MIMI");
                      transactionResult.onSuccess(transactionResponse);
                      // ...
                  }
              }

              @Override
              public void onFailure(Call<CashOutResponnse> call, Throwable throwable) {

                  CashOutResponnse cashOutResponnse= new CashOutResponnse();
                  cashOutResponnse.setAmountAuthorized( ISWProcessPayment.this.amount);
                  cashOutResponnse.setAmountOther( ISWProcessPayment.this.otherAmount);
                  cashOutResponnse.setField39( "A3");
                  String stan ;
                  if(APPUtils.isEmpty(transRequestData.getSTAN())){
                      stan= transRequestData.getSTAN();
                  }else{
                      stan= timeMgr.getTime();

                  }
                  String rrn ;
                  if(APPUtils.isEmpty(transRequestData.getRRN())){
                      rrn= transRequestData.getRRN();
                  }else{
                      rrn= timeMgr.getFullDate().substring(2, 14);

                  }
                  cashOutResponnse.toTransactionResponse(
                          stan,
                          cardData,
                          transactionTime,
                          transRequestData.getIswParameters(),
                          amount,
                          transRequestData.getIswParameters().getInterSwitchThreshold(),
                          throwable.getLocalizedMessage(),
                          rrn
                  );
                  System.out.println(new Gson().toJson(cashOutResponnse)+ "MIMI");

                  transactionResult.onError(throwable.getLocalizedMessage());
              }
          });





    }

    public CVMETHOD getCVMMethod(String cvmResult) {
        char cvmChar = cvmResult.charAt(1);
        switch (cvmChar) {
            case '1':
                return CVMETHOD.OFFLINE_PLAINTEXT_PIN;
            case '2':
                return CVMETHOD.ONLINE_PIN;
            case '3':
                return CVMETHOD.OFFLINE_PLAINTEXT_PIN_AND_SIGNATURE;
            case '4':
                return CVMETHOD.OFFLINE_ENCIPHERED_PIN;
            case '5':
                return CVMETHOD.OFFLINE_ENCIPHERED_PIN_AND_SIGNATURE;
            case 'E':
                return CVMETHOD.SIGNATURE;
            default:
                return CVMETHOD.NO_CVM_PERFORMED;
        }
    }

}
