package com.Enkpay.alltransctionsPOS.interswitch.models;


import lombok.Data;
import lombok.ToString;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "transferRequest")
@Data
@ToString
public class TransferRequest {

    @Path("terminalInformation")
    @Element(name = "batteryInformation", required = false)
    private int batteryInformation;

    @Path("terminalInformation")
    @Element(name = "currencyCode", required = false)
    private String currencyCode = "";


    @Path("terminalInformation")
    @Element(name = "languageInfo", required = false)
    private String languageInfo = "";

    @Path("terminalInformation")
    @Element(name = "merchantId", required = false)
    private String merchantId = "";

    @Path("terminalInformation")
    @Element(name = "merhcantLocation")
    private String merchantLocation = "";

    @Path("terminalInformation")
   @Element(name = "posConditionCode")
    private String posConditionCode=  "";

    @Path("terminalInformation")
   @Element(name = "posDataCode")
    private String posDataCode=  "";

    @Path("terminalInformation")
    @Element(name = "posEntryMode")
    private String posEntryMode=  "";

    @Path("terminalInformation")
   @Element(name = "posGeoCode")
    private String posGeoCode=  "";


    @Path("terminalInformation")
   @Element(name = "printerStatus")
    private int printerStatus = 0;

    @Path("terminalInformation")
   @Element(name = "terminalId")
    private String terminalId = "";

    @Path("terminalInformation")
   @Element(name = "terminalType")
    private String terminalType = "";

    @Path("terminalInformation")
   @Element(name = "transmissionDate")
    private String transmissionDate = "";

    @Path("terminalInformation")
   @Element(name = "uniqueId")
    private String uniqueId = "";

    @Path("cardData")
   @Element(name = "cardSequenceNumber")
    private String cardSequenceNumber = "0";

    @Path("cardData/emvData")
   @Element(name = "AmountAuthorized")
    private String  amountAuthorized = "";

    @Path("cardData/emvData")
   @Element(name = "AmountOther")
    private String  amountOther = "";

    @Path("cardData/emvData")
   @Element(name = "ApplicationInterchangeProfile")
    private String applicationInterchangeProfile = "";

    @Path("cardData/emvData")
   @Element(name = "atc")
    private String atc = "";

    @Path("cardData/emvData")
   @Element(name = "Cryptogram")
    private String  cryptogram = "";

    @Path("cardData/emvData")
   @Element(name = "CryptogramInformationData")
    private int  cryptogramInformationData = 0;

    @Path("cardData/emvData")
   @Element(name = "CvmResults")
    private String  cvmResults = "0";

    @Path("cardData/emvData")
   @Element(name = "iad")
    private String  iad = "";

    @Path("cardData/emvData")
   @Element(name = "TransactionCurrencyCode")
    private String  transactionCurrencyCode = "";

    @Path("cardData/emvData")
   @Element(name = "TerminalVerificationResult")
    private String  terminalVerficationResult = "";

    @Path("cardData/emvData")
   @Element(name = "TerminalCountryCode")
    private String  terminalCountryCode = "";

    @Path("cardData/emvData")
   @Element(name = "TerminalType")
    private String  terminalType2 = "";

    @Path("cardData/emvData")
   @Element(name = "TerminalCapabilities")
    private String  terminalCapabilities = "";

    @Path("cardData/emvData")
   @Element(name = "TransactionDate")
    private String transationDate = "";

    @Path("cardData/emvData")
   @Element(name = "TransactionType")
    private String  transactionType = "";

    @Path("cardData/emvData")
   @Element(name = "UnpredictableNumber")
    private String  unpredictableNumber = "";

    @Path("cardData/emvData")
   @Element(name = "DedicatedFileName")
    private String  dedicatedFileName = "";

    @Path("cardData/track2")
   @Element(name = "pan")
    private String  pan = "";

    @Path("cardData/track2")
   @Element(name = "expiryMonth")
    private String  expiryMonth = "";

    @Path("cardData/track2")
   @Element(name = "expiryYear")
    private String  expiryYear = "";

    @Path("cardData/track2")
   @Element(name = "track2")
    private String track2 = "";

   @Element(name = "originalTransmissionDateTime")
   private String  originalTransmissionDate = "";

   @Element(name = "stan")
   private String  stan = "0";

   @Element(name = "retrievalReferenceNumber")
   private String  rrn = "0";

   @Element(name = "fromAccount")
   private String  fromAccount = "";

   @Element(name = "toAccount")
   private String  toAccount = "";

   @Element(name = "minorAmount")
   private String  minorAmount = "";

   @Element(name = "receivingInstitutionId")
   private String receivingInstitutionId = "";

   @Element(name = "surcharge")
   private String surcharge = "";

    @Path("pinData")
   @Element(name = "ksnd")
    private String  ksnd = "";

    @Path("pinData")
   @Element(name = "ksn")
    private String  ksn = "";

    @Path("pinData")
   @Element(name = "pinType")
    private String pinType = "";

    @Path("pinData")
   @Element(name = "pinBlock")
    private String pinBlock = "";

   @Element(name = "keyLabel")
   private String keyLabel = "";

   @Element(name = "destinationAccountNumber")
   private String  destinationAccountNumber = "";
    private String  header = "";

   @Element(name = "extendedTransactionType")
   private String  extendedTransactionType = "";

}
