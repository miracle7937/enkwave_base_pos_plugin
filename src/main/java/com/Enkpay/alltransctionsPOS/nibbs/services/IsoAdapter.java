package com.Enkpay.alltransctionsPOS.nibbs.services;

import Var.BuildConfig;
import com.Enkpay.alltransctionsPOS.utils.TripleDES;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.ParseException;



public class IsoAdapter {
    java.util.logging.Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    private IsoMessage unpackWith8583(String data) {
        byte[] dataByteArray = data.getBytes(Charset.forName("UTF-8"));
        MessageFactory msgFactory = new MessageFactory();
        msgFactory.setIgnoreLastMissingField(true);
        InputStream inputStream = null;
        try {

            File configFile = new File("config.xml");
            configFile.setReadable(true);
            configFile.setWritable(true);
            configFile.setExecutable(true);
            inputStream = new FileInputStream( configFile);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ConfigParser.configureFromReader(msgFactory, reader);
            IsoMessage isoMessage = msgFactory.parseMessage(dataByteArray, 0);
            logIsoMessage(isoMessage);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("The config.xml file was not found.");
                    e.printStackTrace();
                }
            }
            return isoMessage;
        }
        catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid response received");
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid response received");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }


    public  IsoMessage processISOBitStreamWithJ8583(String data)  {
        String outputData;

        if (data.indexOf("0") >= 0) {
            outputData=data.substring(data.indexOf("0"));
        } else {
            outputData= data;
        }
        return unpackWith8583(outputData);
    }



    public static byte[] prepareByteStream(IsoMessage isoMessage) throws Exception {
        byte[] isoStream = isoMessage.writeData();
        return prepareByteStream(isoStream);
    }
    public static byte[] prepareByteStream(byte[] isoBytes) throws Exception {
        int len = new String(isoBytes).length();
        byte[] headerBytes = new byte[] { (byte)(len >> 8), (byte)(len % 256) };
        System.out.println("Header bytes: " + TripleDES.Hex2String(headerBytes));

        byte[] request = new byte[headerBytes.length + isoBytes.length];
        System.arraycopy(headerBytes, 0, request, 0, headerBytes.length);
        System.arraycopy(isoBytes, 0, request, headerBytes.length, isoBytes.length);
        System.out.println("Request: " + new String(request) + "\n Hex: " + TripleDES.Hex2String(request));
        return request;
    }



    public void logIsoMessage(IsoMessage msg) {
        if (BuildConfig.DEBUG) {
            logger.fine("Epms Library" + "----ISO MESSAGE-----");
            System.out.println("Epms Library" + "----ISO MESSAGE-----");
            try {
                logger.info("Epms Library"+ " MTI : " + Integer.toHexString(msg.getType()));
                for (int i = 1; i <= 128; i++) {
                    if (msg.hasField(i)) {
                        logger.info(("Epms Library"+ "    Field-" + i + " : " + getResponseDataFromIndex(msg, i)));
                        System.out.println(("Epms Library"+ "    Field-" + i + " : " + getResponseDataFromIndex(msg, i)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                logger.info("Epms Library"+ "--------------------");
            }
        }
    }
    private String getResponseDataFromIndex(IsoMessage isoMsg, int index) {
        return isoMsg.getField(index).getValue().toString();
    }





    public static String getMacNibss(String seed, byte[] macDataBytes) throws Exception{
        byte [] keyBytes = h2b(seed);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(keyBytes, 0, keyBytes.length);
        digest.update(macDataBytes, 0, macDataBytes.length);
        byte[] hashedBytes = digest.digest();
        String hashText = b2h(hashedBytes);
        hashText = hashText.replace(" ", "");
        if (hashText.length() < 64) {
            int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";
            String temp = hashText.toString();
            for (int i = 0; i < numberOfZeroes; i++)
                zeroes = zeroes + "0";
            temp = zeroes + temp;
            return temp;
        }
        return hashText;
    }
    private static byte[] h2b(String hex)
    {
        if ((hex.length() & 0x01) == 0x01)
            throw new IllegalArgumentException();
        byte[] bytes = new byte[hex.length() / 2];
        for (int idx = 0; idx < bytes.length; ++idx) {
            int hi = Character.digit((int) hex.charAt(idx * 2), 16);
            int lo = Character.digit((int) hex.charAt(idx * 2 + 1), 16);
            if ((hi < 0) || (lo < 0))
                throw new IllegalArgumentException();
            bytes[idx] = (byte) ((hi << 4) | lo);
        }
        return bytes;
    }

    private static String b2h(byte[] bytes)
    {
        char[] hex = new char[bytes.length * 2];
        for (int idx = 0; idx < bytes.length; ++idx) {
            int hi = (bytes[idx] & 0xF0) >>> 4;
            int lo = (bytes[idx] & 0x0F);
            hex[idx * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
            hex[idx * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
        }
        return new String(hex);
    }

}
