package com.Enkpay.alltransctionsPOS.nibbs.prep;

import Var.Constants;
import Var.Debug;
import com.Enkpay.alltransctionsPOS.nibbs.enum_file.KeyType;
import com.Enkpay.alltransctionsPOS.nibbs.miscellaneous.ConfigData;
import com.Enkpay.alltransctionsPOS.transaction.PosTransactions;
import com.google.gson.Gson;
import generalModel.KeyHolder;
import services.KeyValuePairStorage;

public class DownloadNibsKeys {
    NibsKeyRequest nibsKeyRequest = new  NibsKeyRequest();

    public void download(PosTransactions.IntResult intResult) {
        KeyHolder keyHolder = new KeyHolder();
        try {
      String encryptedMasterKey =  nibsKeyRequest.get(
              KeyType.MASTER,
              "8767B834"
      ).substring(0, 32);
      String encryptedSessionKey =  nibsKeyRequest.get(
              KeyType.SESSION,
              "8767B834"
      ).substring(0, 32);
      String encryptedPinKey =  nibsKeyRequest.get(
              KeyType.PIN,
              "8767B834"
      ).substring(0, 32);

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
              "8767B834",
              keyHolder.clearSessionKey()
      );

      KeyValuePairStorage loadedStorage =  KeyValuePairStorage.getInstance();
      loadedStorage.put(Constants.PREF_KEYHOLDER, new Gson().toJson(keyHolder));
      loadedStorage.put(Constants.PREF_CONFIG_DATA, new Gson().toJson(configData));
      loadedStorage.putLong(Constants.LAST_POS_CONFIGURATION_TIME, System.currentTimeMillis());
      Debug.print("Fetch from file " +  loadedStorage.get(Constants.PREF_KEYHOLDER));
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
