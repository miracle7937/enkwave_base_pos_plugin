package com.Enkpay.alltransctionsPOS.interswitch.models;

import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Data
@ToString
@Root(strict = false, name = "tokenPassportResponse")
public class TokenPassportResponse {



    @Element(name = "responseMessage", required = false)
    private String responseMessage;


    @Element(name = "responseCode", required = false)
    private String responseCode;

    @Element(name = "token", required = false)
    private String token;
}
