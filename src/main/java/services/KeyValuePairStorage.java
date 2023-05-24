package services;

import Var.Debug;

import java.util.Map;
import java.util.prefs.Preferences;

public class KeyValuePairStorage {
    Preferences prefs;

    private static KeyValuePairStorage instance;
    public  static KeyValuePairStorage getInstance() {
        if (instance == null) {
            instance = new KeyValuePairStorage();
        }
        return instance;
    }

    public KeyValuePairStorage() {
   this.prefs = Preferences.userNodeForPackage(KeyValuePairStorage.class);
    }

    public void put(String key, String value) {
        this.prefs .put(key, value);

    }
    public void putLong(String key, Long value) {
        this.prefs.putLong(key,value);

    }

    public String get(String key) {
        return this.prefs.get(key, "");
    }
    public Long getLong(String key) {
        return this.prefs.getLong(key,0);
    }

    public void remove(String key) {
        this.prefs.remove(key);
    }

}
