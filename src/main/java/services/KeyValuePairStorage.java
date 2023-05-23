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
import java.util.prefs.Preferences;

public class KeyValuePairStorage {
    Preferences prefs;

    private Map<String, String> data;
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
//    private void save()  {
//     try {
//         FileWriter writer = new FileWriter(filePath);
//         for (Map.Entry<String, String> entry : data.entrySet()) {
//             writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
//         }
//         writer.close();
//         Debug.print("Data Saved");
//     }catch (IOException exception){
//         Debug.print(exception.getLocalizedMessage());
//     }
//    }


//    private void load() {
//       try {
//
//           File file = new File(filePath);
//
//           if (!file.exists()) {
//               if (file.createNewFile()) {
//                   Debug.print("File created successfully.");
//               } else {
//                   Debug.print("Failed to create file. File already exists.");
//               }
//           }
//
//           FileReader reader = new FileReader(file);
//           BufferedReader bufferedReader = new BufferedReader(reader);
//           String line;
//           while ((line = bufferedReader.readLine()) != null) {
//               String[] parts = line.split("=");
//               data.put(parts[0], parts[1]);
//           }
//
//           reader.close();
//       }catch (IOException exception){
//
//       }
//    }
}
