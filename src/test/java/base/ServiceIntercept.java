package base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceIntercept {
    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    APIResponse apiResponse;

    public JsonNode getResponse(String endPoint) {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();
        BaseTest.baseURL = "https://staging-website.privilee.ae";
        apiResponse = requestContext.get(BaseTest.baseURL + endPoint);
        System.out.println(BaseTest.baseURL + endPoint);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResBody = null;
        try {
            jsonResBody = objectMapper.readTree(apiResponse.body());
            //System.out.println(jsonResBody.toPrettyString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsonResBody;
    }

    public int getItemCount(String endPoint) {
        JsonNode response = getResponse(endPoint);
        return response.get("items").size();
    }

    public List<String> getItemList(String endPoint) {
        JsonNode response = getResponse(endPoint);
        JsonNode itemsNode = response.get("items");
        List<String> itemList = new ArrayList<>(itemsNode.size());
        for (JsonNode item : itemsNode) {
            itemList.add(item.get("name").asText());
        }
        Collections.sort(itemList);
        return itemList;
    }
    public List<String> getCategories(String endPoint, String itemName) {
        JsonNode response = getResponse(endPoint);
        JsonNode itemsNode = response.get("items");
        List<String> categoryList = new ArrayList<>();
        for (JsonNode item : itemsNode) {
            if (item.get("name").asText().equalsIgnoreCase(itemName)) {
                JsonNode categoryNode = item.get("categories");
                for (JsonNode category : categoryNode) {
                    categoryList.add(category.get("category").asText());
                }
                break;
            }
        }
        return categoryList.isEmpty() ? Collections.emptyList() : categoryList;
    }
    public List<String> filterHotels(String endPoint, String city, List<String> catList) {
        JsonNode hotelResponse = getResponse(endPoint);
        List<String> filteredHotelNames = new ArrayList<>();
        ArrayNode items = (ArrayNode) hotelResponse.get("items");
        for (JsonNode item : items) {
            String hotelCity = item.get("city").asText();
            if (!city.equalsIgnoreCase(hotelCity)) {
                continue;
            }

            JsonNode categoriesNode = item.get("categories");
            List<String> hotelCategories = new ArrayList<>();
            for (JsonNode categoryNode : categoriesNode) {
                hotelCategories.add(categoryNode.get("category").asText());
            }
            if (hotelCategories.containsAll(catList)) {
                String hotelName = item.get("name").asText();
                filteredHotelNames.add(hotelName);
            }
        }

        System.out.println("Number of filtered hotels: " + filteredHotelNames.size());
        return filteredHotelNames;
    }

}
