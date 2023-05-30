package services;

import Var.Debug;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;


import java.util.Map;
import java.util.prefs.Preferences;

public class KeyValuePairStorage {
    Preferences prefs;
    Configuration config;
    FileBasedConfigurationBuilder builder;

    private static KeyValuePairStorage instance;
    public  static KeyValuePairStorage getInstance() {
        if (instance == null) {
            instance = new KeyValuePairStorage();
        }
        return instance;
    }

    public KeyValuePairStorage() {

        Parameters params = new Parameters();
        builder  =
                new FileBasedConfigurationBuilder(KeyValuePairStorage.class)
                        .configure(params.fileBased().setFileName("preferences.properties"));
      try {
          config= (Configuration) builder.getConfiguration();
      }catch (Exception e){
          System.out.println(e.getLocalizedMessage());
      }
    }

    public void put(String key, String value) {
//        this.prefs .put(key, value);
        config.setProperty(key, value);
       try {
           builder.save();
       }catch (Exception e){
           System.out.println(e.getLocalizedMessage());
       }

    }
    public void putLong(String key, Long value) {
//        this.prefs.putLong(key,value);
        config.setProperty(key, value);
        try {
            builder.save();
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String get(String key) {
            return   config.getString("myKey");
//        return this.prefs.get(key, "");
    }
    public Long getLong(String key) {
        return   config.getLong(key);
    }

    public void remove(String key) {
        this.prefs.remove(key);
    }

}
