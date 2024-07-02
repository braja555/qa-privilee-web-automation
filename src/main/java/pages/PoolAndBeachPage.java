package pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonFileReader;

import java.util.*;
import java.util.stream.Collectors;

import static listeners.ExtentReportListener.logStep;

public class PoolAndBeachPage {

    private static final Logger log = LoggerFactory.getLogger(PoolAndBeachPage.class);
    private String tabMenus = "//button[starts-with(@class, 'sc-210e007d-2')]";
    private String txtNumberOfVenue = "//h3[starts-with(@class, 'sc-428108d3-1')]";
    private String nameOfVenus = "//h3[starts-with(@class, 'sc-58387120-4')]";
    private Page page;
    Map<String , String> map = new HashMap<>();

    public PoolAndBeachPage(Page page) {
        this.page = page;
    }

    public Map<String, String> getTotalNumberOfVenues() {
        try {
            logStep("Validating total number of venues");
            Locator tabMenu = page.locator(tabMenus);
            List<ElementHandle> tabMenuElements = tabMenu.elementHandles();
            String[] keys = new String[]{JsonFileReader.readValueFromJson("menus")};
            for (int i = 0; i < tabMenuElements.size() && i < keys.length; i++) {
                tabMenuElements.get(i).click();
                String venue = page.locator(txtNumberOfVenue).textContent();
                map.put(keys[i], venue);
            }
        } catch (Exception e) {
            log.error("Failed to validate the total number of venues: {}", e.getMessage(), e);
        }
        return map;
    }
    public List<String> getVenueNames(String venue) {
        List<String> venueNames = new ArrayList<>();
        Locator tabMenu = page.locator("//button[text() = '"+venue+"']");
        tabMenu.click();
        try {
            logStep("Validating names of venues");
            page.waitForSelector(nameOfVenus);
            Locator venueLocator = page.locator(nameOfVenus);
            List<ElementHandle> venueElements = venueLocator.elementHandles();
            venueNames = venueElements.stream()
                    .map(ElementHandle::textContent)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to validate the total number of venues: {}", e.getMessage(), e);
        }
        return venueNames;
    }
}
