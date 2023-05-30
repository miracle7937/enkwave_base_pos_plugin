package services;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class KeyValuePairStorage {
    private PreferenceBase storageStrategy;



    public void put(String key, String value) {
        this.storageStrategy.storeStringData(key, value);

    }
    public void putLong(String key, Long value) {
        this.storageStrategy.storeLongData(key,value);

    }

    public String get(String key) {
        return this.storageStrategy.getStringData(key);
    }
    public Long getLong(String key) {
        return  this.storageStrategy.getLongData(key);
    }


}
