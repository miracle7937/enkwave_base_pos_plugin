package com.Enkpay.alltransctionsPOS.utils;

public class TerminalField62 {
    public  String  get(){
        String sn = "4567273648402939339";
        String cs = String.format("%03d", sn.length());

        String sF64 = "01" + cs + sn;
        return sF64;
    }
}
