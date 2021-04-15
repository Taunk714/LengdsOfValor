import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;


public class configUtil {
    private static Properties properties;

    static{
        readConfig();
    }

    public static void readConfig(){
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key
     * @param defaultValue If value doesn't exist, then use default value.
     * @return the corespond value
     */
    public static String getConfig(String key, String defaultValue){
        return properties.getProperty(key)== null ? defaultValue: properties.getProperty(key);
    }

    public static String getConfig(String key){
        return properties.getProperty(key);
    }

    /**
     * 
     * @param key
     * @param defaultValue
     * @return integer value of config value
     */
    public static int getConfigInt(String key, int defaultValue){
        String value = properties.getProperty(key);
        if(value == null){
            return defaultValue;
        }
        else{
            return Integer.parseInt(value);
        }
    }

    public static int getConfigInt(String key){
        return Integer.parseInt(properties.getProperty(key));
    }

    public static double getConfigDouble(String key){
        return Double.parseDouble(properties.getProperty(key));
    }
}
