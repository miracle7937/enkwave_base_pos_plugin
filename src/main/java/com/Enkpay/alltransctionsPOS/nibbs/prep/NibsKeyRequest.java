package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Constants;
import com.Enkpay.alltransctionsPOS.nibbs.enum_file.KeyType;
import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;
import com.Enkpay.alltransctionsPOS.nibbs.services.IsoAdapter;
import com.Enkpay.alltransctionsPOS.nibbs.services.SocketRequest;
import com.Enkpay.alltransctionsPOS.utils.IsoTimeManager;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;

import com.solab.iso8583.IsoValue;
import enums.IsoAccountType;

public class NibsKeyRequest {

 public String get(KeyType keyType, String terminalID) throws Exception {
     SocketRequest requestHandler = new SocketRequest(new NibsUtilityData());
     IsoMessage isoMessage = new IsoMessage();
     IsoTimeManager timeMgr = new IsoTimeManager();
     isoMessage.setType(Integer.parseInt(Constants.MTI.NETWORK_MGT_REQUEST_MTI, 16));

     isoMessage.setField(
             Constants.PROCESSING_CODE_3,
             new IsoValue(
                     IsoType.ALPHA,
                     getResponseCode(keyType) + IsoAccountType.DEFAULT_UNSPECIFIED.getCode() + IsoAccountType.DEFAULT_UNSPECIFIED.getCode(),
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
            new  IsoValue(IsoType.NUMERIC, timeMgr.getShortDate(), 4)
     );
     isoMessage.setField(
             Constants.CARD_ACCEPTOR_TERMINAL_ID_41,
            new  IsoValue(IsoType.ALPHA, terminalID, 8)
     );
     byte[] messageBytes = IsoAdapter.prepareByteStream(isoMessage);
     String response = requestHandler.send( messageBytes);
    IsoMessage  parsedResponse = new IsoAdapter().processISOBitStreamWithJ8583( response);
   return   parsedResponse.getField(Constants.SECURITY_RELATED_CONTROL_INFO_53).getValue().toString();
    }

    public String  getResponseCode(KeyType keyType){

        switch (keyType){
            case MASTER:
                return Constants.IsoTransactionTypeCode.TERMINAL_MASTER_KEY;
            case SESSION:
                return Constants.IsoTransactionTypeCode.TERMINAL_SESSION_KEY;
            default:
                return Constants.IsoTransactionTypeCode.TERMINAL_PIN_KEY;



        }}

}
