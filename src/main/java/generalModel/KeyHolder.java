package generalModel;


import Var.PosMode;
import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;
import com.Enkpay.alltransctionsPOS.nibbs.services.Decrypter;
import com.Enkpay.alltransctionsPOS.utils.TripleDES;
import lombok.Data;

import javax.crypto.SecretKey;

import static com.Enkpay.alltransctionsPOS.nibbs.services.Decrypter.*;

@Data
public class KeyHolder {
    public int id = 1;
    public String masterKey;
    private String clearMasterKey;
    public String sessionKey;
    public String pinKey;
    public String track2Key;
    public String bdk;
    private PosMode posMode = PosMode.EPMS;



    public KeyHolder() {

    }

public  void  setClearPinKey(String value){
    clearMasterKey= value;
}
    public boolean isValid() {
        return clearPinKey() != null && pinKey != null && masterKey != null;
    }

    public String nibssClearMasterKey() {
        return   clearMasterKey!= null? clearMasterKey  :  threeDesDecryptA(NibsUtilityData.compKey1, NibsUtilityData.compKey2,masterKey );
    }

    public String clearSessionKey() {
       return Decrypter.threeDesDecrypt(sessionKey,  nibssClearMasterKey());
    }

    public String clearPinKey() {
        return Decrypter.threeDesDecrypt(pinKey, nibssClearMasterKey());
    }


    public static String threeDesDecryptA(String keyComponent1, String keyComponent2, String encryptedToken) {
        byte[] keyB1 = hexToByte(keyComponent1 + keyComponent1.substring(0, 16));
        byte[] keyB2 = hexToByte(keyComponent2 + keyComponent2.substring(0, 16));

        for (int i = 0; i < keyB2.length; ++i) {
            keyB1[i] ^= keyB2[i];
        }


        SecretKey key = readKey(keyB1);


        return Decrypt(key, encryptedToken);


    }
}