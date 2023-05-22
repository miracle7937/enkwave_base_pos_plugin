package com.Enkpay.alltransctionsPOS.interswitch.models;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;


@Data
@ToString
@AllArgsConstructor
@Root(strict = false, name = "tokenPassportRequest")
public class TokenPassportRequest {

    @Path("terminalInformation")
    @Element(name = "merchantId", required = false)
    private String merchantId;


    @Path("terminalInformation")
    @Element(name = "terminalId", required = false)
    private String terminalId;
}
