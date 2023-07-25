package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Constants;
import Var.Debug;
import com.Enkpay.alltransctionsPOS.nibbs.enum_file.KeyType;
import com.Enkpay.alltransctionsPOS.nibbs.miscellaneous.ConfigData;
import com.Enkpay.alltransctionsPOS.transaction.PosTransactions;
import com.google.gson.Gson;
import generalModel.KeyHolder;
import lombok.AllArgsConstructor;
import services.KeyValuePairStorage;
import services.PreferenceBase;
public class DownloadNibsKeys {
    NibsKeyRequest nibsKeyRequest = new  NibsKeyRequest();
    final PreferenceBase preferenceBase;
    public DownloadNibsKeys(PreferenceBase preferenceBase){
        this.preferenceBase = preferenceBase;
        }

    public void download(PosTransactions.IntResult intResult, String terminalID) {
        KeyHolder keyHolder = new KeyHolder();
        Debug.print("Hello " );
        try {
      String encryptedMasterKey =  nibsKeyRequest.get(
              KeyType.MASTER,
              terminalID
      ).substring(0, 32);
      //get clear master from XpressPayment
//            String clearPinKeyFromExpressPayment=     new ClearMasterKeyFromExpressPayment().request(encryptedMasterKey);
//            keyHolder.setClearMasterKey(clearPinKeyFromExpressPayment);
      String encryptedSessionKey =  nibsKeyRequest.get(
              KeyType.SESSION,
              terminalID
      ).substring(0, 32);
      String encryptedPinKey =  nibsKeyRequest.get(
              KeyType.PIN,
              terminalID
      ).substring(0, 32);

            Debug.print("END " );


            Debug.print("encryptedMasterKey " + encryptedMasterKey);
            Debug.print("encryptedSessionKey " + encryptedSessionKey);
            Debug.print("encryptedPinKey " + encryptedPinKey);

      keyHolder.setMasterKey(encryptedMasterKey);
      keyHolder.setSessionKey(encryptedSessionKey);
      keyHolder.setPinKey(encryptedPinKey);
            Debug.print("=======================================================================");
            Debug.print("nibssClearMasterKey " + keyHolder.nibssClearMasterKey());
            Debug.print("clearSessionKey " +  keyHolder.clearSessionKey());
            Debug.print("clearPinKey " + keyHolder.clearPinKey());
            ConfigData configData= new ParametersDownload2().download(
                    terminalID,
              keyHolder.clearSessionKey()
      );
            if(!configData.getMnl().isEmpty()){
                KeyValuePairStorage loadedStorage =  new KeyValuePairStorage(preferenceBase);
                loadedStorage.put(Constants.PREF_KEYHOLDER, new Gson().toJson(keyHolder));
                loadedStorage.put(Constants.PREF_CONFIG_DATA, new Gson().toJson(configData));
                loadedStorage.putLong(Constants.LAST_POS_CONFIGURATION_TIME, System.currentTimeMillis());
                Debug.print("Fetch from file " +  loadedStorage.get(Constants.PREF_KEYHOLDER));

            }else {
                Debug.print("=======================================================================");
                Debug.print("========================Fail to get Parameter=============================================");
                if(intResult != null){
                    intResult.onError("Fail to initialize key");
                }

            }


            if(intResult != null){
                intResult.onSuccess();
            }
  }catch (Exception exception){
            if(intResult != null){
                intResult.onError( exception.getLocalizedMessage());
            }

        }
    }
}
