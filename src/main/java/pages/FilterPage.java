package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import constants.AppConstants;
import utils.JsonFileReader;

import java.util.Arrays;
import java.util.List;

public class FilterPage {

    private String filterButton = "Filters";
    private String  showVenueButton = "//button[starts-with(text(), 'Show ')]";
    private String filter = "Filters";
    private String clearFilter = "Clear filters";
    private Page page;
    public FilterPage(Page page) {
        this.page = page;
    }
    public void selectFilterCategories(List<String> venueFilterData ) throws InterruptedException {
        clickButtonByName(filterButton);
        clickButtonByName(AppConstants.RAS_LOC);
        clickCategories(venueFilterData);
        clickShowVenueButton();
    }
    public List<String> getVenueFilterData() {
        return Arrays.asList(
                JsonFileReader.readValueFromJson("familyAccessForKids"),
                JsonFileReader.readValueFromJson("guestAccess"),
                JsonFileReader.readValueFromJson("familyAccessForAdult"),
                JsonFileReader.readValueFromJson("venueType")
        );
    }
    public void clickButtonByName(String buttonName) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonName).setExact(true)).click();
    }
    private void clickShowVenueButton() {
        page.locator(showVenueButton).click();
    }
    private void clickCategories(List<String> categories) {
        for (String category : categories) {
            clickButtonByName(category.trim());
        }
    }
    public void clearFilter() {
        clickButtonByName(filter);
        clickButtonByName(clearFilter);
    }
    public String getVenueName(String itemName) {
        return page.locator("(//h3[text()='" + itemName + "'])[1]").textContent().trim();
    }
}
