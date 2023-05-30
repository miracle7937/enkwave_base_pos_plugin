package services;

public interface PreferenceBase {
    void storeStringData(String  key, Object object);
    void storeLongData(String  key, Long object);
    String getStringData(String  key);
    Long getLongData(String  key);

}
