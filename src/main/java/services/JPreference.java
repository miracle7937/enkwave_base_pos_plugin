package services;

import java.util.prefs.Preferences;

public class JPreference implements PreferenceBase {
    Preferences prefs;

    JPreference(){
        this.prefs = Preferences.userNodeForPackage(KeyValuePairStorage.class);
    }
    @Override
    public void storeStringData(String key, Object object) {
        this.prefs .put(key, key);

    }

    @Override
    public void storeLongData(String key, Long  object) {
        this.prefs.putLong(key,object);
    }

    @Override
    public String getStringData(String key) {
        return this.prefs.get(key,"");

    }

    @Override
    public Long getLongData(String key) {
        return this.prefs.getLong(key,0);
    }
}
