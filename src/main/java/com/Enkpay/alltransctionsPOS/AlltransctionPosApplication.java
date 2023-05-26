package com.Enkpay.alltransctionsPOS;

import Var.Constants;
import com.Enkpay.alltransctionsPOS.nibbs.prep.ClearMasterKeyFromExpressPayment;
import com.Enkpay.alltransctionsPOS.nibbs.prep.DownloadNibsKeys;
import com.Enkpay.alltransctionsPOS.nibbs.prep.ParametersDownload;
import enums.IsoAccountType;
import enums.TransactionType;
import generalModel.KeyHolder;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class AlltransctionPosApplication {

	public static void main(String[] args) {
//		SpringApplication.run(AlltransctionPosApplication.class, args);

		try {

			new ClearMasterKeyFromExpressPayment().request("11111111111111111111111111111111");
//		 new DownloadNibsKeys().download(null);
//			new ParametersDownload().download(
//					TransactionType.TERMINAL_PARAMETER_DOWNLOAD,
//					"8767B834",
//					keyHolder.clearSessionKey(),
//					Constants.RESPONSE_CODE_39,
//					IsoAccountType.DEFAULT_UNSPECIFIED

//);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}




//
//		new ISWInit().setUpIswToken("MERCHANT001", "TERMINAL001", new OnISWTokenComplete() {
//			@Override
//			public void onSuccess(TokenPassportResponse tokenPassportResponse) {
//				System.out.println(new Gson().toJson(tokenPassportResponse) + " MIIII");
//			}
//
//			@Override
//			public void onError(String message) {
//				System.out.println(message + " MIIII");
//
//			}
//		});

//		CardData cardData = new CardData();
//		cardData.track2Data = "1234567890123456=22071010000012345678";
//		cardData.iccData = "9F0206000000000100";
//		cardData.panSequenceNumber = "123";
//		cardData.posEntryMode = "05";
//		cardData.pinBlock = "1234567890123456";
//		cardData.pan = "1234567890123456";
//		cardData.serviceCode = "123";
//		cardData.expiryDate = "2207";
//		cardData.acquiringInstitutionIdCode = "123456";
////
//		TLVTags tlvTags = new TLVTags();
//		tlvTags.setTLV9F02("value1");
//		tlvTags.setTLV9F03("value2");
//		tlvTags.setTLV82("value3");
//		tlvTags.setTLV9F36("value4");
//		tlvTags.setTLV9F26("value5");
//		tlvTags.setTLV9F27("11111");
//		tlvTags.setTLV9F34("value7");
//		tlvTags.setTLV9F10("value8");
//		tlvTags.setTLV95("value9");
//		tlvTags.setTLV9F35("value10");
//		tlvTags.setTLV9A("value11");
//		tlvTags.setTLV9C("value12");
//		tlvTags.setTLV9F37("value13");
//		tlvTags.setTLV85("value14");
//
//		//
//
//
//		IswParameters iswParams = new IswParameters();
//		iswParams.setPinKey("1234567890");
//		iswParams.setToken("abcd1234");
//		iswParams.setDestinationAccountNumber("0123456789");
//		iswParams.setMerchantId("MERCHANT001");
//		iswParams.setMerchantNameLocation("My Shop, Lagos");
//		iswParams.setTerminalId("TERMINAL001");
//		iswParams.setTerminalSerial("SERIAL123");
//		iswParams.setReceivingInstitutionId("RECEIVE123");
//		iswParams.setInterSwitchThreshold(5000L);
//		iswParams.setRemark("Payment for order #1234");
//
//		//
//		TransactionRequestData transactionRequestData = new TransactionRequestData();
//		transactionRequestData.transactionType = TransactionType.PURCHASE;
//		transactionRequestData.amount = 5000L;
//		transactionRequestData.otherAmount = 0L;
//		transactionRequestData.accountType = IsoAccountType.DEFAULT_UNSPECIFIED;
//		transactionRequestData.RRN = "123456789012";
//		transactionRequestData.STAN = "123456";
//		transactionRequestData.additionalTransParams = null;
//		transactionRequestData.echoData = null;
//		transactionRequestData.originalDataElements = null;
//		transactionRequestData.iswParameters = iswParams;
//		transactionRequestData.tlvTags = tlvTags;
//
//		new ISWProcessPayment().ProcessPayment(cardData, transactionRequestData, new OnTransactionCompleted() {
//			@Override
//			public void onSuccess() {
//
//			}
//
//			@Override
//			public void onError() {
//
//			}
//		});

	}

}
