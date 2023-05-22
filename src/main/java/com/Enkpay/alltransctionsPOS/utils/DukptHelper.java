package com.Enkpay.alltransctionsPOS.utils;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class DukptHelper {
    String IPEK_TEST = "9F8011E7E71E483B";
    String KSN_TEST = "0000000006DDDDE01500";

    String  IPEK_LIVE = "3F2216D8297BCE9C";
    String KSN_LIVE = "0000000002DDDDE00001";



    public static String getSessionKey(String IPEK, String KSN) {
        String initialIPEK = IPEK;
        String ksn = String.format("%20s", KSN).replace(' ', '0');
        System.out.println("The expected value of the ksn " + ksn);
        String sessionkey = "";

        // Get ksn with a zero counter by ANDing it with 0000FFFFFFFFFFE00000
        String newKSN = XORorANDorORfunction(ksn, "0000FFFFFFFFFFE00000", "&");
        System.out.println("The expected value of the new KSN is " + newKSN);

        String counterKSN = ksn.substring(ksn.length() - 5).format("%016d", 0);

        // get the number of binary associated with the counterKSN number
        String newKSNtoleft16 = newKSN.substring(newKSN.length() - 16);
        String counterKSNbin = Integer.toBinaryString(Integer.parseInt(counterKSN, 16));
        System.out.println("The expected value of the counter KSN Bin " + counterKSNbin);

        String binarycount = counterKSNbin;
        for (int i = 0; i < counterKSNbin.length(); i++) {
            int len = binarycount.length();
            String result = "";
            if (binarycount.substring(0, 1).equals("1")) {
                result = "1".concat(new String(new char[len - 1]).replace("\0", "0"));
                System.out.println("The expected value of the result is " + result);
                binarycount = binarycount.substring(1);
                System.out.println("The expected value of the new binary count is " + binarycount);
            } else {
                binarycount = binarycount.substring(1);
                System.out.println("The expected value of the new binary count is " + binarycount);
                continue;
            }
            String counterKSN2 = String.format("%16s", Integer.toHexString(Integer.parseInt(result, 2)).toUpperCase()).replace(' ', '0');
            String newKSN2 = XORorANDorORfunction(newKSNtoleft16, counterKSN2, "|");
            sessionkey = BlackBoxLogic(newKSN2, initialIPEK); // Call the Black Box from here
            newKSNtoleft16 = newKSN2;
            initialIPEK = sessionkey;
        }

        String checkWorkingKey = XORorANDorORfunction(sessionkey, "00000000000000FF00000000000000FF", "^");
        System.out.println("*************************The expected value of the working key is " + checkWorkingKey);
        return XORorANDorORfunction(sessionkey, "00000000000000FF00000000000000FF", "^");
    }

    public static String XORorANDorORfunction(String valueA, String valueB, String symbol) {
        char[] a = valueA.toCharArray();
        char[] b = valueB.toCharArray();
        String result = "";

        for (int i = 0; i <= a.length - 1; i++) {
            int parseInt = Integer.parseInt(String.valueOf(b[i]), 16);
            int anInt = Integer.parseInt(String.valueOf(a[i]), 16);
            if (symbol.equals("|")) {
                result += Integer.toString(anInt |
                        parseInt, 16).toUpperCase();
            } else if (symbol.equals("^")) {
                result += Integer.toString(anInt ^
                        parseInt, 16).toUpperCase();
            } else {
                result += Integer.toString(anInt &
                        parseInt, 16).toUpperCase();
            }
        }
        return result;
    }


    public static String BlackBoxLogic(String ksn, String iPek) {
        if (iPek.length() < 32) {
            String msg = XORorANDorORfunction(iPek, ksn, "^");
            String desreslt = desEncrypt(msg, iPek);
            String rsesskey = XORorANDorORfunction(desreslt, iPek, "^");
            return rsesskey;
        }
        String current_sk = iPek;
        String ksn_mod = ksn;
        String leftIpek = XORorANDorORfunction(current_sk, "FFFFFFFFFFFFFFFF0000000000000000", "&").substring(16);
        String rightIpek = XORorANDorORfunction(current_sk, "0000000000000000FFFFFFFFFFFFFFFF", "&").substring(16);
        String message = XORorANDorORfunction(rightIpek, ksn_mod, "^");
        String desresult = desEncrypt(message, leftIpek);
        String rightSessionKey = XORorANDorORfunction(desresult, rightIpek, "^");
        String resultCurrent_sk = XORorANDorORfunction(current_sk, "C0C0C0C000000000C0C0C0C000000000", "^");
        String leftIpek2 = XORorANDorORfunction(resultCurrent_sk, "FFFFFFFFFFFFFFFF0000000000000000", "&").substring(0, 16);
        String rightIpek2 = XORorANDorORfunction(resultCurrent_sk, "0000000000000000FFFFFFFFFFFFFFFF", "&").substring(16);
        String message2 = XORorANDorORfunction(rightIpek2, ksn_mod, "^");
        String desresult2 = desEncrypt(message2, leftIpek2);
        String leftSessionKey = XORorANDorORfunction(desresult2, rightIpek2, "^");
        return leftSessionKey + rightSessionKey;
    }
    public static String encryptPinBlock(String pan, String pin) {
        pan = padStart(pan.substring(pan.length() - 13).substring(0, 12), 16, '0');;
        System.out.println("The expected value of the encrypted pan is " + pan);
        pin = "0" + Integer.toHexString(pin.length()) +   padEnd(pin, 16, 'F');
        String ppp = XORorANDorORfunction(pan, pin, "^");
        return ppp;
    }

    public static String byteArrayToHexString(byte[] key) {
        StringBuilder st = new StringBuilder();
        for (byte b : key) {
            st.append(String.format("%02X", b));
        }
        return st.toString();
    }

    public static byte[] hexStringToByteArray(String key) {
        byte[] result = new byte[0];
        for (int i = 0; i < key.length(); i += 2) {
            result = Arrays.copyOf(result, result.length + 1);
            result[result.length - 1] = (byte) Integer.parseInt(key.substring(i, (i + 2)), 16);
        }
        return result;
    }
    public static String desEncrypt(String desData, String key) {
        byte[] keyData = hexStringToByteArray(key);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyData);
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            bout.write(cipher.doFinal(hexStringToByteArray(desData)));
        } catch (Exception e) {
            System.out.println("Exception DES Encryption.. ");
            e.printStackTrace();
        }
        return byteArrayToHexString(bout.toByteArray()).substring(0, 16);
    }

    public static String DesEncryptDukpt(String workingKey, String clearPinBlock) {
        String pinBlock = XORorANDorORfunction(workingKey, clearPinBlock, "^");
        byte[] keyData = hexStringToByteArray(workingKey);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyData);
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            bout.write(cipher.doFinal(hexStringToByteArray(pinBlock)));
        } catch (Exception e) {
            System.out.println("Exception .. " + e.getMessage());
        }
        return XORorANDorORfunction(workingKey, byteArrayToHexString(bout.toByteArray()).substring(0, 16), "^");
    }

    public static String padStart(String str, int minLength, char padChar) {
        if (str.length() < minLength) {
            StringBuilder sb = new StringBuilder(minLength);
            int padLen = minLength - str.length();
            for (int i = 0; i < padLen; i++) {
                sb.append(padChar);
            }
            sb.append(str);
            return sb.toString();
        } else {
            return str;
        }
    }
    public static String padEnd(String str, int minLength, char padChar) {
        if (str.length() < minLength) {
            StringBuilder sb = new StringBuilder(minLength);
            sb.append(str);
            int padLen = minLength - str.length();
            for (int i = 0; i < padLen; i++) {
                sb.append(padChar);
            }
            return sb.toString();
        } else {
            return str;
        }
    }


}
