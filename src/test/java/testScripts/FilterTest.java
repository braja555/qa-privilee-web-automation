package testScripts;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ApiEndpoints;
import utils.JsonFileReader;

import java.util.List;

import static listeners.ExtentReportListener.logStep;

public class FilterTest extends BaseTest {

    @Test(description = "Validate that the venue page displays correct venues after applying customized category filters, " +
            "and verify the API returned results match the filtered venues on the venue list page. " +
            "This test is important because it ensures that the filtering functionality is working correctly, " +
            "providing users with accurate search results and enhancing their overall experience.")
    public void validateVenueApplyingCustomizedCategoriesFilter() throws InterruptedException {
        logStep("Test Scenario: TS_01_Filter: Validate the venue by applying filters and it's api returned results on venue list page");
        String actualTitle = page.title();
        Assert.assertEquals(actualTitle, AppConstants.VENUE_PAGE_TITLE, "Page Title not matched");
        List<String> venueFilterData = filterPage.getVenueFilterData();
        filterPage.selectFilterCategories(venueFilterData);
        List<String> filteredVenueCategoriesFromNetwork = serviceIntercept.filterHotels(ApiEndpoints.getHotelEndpoint(), AppConstants.RAS_LOC, filterPage.getVenueFilterData());
        for (String item : filteredVenueCategoriesFromNetwork) {
            String venueName = filterPage.getVenueName(item);
            Assert.assertEquals(venueName.trim(), item.trim());
        }
    }

    @Test(description = "Validate that the venue page displays the correct venue when category data is passed to both the endpoint and the frontend (applying the categories through Filters). " +
            "This test is important because it ensures that the category filtering system functions as expected, providing users with accurate and consistent venue information.")
    public void validateCategoriesForGivenVenueInFilter() throws InterruptedException {
        logStep("Test Scenario: TS_02_Filter: Validate the venue by passing the categories data to the endpoint and to the FE (applying the categories through Filters)");
        String venue = JsonFileReader.readValueFromJson("venueName");
        List<String> filteredVenueCategoriesFromNetwork = serviceIntercept.getCategories(ApiEndpoints.getHotelEndpoint(), venue);
        filterPage.selectFilterCategories(filteredVenueCategoriesFromNetwork);
        Assert.assertEquals(venuePage.getVenueName(venue), venue);
    }

    @Test(description = "Validate the clear filter functionality to ensure that applied filters can be reset and the venue list returns to its default state. " +
            "This test is important because it verifies that users can easily remove applied filters, enhancing usability and flexibility in searching for venues.")
    public void validateClearFilters() throws InterruptedException {
        logStep("Test Scenario: TS_03_Filter: Validate the clear filter functionality");
        String venue = JsonFileReader.readValueFromJson("venueName");
        List<String> filteredVenueCategoriesFromNetwork = serviceIntercept.getCategories(ApiEndpoints.getHotelEndpoint(), venue);
        filterPage.selectFilterCategories(filteredVenueCategoriesFromNetwork);
        Assert.assertEquals(venuePage.getVenueName(venue), venue);
        filterPage.clearFilter();
    }
}
