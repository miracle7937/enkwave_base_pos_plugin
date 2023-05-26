package com.Enkpay.alltransctionsPOS.nibbs.model;

import com.google.gson.annotations.SerializedName;

public class ExpressPaymentRequestBody {
    @SerializedName("MasterKey")
    private String MasterKey;

    public ExpressPaymentRequestBody(String masterKey) {
        this.MasterKey = masterKey;
    }
}
