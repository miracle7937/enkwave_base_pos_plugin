package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Constants;
import Var.Debug;
import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;
import com.Enkpay.alltransctionsPOS.nibbs.services.IsoAdapter;
import com.Enkpay.alltransctionsPOS.nibbs.services.SocketRequest;
import com.Enkpay.alltransctionsPOS.utils.APPUtils;
import com.Enkpay.alltransctionsPOS.utils.IsoTimeManager;
import com.Enkpay.alltransctionsPOS.utils.TerminalField62;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import enums.IsoAccountType;
import enums.TransactionType;

public class ParametersDownload {
   public String download(TransactionType transactionType,
                        String terminalID,
                        String sessionKey,
                        Integer responseDataIndex,
                        IsoAccountType accountType){
       String field62String =  String.format("01%03d%s", "0123456789ABC".length(), "0123456789ABC");
       SocketRequest requestHandler = new SocketRequest(new NibsUtilityData());
       IsoMessage isoMessage = new IsoMessage();
       IsoTimeManager timeMgr = new IsoTimeManager();
       isoMessage.setType(Integer.parseInt(Constants.MTI.NETWORK_MGT_REQUEST_MTI, 16));
       isoMessage.setField(
               Constants.PROCESSING_CODE_3,
              new IsoValue(
                       IsoType.ALPHA,
                      transactionType.getCode() + accountType.getCode() + IsoAccountType.DEFAULT_UNSPECIFIED.getCode(),
                       6
               )
       );

       isoMessage.setField(
               Constants.TRANSMISSION_DATE_TIME_7,
               new IsoValue(IsoType.NUMERIC, timeMgr.getLongDate(), 10)
       );
       isoMessage.setField(
               Constants.SYSTEMS_TRACE_AUDIT_NUMBER_11,
               new IsoValue(IsoType.NUMERIC, timeMgr.getTime(), 6)
       );
       isoMessage.setField(
               Constants.TIME_LOCAL_TRANSACTION_12,
               new IsoValue(IsoType.NUMERIC, timeMgr.getTime(), 6)
       );
       isoMessage.setField(
               Constants.DATE_LOCAL_TRANSACTION_13,
               new IsoValue(IsoType.NUMERIC, timeMgr.getShortDate(), 4)
       );
       isoMessage.setField(
               Constants.CARD_ACCEPTOR_TERMINAL_ID_41,
               new IsoValue(IsoType.ALPHA, terminalID, 8)
       );
       isoMessage.setField(
               Constants.PRIVATE_FIELD_MGT_DATA1_62,
               new IsoValue(IsoType.LLLVAR, field62String)
       );

       System.out.println("Parameter ---> ");
         new IsoAdapter().logIsoMessage(isoMessage);
try{

       byte[] isoMsgByteArray;

    if (sessionKey != null) {
           isoMessage.setField(Constants.PRIMARY_MESSAGE_HASH_VALUE_64, new IsoValue(IsoType.ALPHA, "", 64));
           String messageString = new String(isoMessage.writeData());
           String hash = APPUtils.getMacNibss(sessionKey,messageString.getBytes());
           System.out.println("hash --->1 "+ hash);
           messageString = messageString+ hash.toUpperCase();
           isoMsgByteArray = IsoAdapter.prepareByteStream(messageString.getBytes());
           System.out.println("messageString --->   "+ byteArrayToHexString(isoMsgByteArray));
    } else {
           isoMsgByteArray = IsoAdapter.prepareByteStream(isoMessage);
       }


   new IsoAdapter().logIsoMessage(isoMessage);
    Debug.print("PARAMETER BODY " + new String(isoMsgByteArray));
    String response  = requestHandler.send(isoMsgByteArray);
    System.out.println("Response --->" + response.length());

    IsoMessage  parsedResponse = new IsoAdapter().processISOBitStreamWithJ8583( response);
    String responseCode =     parsedResponse.getField(Constants.RESPONSE_CODE_39).getValue().toString();
    if (responseCode != "00") {
        throw new Exception(Constants.getResponseMessageFromCode(responseCode));
    }

  return  parsedResponse.getField(responseDataIndex).getValue().toString();
} catch (Exception e) {
    throw new RuntimeException(e);
}

   }
    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
