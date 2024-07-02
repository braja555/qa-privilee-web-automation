package utils;

import factory.PlaywrightFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class JsonFileReader {

    private static final String JSON_FILE_PATH = ConfigLoader.getProperty("jsonTestDataFilePath");

    /**
     * Reads JSON data from a file and returns it as a JSONObject.
     * @return The JSON data as a JSONObject.
     */
    public static JSONObject readJsonFromFile() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads a specific value from a JSON file by key.
     * @param key The key whose value needs to be read.
     * @return The value associated with the key.
     */
    public static String readValueFromJson(String key) {
        JSONObject jsonObject = readJsonFromFile();
        return jsonObject != null ? (String) jsonObject.get(key) : null;
    }

}
