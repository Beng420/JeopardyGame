// A class that handles saving game options to a file and loading them from a file

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class FileHandler {
    public static String filePath = "C:\\Users\\Public\\Documents\\JeopardyGame\\gameOptions.txt";
        
        // Saves the game options to a file
        public static void saveGameOptions(HashMap<String, String> options) {
            try {
                File file = new java.io.File(filePath);
                file.getParentFile().mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                } 
                PrintWriter writer = new PrintWriter(file);
                for (String key : options.keySet()) {
                    writer.println(key + "=" + options.get(key));
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // Loads the game options from a file
        public static HashMap<String, String> loadGameOptions() {
            try {
                HashMap<String, String> options = new HashMap<String, String>();
                String line = "";
                File file = new java.io.File(filePath);
                file.getParentFile().mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                } 
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("=");
                    if(split.length == 2)
                        options.put(split[0], split[1]);
                    else
                        options.put(split[0], "");
                }
                reader.close();
                return options;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}
