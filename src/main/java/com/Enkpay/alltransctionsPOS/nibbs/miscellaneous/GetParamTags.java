package com.Enkpay.alltransctionsPOS.nibbs.miscellaneous;

import Var.Debug;

public class GetParamTags {
    ConfigData configData = new ConfigData();
    public ConfigData get(String key) {
        String ctmkdatetime = "";
        String mid = "";
        String timeout = "";
        String currencycode = "";
        String countrycode = "";
        String callhome = "";
        String mnl = "";
        String mcc = "";
        String f62 = key;
        int loop = 0, i = 0;
        String t = "";
        String l = "";
        String v = "";
        for (i = 0; i < f62.length(); i++) {
            if (loop == 0) {
                t = f62.substring(i, i + 2);
                i = i + 1;
                loop++;
            } else if (loop == 1) {
                l = f62.substring(i, i + 3);
                i = i + 2;
                loop++;
            } else if (loop == 2) {
                int f = Integer.parseInt(l);
                v = f62.substring(i, Math.min(i + f, f62.length()));
                loop = 0;
                i = i + f - 1;
                switch (t) {
                    case "02":
                        ctmkdatetime = v;
                        break;
                    case "03":
                        mid = v;
                        break;
                    case "04":
                        timeout = v;
                        break;
                    case "05":
                        currencycode = v;
                        break;
                    case "06":
                        countrycode = v;
                        break;
                    case "07":
                        callhome = v;
                        break;
                    case "08":
                        mcc = v;
                        break;
                    case "52":
                        mnl = v;
                        break;
                    default:
                        Debug.print("UNKNOWN TLV...");
                        break;
                }
            }
        }
        configData.setCallHome(callhome);
        configData.setCtMkDatetime(ctmkdatetime);
        configData.setMnl(mnl);
        configData.setMcc(mcc);
        configData.setCountryCode(countrycode);
        configData.setCurrencyCode(currencycode);
        configData.setMid(mid);
        configData.setTimeout(timeout);

        Debug.print("callhome " + callhome);
        Debug.print( "ctmkdatetime: " + ctmkdatetime);
        Debug.print( "mcc: " + mcc);
        Debug.print( "countrycode: " + countrycode);
        Debug.print( "mid: " + mid);
        Debug.print("timeout: " + timeout);
        Debug.print("mnl: " + mnl);
        Debug.print( "currencycode: " + currencycode);
        return configData;
    }
}
