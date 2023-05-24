package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Debug;
import com.Enkpay.alltransctionsPOS.interswitch.models.AdditionalTransParams;
import com.Enkpay.alltransctionsPOS.nibbs.miscellaneous.*;
import com.Enkpay.alltransctionsPOS.nibbs.model.HostConfig;
import com.Enkpay.alltransctionsPOS.transaction.PosTransactions;
import com.Enkpay.alltransctionsPOS.utils.APPUtils;
import com.Enkpay.alltransctionsPOS.utils.CardData;
import com.Enkpay.alltransctionsPOS.utils.IsoTimeManager;
import enums.IsoAccountType;
import enums.TransactionType;
import generalModel.TransactionRequestData;
import generalModel.TransactionResponse;
import lombok.AllArgsConstructor;
import org.jpos.iso.ISOUtil;



@AllArgsConstructor
public class ProcessTransaction {
    private static final String TAG = ProcessTransaction.class.getSimpleName();
    final HostConfig hostConfig;

    public void process(CardData cardData, TransactionRequestData requestData, PosTransactions.TransactionResult transactionResult) {

        ISO8583 requestIsoMessage = setBaseFields(requestData, cardData, hostConfig.getConfigData());

        switch (requestData.getTransactionType()) {
            case REVERSAL: {

                byte[] field11 = requestData.getOriginalDataElements().getOriginalSTAN().getBytes();
                requestIsoMessage.setBit(11, field11, field11.length);

                //not this when u running .substring(4)
                byte[] field12 = requestData.getOriginalDataElements().getOriginalTransmissionTime().substring(4).getBytes();
                requestIsoMessage.setBit(12, field12, field12.length);

                byte[] field13 = requestData.getOriginalDataElements().getOriginalTransmissionTime().substring(0, 4).getBytes();
                requestIsoMessage.setBit(13, field13, field13.length);

                byte[] field37 = requestData.getOriginalDataElements().getOriginalRRN().getBytes();
                requestIsoMessage.setBit(37, field37, field37.length);

                byte[] field38 = requestData.getOriginalDataElements().getOriginalAuthorizationCode().getBytes();
                requestIsoMessage.setBit(38, field38, field38.length);

                //reason code
                byte[] field56 = "4021".getBytes();
                requestIsoMessage.setBit(56, field56, field56.length);
                setOriginalTransactionData(requestIsoMessage, requestData);

            }
            case PURCHASE: {
                System.out.println("Purchase");
                break;
            }

        }


        ISO8583.sec = true;
        byte[] preUnmac = requestIsoMessage.getMacIso();
        Debug.print(TAG + " PRE ISO BEFORE MAC: " + new String(preUnmac));
        byte[] unMac = new byte[preUnmac.length - 64];
        System.arraycopy(preUnmac, 0, unMac, 0, preUnmac.length - 64);
        Debug.print(TAG + " ISO BEFORE MAC: " + new String(unMac));
        Debug.print(TAG + " CLEAR SESSION KEY USED: " + hostConfig.getKeyHolder().clearSessionKey());

        EncDec enc = new EncDec();
        String gotten = null;
        try {
            gotten = enc.getMacNibss(hostConfig.getKeyHolder().clearSessionKey(), unMac);
            System.out.println(TAG + "MAC: " + gotten);
        } catch (Exception e) {
            e.printStackTrace();
            Debug.print(e.fillInStackTrace());
        }

        byte[] field128 = gotten.getBytes();
        requestIsoMessage.setBit(128, field128, field128.length);
        ISO8583.sec = true;
        byte[] packData = requestIsoMessage.isotostr();
        System.out.println(TAG + "ISO TO HOST: " + ISOUtil.hexString(packData));
        byte[] getSending = new byte[packData.length - 2];
        System.arraycopy(packData, 2, getSending, 0, packData.length - 2);
        ISO8583.sec = true;
        ISO8583 unpackISO8583 = new ISO8583();
        unpackISO8583.strtoiso(getSending);
        String[] sending = new String[128];
        Utilities.logISOMsgMute(unpackISO8583, sending);


        byte[] recvarr = null;
        if (hostConfig.getConnectionData().getIsSSL()) {
            try {
                recvarr = SSLTLS.doSSL(hostConfig.getConnectionData().getIpAddress(), hostConfig.getConnectionData().getIpPort().toString(), packData);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            CommSocket send = new CommSocket();
            if (send.open(hostConfig.getConnectionData().getIpAddress(), hostConfig.getConnectionData().getIpPort())) {
                Debug.print(TAG + " OPEN SUCCESS");
                int count = send.send(packData);
                Debug.print(TAG + "SENT: " + count);
                recvarr = send.recv();
                send.close();
            } else {

                System.out.println(TAG + " Could Not Open: " + hostConfig.getConnectionData().getIpAddress() + ":" + hostConfig.getConnectionData().getIpPort());
            }

        }

        if (recvarr == null) {
            Debug.print("Receiver is null please refresh");
            transactionResult.onError("Receiver is null please refresh");

        } else {

            String resp = new String(recvarr);
            byte[] response;

            int l = resp.indexOf(requestData.getTransactionType().getMTIResponse());
            String des = resp.substring(l);
            response = des.getBytes();
            Debug.print(TAG + "  SECOND STEP: " + l);

            Debug.print(TAG + "  PARSED RESPONSE FROM HOST: " + new String(response));
            Debug.print(TAG + "  PARSED RESPONSE FROM HOST LENGTH: " + new String(response).length());
            ISO8583.sec = true;
            unpackISO8583.strtoiso(response);
            String[] receiving = new String[128];
            Utilities.logISOMsg(unpackISO8583, receiving);

            transactionResult.onSuccess(new TransactionResponse().parseNibssMessage(receiving, requestData.getTransactionType()));
        }


    }





    private void setOriginalTransactionData(ISO8583 requestIsoMessage, TransactionRequestData requestData) {
        if (requestData.getOriginalDataElements() != null) {

            String originalElement = "0200" +
                    requestData.getOriginalDataElements().getOriginalSTAN()
                    + requestData.getOriginalDataElements().getOriginalTransmissionDateTime()
                    + Utilities.padLeftZeros(requestData.getOriginalDataElements().getOriginalAcquiringInstCode(), 11)
                    + Utilities.padLeftZeros(requestData.getOriginalDataElements().getOriginalForwardingInstCode(), 11);



            byte[] field90= originalElement.getBytes();
            requestIsoMessage.setBit(90, field90, field90.length);


            if(requestData.transactionType == TransactionType.REVERSAL){
                byte[] field95= "000000000000000000000000D00000000D00000000".getBytes();
                requestIsoMessage.setBit(95, field95, field95.length);
            }

            if(requestData.transactionType == TransactionType.REFUND){
                Long koboAmount = requestData.refundAmount*100;
                String finalAmount = Utilities.padLeft(koboAmount + "", 12, '0');
                byte[] field95= (finalAmount+ "000000000000D00000000D00000000").getBytes();
                requestIsoMessage.setBit(95, field95, field95.length);
            }


        }
    }









    public ISO8583 setBaseFields(TransactionRequestData requestData, CardData cardData, ConfigData configData) {
        AdditionalTransParams addTransParams = requestData.getAdditionalTransParams();
        IsoTimeManager timeMgr = new IsoTimeManager();
        String transmissionDateAndTime = timeMgr.getLongDate();
        String sequenceNumber = timeMgr.getTime();
        String timeLocalTransaction = timeMgr.getTime();
        String dateLocalTransaction = timeMgr.getShortDate();
        String RRN;
        if (APPUtils.isEmpty(requestData.getRRN())) {
            RRN = timeMgr.getFullDate().substring(2, 14);
            ;
        } else {
            RRN = requestData.getRRN();
        }

        ISO8583 packISO8583 = new ISO8583();
        packISO8583.setMit(requestData.transactionType.getMTI());
        packISO8583.clearBit();
        String processingCode =
                requestData.transactionType.getCode() + requestData.accountType.getCode() + IsoAccountType.DEFAULT_UNSPECIFIED.getCode();


        byte[] field2 = cardData.getPan().getBytes();
        packISO8583.setBit(2, field2, field2.length);

        byte[] field3 = processingCode.getBytes();
        packISO8583.setBit(3, field3, field3.length);

        Long transactionAmount = (requestData.getAmount() + requestData.getOtherAmount());
        String paddedAmount = Utilities.padLeftZeros(transactionAmount.toString(), 12);
        byte[] field4 = paddedAmount.getBytes(); //check also
        packISO8583.setBit(4, field4, field4.length);


        byte[] field7;
        if (APPUtils.isEmpty(addTransParams.getTransmissionDateF7())) {
            field7 = transmissionDateAndTime.getBytes();
        } else {
            field7 = addTransParams.getTransmissionDateF7().getBytes();
        }
        packISO8583.setBit(7, field7, field7.length);


        byte[] field11;
        if (APPUtils.isEmpty(addTransParams.getStanF11())) {
            field11 = sequenceNumber.getBytes();
        } else {
            field11 = addTransParams.getStanF11().getBytes();
        }
        packISO8583.setBit(11, field11, field11.length);


        byte[] field12;
        if (APPUtils.isEmpty(addTransParams.getLocalTimeF12())) {
            field12 = timeLocalTransaction.getBytes();
        } else {
            field12 = addTransParams.getLocalTimeF12().getBytes();
        }
        packISO8583.setBit(12, field12, field12.length);


        byte[] field13;
        if (APPUtils.isEmpty(addTransParams.getLocalDateF13())) {
            field13 = dateLocalTransaction.getBytes();
        } else {
            field13 = addTransParams.getLocalDateF13().getBytes();
        }
        packISO8583.setBit(13, field13, field13.length);


        byte[] field14 = cardData.getExpiryDate().getBytes();
        packISO8583.setBit(14, field14, field14.length);


        byte[] field18 = configData.getMcc().getBytes();
        packISO8583.setBit(18, field18, field18.length);

        byte[] field22 = "051".getBytes(); // check too POS entry mode
        packISO8583.setBit(22, field22, field22.length);

        if (cardData.getPanSequenceNumber().length() == 3) {
            byte[] field23 = cardData.getPanSequenceNumber().getBytes(); // check too Card sequence number eg master card number, visa number
            packISO8583.setBit(23, field23, field23.length);
        }


        byte[] field25 = "00".getBytes(); // check too POS condition code Good
        packISO8583.setBit(25, field25, field25.length);

        byte[] field26 = "06".getBytes(); // check too POS PIN capture code length pin
        packISO8583.setBit(26, field26, field26.length);


        byte[] field28 = "D00000000".getBytes(); // check too Amount, transaction fee Good
        packISO8583.setBit(28, field28, field28.length);

        byte[] field30 = "C00000000".getBytes();
        packISO8583.setBit(30, field30, field30.length);


        byte[] field32 = cardData.getTrack2Data().substring(0, 6).getBytes(); //track2Data.substring(0, 6); that the answer  // check too Acquiring institution id code
        packISO8583.setBit(32, field32, field32.length);

        byte[] field35 = cardData.getTrack2Data().getBytes();
        packISO8583.setBit(35, field35, field35.length);

        // check Retrieval reference number
        byte[] field37;
        if (APPUtils.isEmpty(addTransParams.getRrnF37())) {
            field37 = RRN.getBytes();
        } else {
            field37 = addTransParams.getRrnF37().getBytes();
        }
        packISO8583.setBit(37, field37, field37.length);


        // Service Restriction Code
        byte[] field40 = Utilities.getServiceCode(cardData.getTrack2Data()).getBytes();
        packISO8583.setBit(40, field40, field40.length);


        byte[] field41 = hostConfig.getTerminalId().getBytes(); //device ID
        packISO8583.setBit(41, field41, field41.length);


        byte[] field42 = configData.getMid().getBytes();
        packISO8583.setBit(42, field42, field42.length);


        byte[] field43 = configData.getMnl().getBytes();
        packISO8583.setBit(43, field43, field43.length);


        byte[] field49 = configData.getCountryCode().getBytes();// currency code
        packISO8583.setBit(49, field49, field49.length);

        if (!APPUtils.isEmpty(cardData.getPinBlock())) {
            byte[] field52 = cardData.getPinBlock().getBytes(); // pin block
            packISO8583.setBit(52, field52, field52.length);

        }
        byte[] field55 = cardData.getIccData().toUpperCase().getBytes(); // ICCData
        packISO8583.setBit(55, field55, field55.length);


        if (ProfileParser.field59 != null) {
            if (Integer.parseInt(ProfileParser.txnNumber) == 2) {
                int i = ProfileParser.field59.indexOf("Meter Number=12^") + 16;
                Debug.print(TAG + " INTEGER: " + i);
                if (i <= 15) {
                    byte[] field59 = ProfileParser.field59.getBytes();
                    packISO8583.setBit(59, field59, field59.length);
                } else {
                    String fin = ProfileParser.field59.substring(0, i) + ProfileParser.field62 + "^" + ProfileParser.field59.substring(i);
                    ProfileParser.field59 = fin;
                    byte[] field59 = ProfileParser.field59.getBytes();
                    packISO8583.setBit(59, field59, field59.length);
                }
            } else {
                byte[] field59 = " ".getBytes();
                packISO8583.setBit(59, field59, field59.length);
            }

            byte[] field123 = "510101511344101".getBytes(); // Good
            packISO8583.setBit(123, field123, field123.length);

            byte use = 0x0;
            char ch = (char) use;
            byte[] field128 = Character.toString(ch).getBytes();
            packISO8583.setBit(128, field128, field128.length);

        }
        return packISO8583;
    }


}