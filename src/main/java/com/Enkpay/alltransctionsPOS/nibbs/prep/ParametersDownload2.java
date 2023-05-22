package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Debug;
import com.Enkpay.alltransctionsPOS.nibbs.miscellaneous.*;
import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;
import com.Enkpay.alltransctionsPOS.utils.IsoTimeManager;
import com.solab.iso8583.IsoMessage;
import org.jpos.iso.ISOUtil;

public class ParametersDownload2 {
    private final String TAG = ParametersDownload2.class.getSimpleName();

    public ConfigData download(
                           String terminalID,
                           String sessionKey
                           ){

        NibsUtilityData nibsUtilityData= new NibsUtilityData();
        ISO8583 packISO8583 = new ISO8583();
        IsoMessage isoMessage = new IsoMessage();
        IsoTimeManager timeMgr = new IsoTimeManager();
        packISO8583.setMit("0800");
        packISO8583.clearBit();
        byte[] field3 = "9C0000".getBytes();
        packISO8583.setBit(3, field3, field3.length);

        byte[] field7 =  timeMgr.getLongDate().getBytes();
        packISO8583.setBit(7, field7, field7.length);

        byte[] field11 = timeMgr.getTime().getBytes();
        packISO8583.setBit(11, field11, field11.length);
        byte[] field12 =   timeMgr.getTime().getBytes();
        packISO8583.setBit(12, field12, field12.length);


        byte[] field13 =  timeMgr.getShortDate().getBytes();
        packISO8583.setBit(13, field13, field13.length);
        byte[] field41 = terminalID.getBytes();
        packISO8583.setBit(41, field41, field41.length);

        String sn = "4567273648402939339";
        String cs = String.format("%03d", sn.length());

        String sF64 = "01" + cs + sn;//sn
        byte[] field62 = sF64.getBytes();
        packISO8583.setBit(62, field62, field62.length);

        byte use = 0x0;
        char ch = (char) use;
        byte[] field64 = Character.toString(ch).getBytes();
        packISO8583.setBit(64, field64, field64.length);
        ISO8583.sec = false;
        byte[] unMac = packISO8583.getMacIso();
        byte[] unMacTest = new byte[unMac.length - 64];
        System.arraycopy(unMac, 0, unMacTest, 0, unMac.length - 64);

        EncDec enc = new EncDec();
        String gotten = null;
        try {
            Debug.print(TAG+ "CLEAR SESSION KEY USED: " + sessionKey);
            gotten = enc.getMacNibss(sessionKey, unMacTest);
            Debug.print(TAG+ "PARAMETER Key: " + gotten);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assert gotten != null;
        field64 = gotten.getBytes();
        packISO8583.setBit(64, field64, field64.length);
        byte[] packData = packISO8583.isotostr();

        Debug.print(TAG+ "PARAMETER. ISO TO HOST: " + new String(packData));
        Debug.print(TAG+ "ISO TO HOST: " + ISOUtil.hexString(packData));
        byte[] recvarr = null;
        ISO8583 unpackISO8583 = new ISO8583();
        if(nibsUtilityData.getIsSSL())
        {

            try {
                Debug.print("PARAMETER BODY " + new String(packData));

                recvarr =  SSLTLS.doSSL(nibsUtilityData.getIpAddress(), nibsUtilityData.getIpPort().toString(), packData);
                Debug.print("Response " + recvarr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (recvarr != null && recvarr.length > 65)
            {
                ProfileParser.parahost1 = new byte[recvarr.length - 4];
                System.arraycopy(recvarr, 4, ProfileParser.parahost1, 0, recvarr.length - 4);
                unpackISO8583.strtoiso(ProfileParser.parahost1);
            }
        }else {
            CommSocket send = new CommSocket();
            if (send.open(nibsUtilityData.getIpAddress(), nibsUtilityData.getIpPort().toString()) == true) {
                Debug.print(TAG+ "OPEN SUCCESS");
                int count = send.send(packData);
                Debug.print(TAG+ "SENT: " + count);
                recvarr = send.recv();
                Debug.print(TAG+ "----------> "+ recvarr);
                send.close();
            } else {

                Debug.print(TAG+ "PARAMETER FAILED");
                Debug.print(TAG+ "Could Not Open: " + ProfileParser.hostip + ":" + ProfileParser.hostport);
            }
            if (recvarr != null && recvarr.length > 65)
            {
                ProfileParser.parahost1 = new byte[recvarr.length - 2];
                System.arraycopy(recvarr, 2, ProfileParser.parahost1, 0, recvarr.length - 2);
                unpackISO8583.strtoiso(ProfileParser.parahost1);
            }
        }
        String[] receiving;

        if (recvarr == null || recvarr.length < 65) {
            Debug.print(TAG+  "PARAMETER DOWNLAOD FAILED");
            Debug.print(TAG+ "PARAMETER FAILED");
            receiving = new String[128];
            return null;
        }
        Debug.print(TAG+"RECV: " + new String(recvarr));
        Debug.print(TAG+ "2. RECV: " + ISOUtil.hexString(recvarr));
        //Log.i(TAG, "DONE WITH CONNECTION");
        receiving = new String[128];
        Utilities.logISOMsg(unpackISO8583, receiving);

        if(receiving[62] == null)
        {
            receiving = new String[128];
            Debug.print(TAG+ "PARAMETER DOWNLOAD SUCCESSFUL - HOST TWO");


        }

        String key = receiving[62];

        if(key.length() < 50 ||  !receiving[39].equals("00"))
        {
            Debug.print(TAG+ "PARAMETER FAILED\" - HOST TWO");
        }
        Debug.print(TAG + "FIELD 62:=====> " + key);
        return new GetParamTags().get(key);



    }
}
