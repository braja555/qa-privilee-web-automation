package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static listeners.ExtentReportListener.logStep;

public class VenuePage {

    private static final Logger log = LoggerFactory.getLogger(VenuePage.class);
    private String searchInputFieldMap = "Search for venue";
    private String closeIconOfSignUpWindow = ".sc-3e858505-2";
    private String closeIconOfVenueDetailWindow = "(//button[@title='Close modal window'])[2]";
    private String mapPopUpVenueName = "//div[@class='mapboxgl-popup-content']//h3";
    private String joinPrivileeButton = "";
    private Page page;

    public VenuePage(Page page) {
        this.page = page;
    }
    public void selectVenueFromListInMapSearchField(String venueName) {
        try {
            logStep("Attempting to select venue from list in map search field: {}", venueName);
            Locator searchInput = page.getByPlaceholder(searchInputFieldMap);
            searchInput.click();
            Locator venueList = page.getByRole(AriaRole.LIST);
            Locator venueOption = venueList.locator("text=" + venueName);
            venueOption.click();
            logStep("Successfully selected venue: {}", venueName);
        } catch (Exception e) {
            log.error("Failed to select the venue from the list: {}", e.getMessage(), e);
        }
    }
    public void enterAndSelectVenueInMapSearchField(String venueName) {
        try {
            logStep("Attempting to select venue from list in map search field: {}", venueName);

            Locator searchInput = page.getByPlaceholder(searchInputFieldMap);
            searchInput.fill(venueName);
            searchInput.press("Enter");
            logStep("Successfully selected venue: {}", venueName);
        } catch (Exception e) {
            log.error("Failed to select the venue from the list: {}", e.getMessage(), e);
        }
    }
    public void closeVenueDetailWindow() {
        try {
            logStep("Attempting to close venue detail window");
            Locator closeIcon = page.locator(closeIconOfVenueDetailWindow);
            if (closeIcon.isVisible()) {
                closeIcon.click();
                logStep("Venue detail window closed successfully");
            } else {
                logStep("Venue detail window is not present");
            }
        } catch (Exception e) {
            log.error("Venue detail window is not present or could not be closed: {}", e.getMessage(), e);
        }
    }
    public String getTitleInVenueDetailWindow(String venueTitle) {
        try {
            logStep("Attempting to get title in venue detail window for venue: {}", venueTitle);
            String venueTitleXPath = buildVenueTitleXPath(venueTitle);
            Locator titleLocator = page.locator(venueTitleXPath);
            String title = titleLocator.textContent();

            logStep("Successfully retrieved title: {}", title);
            return title;
        } catch (Exception e) {
            log.error("Failed to get the title in the venue detail window: {}", e.getMessage(), e);
            return null;
        }
    }
    public String getMapPopVenueName() {
        try {
            logStep("Attempting to get venue name in map pop up");

            Locator mapPopUpVenueElement = page.locator(mapPopUpVenueName);
            String mapPopUpVenue = mapPopUpVenueElement.textContent();

            logStep("Successfully retrieved title: {}", mapPopUpVenue);
            return mapPopUpVenue;
        } catch (Exception e) {
            log.error("Failed to get the title in the venue detail window: {}", e.getMessage(), e);
            return null;
        }
    }
    public void clickMapPopUpVenueName(){
        page.locator(mapPopUpVenueName).click();
    }
    public String getVenueName(String venue) {
        return page.locator("(//h3[text()='"+venue+"'])[1]").textContent();
    }
    private String buildVenueTitleXPath(String venueTitle) {
        return String.format("//h2[text() = '%s']", venueTitle);
    }
}
