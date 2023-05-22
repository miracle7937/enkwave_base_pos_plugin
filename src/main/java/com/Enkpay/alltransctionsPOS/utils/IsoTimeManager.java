package com.Enkpay.alltransctionsPOS.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IsoTimeManager {

    private Date now = new Date(System.currentTimeMillis());

    public String getLongDate() {
        return new SimpleDateFormat(LONG_DATE, Locale.getDefault()).format(now);
    }

    public String getShortDate() {
        return new SimpleDateFormat(SHORT_DATE, Locale.getDefault()).format(now);
    }

    public String getTime() {
        return new SimpleDateFormat(LONG_TIME, Locale.getDefault()).format(now);
    }

    public String getFullDate() {
        return new SimpleDateFormat(FULL_DATE_TIME, Locale.getDefault()).format(now);
    }

    private static final String LONG_DATE = "MMddHHmmss";
    private static final String SHORT_DATE = "MMdd";
    private static final String LONG_TIME = "HHmmss";
    private static final String FULL_DATE_TIME = "yyyyMMddHHmmss";
}
