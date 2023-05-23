package services;

import Var.Debug;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyValuePairStorage {
        Path userHomeDir = Paths.get("").toAbsolutePath();
    String filePath = userHomeDir.toString() + "/savedata.txt";
    private Map<String, String> data;
    private static KeyValuePairStorage instance;
    public  static KeyValuePairStorage getInstance() {
        if (instance == null) {
            instance = new KeyValuePairStorage();
        }
        return instance;
    }

    public KeyValuePairStorage() {
        data = new HashMap();
        load();
    }

    public void put(String key, String value) {
        data.put(key, value);
        save();
    }
    public void putLong(String key, Long value) {
        data.put(key,String.valueOf( value));
        save();
    }

    public String get(String key) {
        return data.get(key);
    }
    public Long getLong(String key) {
        return Long.parseLong(data.get(key));
    }

    public void remove(String key) {
        data.remove(key);
    }
    private void save()  {
     try {
         FileWriter writer = new FileWriter(filePath);
         for (Map.Entry<String, String> entry : data.entrySet()) {
             writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
         }
         writer.close();
         Debug.print("Data Saved");
     }catch (IOException exception){
         Debug.print(exception.getLocalizedMessage());
     }
    }


    private void load() {
       try {

           File file = new File(filePath);

           if (!file.exists()) {
               if (file.createNewFile()) {
                   Debug.print("File created successfully.");
               } else {
                   Debug.print("Failed to create file. File already exists.");
               }
           }

           FileReader reader = new FileReader(file);
           BufferedReader bufferedReader = new BufferedReader(reader);
           String line;
           while ((line = bufferedReader.readLine()) != null) {
               String[] parts = line.split("=");
               data.put(parts[0], parts[1]);
           }

           reader.close();
       }catch (IOException exception){

       }
    }
}
