package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ApiEndpoints {
    private static JSONObject endpoints;

    static {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/test/resources/api/endpoints.json")) {
            endpoints = (JSONObject) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
   /* static {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = ApiEndpoints.class.getClassLoader().getResourceAsStream("/api/endpoints.json")) {
            if (input == null) {
                System.out.println("Sorry, unable to find endpoints.json");
            }
            endpoints = mapper.readTree(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/

    public static String getMapEndpoint() {
        return (String) endpoints.get("map");
    }

    public static String getHotelEndpoint() {
        return (String) endpoints.get("hotel");
    }

    public static String getFitnessEndpoint() {
        return (String) endpoints.get("fitness");
    }

    public static String getFamilyEndpoint() {
        return (String) endpoints.get("family");
    }

    public static String getRestaurantEndpoint() {
        return (String) endpoints.get("restaurant");
    }

}
