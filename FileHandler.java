// A class that handles saving game options to a file and loading them from a file

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class FileHandler {
        
        // Saves the game options to a file
        public static void saveGameOptions(HashMap<String, String> options) {
            try {
                PrintWriter writer = new PrintWriter("gameOptions.txt", "UTF-8");
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
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("gameOptions.txt"));
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("=");
                    options.put(split[0], split[1]);
                }
                reader.close();
                return options;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}