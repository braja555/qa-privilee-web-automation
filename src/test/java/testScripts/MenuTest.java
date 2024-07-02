package testScripts;

import base.BaseTest;
import com.microsoft.playwright.ElementHandle;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ApiEndpoints;

import java.util.ArrayList;
import java.util.List;

import static listeners.ExtentReportListener.logStep;

public class MenuTest extends BaseTest {

    @Test(description = "Validate that each menu on the application correctly displays the names of all venues associated with it by comparing the displayed venue names with the names retrieved from the network API. " +
            "This test is important because it ensures that the application accurately reflects the data from the backend, providing users with reliable and consistent information.")
    public void validateAllVenueNameForEachMenus() {
        logStep("Test Scenario: TS_01_Menu: Validate that each menu on the application correctly displays the names of all venues associated with it");
        List<String> nameOfPoolAndBeachVenueFromNetwork = serviceIntercept.getItemList(ApiEndpoints.getHotelEndpoint());
        List<String> actualNameOfVenue = poolAndBeachPage.getVenueNames(AppConstants.HOTEL_VENUE);
        List<String> nameOfFitnessVenueFromNetwork = serviceIntercept.getItemList(ApiEndpoints.getFitnessEndpoint());
        List<String> actualNameOfFitnessVenue = poolAndBeachPage.getVenueNames(AppConstants.FITNESS_VENUE);
        List<String> nameOfFamilyVenueFromNetwork = serviceIntercept.getItemList(ApiEndpoints.getFamilyEndpoint());
        List<String> actualNameOfFamilyVenue = poolAndBeachPage.getVenueNames(AppConstants.FAMILY_VENUE);
        List<String> nameOfRestaurantVenueFromNetwork = serviceIntercept.getItemList(ApiEndpoints.getRestaurantEndpoint());
        List<String> actualNameOfRestaurantVenue = poolAndBeachPage.getVenueNames(AppConstants.DINNING_VENUE);
        logStep("Retrieved number of venues and name for Pool & Beach from UI and Network API response");
        Assert.assertEquals(actualNameOfVenue.size(), nameOfPoolAndBeachVenueFromNetwork.size(), "The lists size of venues for Pool & Beach does not match expected value.");
        Assert.assertEquals(actualNameOfVenue, nameOfPoolAndBeachVenueFromNetwork, "The lists names of venues for Pool & Beach does not match expected value.");
        logStep("Retrieved number of venues and name for Fitness from UI and Network API response");
        Assert.assertEquals(actualNameOfFitnessVenue.size(), nameOfFitnessVenueFromNetwork.size(), "The lists size of venues for Fitness does not match expected value.");
        Assert.assertEquals(actualNameOfFitnessVenue, nameOfFitnessVenueFromNetwork, "The lists names of venues for Fitness does not match expected value.");
        logStep("Retrieved number of venues and name for Family from UI and Network API response");
        Assert.assertEquals(actualNameOfFamilyVenue.size(), nameOfFamilyVenueFromNetwork.size(), "The lists size of venues for Family activities does not match expected value.");
        Assert.assertEquals(actualNameOfFamilyVenue, nameOfFamilyVenueFromNetwork, "The lists names of venues for Family activities does not match expected value.");
        logStep("Retrieved number of venues and name for Dining from UI and Network API response");
        Assert.assertEquals(actualNameOfRestaurantVenue.size(), nameOfRestaurantVenueFromNetwork.size(), "The lists size of venues for Dinning does not match expected value.");
        Assert.assertEquals(actualNameOfRestaurantVenue, nameOfRestaurantVenueFromNetwork, "The lists names of venues for Dinning does not match expected value.");
    }

    @Test(description = "Validate the correct functionality of menus and the accurate count of venues by comparing the number of venues displayed in the UI with the counts retrieved from the network API. " +
            "This test is important because it ensures the menus reflect accurate venue counts, providing users with reliable information and enhancing the overall user experience.")
    public void validateMenusAndTotalVenus() {
        logStep("Test Scenario: TS_02_Menu: Validate the correct functionality of menus and the accurate count of venues");
        int numOfPoolAndBeachVenues = serviceIntercept.getItemCount(ApiEndpoints.getHotelEndpoint());
        int numOfFitnessVenues = serviceIntercept.getItemCount(ApiEndpoints.getFitnessEndpoint());
        int numOfFamilyVenues = serviceIntercept.getItemCount(ApiEndpoints.getFamilyEndpoint());
        int numOfDiningVenues = serviceIntercept.getItemCount(ApiEndpoints.getRestaurantEndpoint());
        String expectedVenuesMessageForPoolAndBeach = numOfPoolAndBeachVenues + " pool & beach venues";
        String expectedVenuesMessageForFitness = numOfFitnessVenues + " fitness venues";
        String expectedVenuesMessageForFamily = numOfFamilyVenues + " family activities";
        String expectedVenuesMessageForDining = numOfDiningVenues + " dining discounts";
        SoftAssert softAssert = new SoftAssert();
        map = poolAndBeachPage.getTotalNumberOfVenues();
        logStep("Retrieved number of venues for Pool & Beach from UI and Network API response");
        softAssert.assertEquals(map.get("pool"), expectedVenuesMessageForPoolAndBeach, "Number of venues for Pool & Beach does not match expected value.");
        logStep("Retrieved number of venues for Fitness from UI and Network API response");
        softAssert.assertEquals(map.get("fitness"), expectedVenuesMessageForFitness, "Number of venues for Fitness does not match expected value.");
        logStep("Retrieved number of venues for Family from UI and Network API response");
        softAssert.assertEquals(map.get("family"), expectedVenuesMessageForFamily, "Number of venues for Family does not match expected value.");
        logStep("Retrieved number of venues for Dining from UI and Network API response");
        softAssert.assertEquals(map.get("dining"), expectedVenuesMessageForDining, "Number of venues for Dining does not match expected value.");
    }

    @Test(description = "Validate the pool and beach page for broken images by checking each image element's source attribute. " +
            "This test is important because it ensures that all images on the page are correctly loaded, providing users with a seamless visual experience without broken or missing images.")
    public void validateBrokenImages() {
        logStep("Test Scenario: TS_03_Menu: Validate the pool and beach page of all images");
        List<ElementHandle> images = page.querySelectorAll("img");
        List<String> brokenImages = new ArrayList<>();

        for (ElementHandle image : images) {
            String src = image.getAttribute("src");
            if (src != null && !src.isEmpty()) {
                if (!commonFunctions.isImageValid(src)) {
                    brokenImages.add(src);
                }
            }
        }
        Assert.assertTrue(brokenImages.isEmpty(), "Broken images found: " + brokenImages);
    }
}
