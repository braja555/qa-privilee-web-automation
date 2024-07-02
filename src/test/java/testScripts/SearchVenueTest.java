package testScripts;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JsonFileReader;

import static listeners.ExtentReportListener.logStep;

public class SearchVenueTest extends BaseTest {

    @Test(description = "Validate venue search functionality for guest users on the application by searching for a specific venue and verifying its details on the venue detail window and map popup. " +
            "This test is important because it ensures that guest users can successfully search for venues and view accurate information, enhancing their experience.")
    public void validateSearchVenueForGuestUser() {
        logStep("Test Scenario TS_01_Search: Validate venue search functionality for guest users on the application.");
        String venueNameExpected = JsonFileReader.readValueFromJson("venueName");
        logStep("Read venue name from JSON file");
        venuePage.selectVenueFromListInMapSearchField(venueNameExpected);
        logStep("Selected venue from list in map search field");
        signUpPage.closeSignUpWindow();
        logStep("Closed sign-up window if it appears");
        String venueNameActual = venuePage.getTitleInVenueDetailWindow(venueNameExpected);
        logStep("Retrieved venue name from venue detail window");
        Assert.assertEquals(venueNameActual, venueNameExpected);
        logStep("Validated venue name: Expected and Actual");
        venuePage.closeVenueDetailWindow();
        logStep("Closed venue detail window");
        String mapPopUpVenueNameActual = venuePage.getMapPopVenueName();
        Assert.assertEquals(mapPopUpVenueNameActual, venueNameExpected);
        String venueNameFromListOfVenuesActual =venuePage.getVenueName(venueNameExpected);
        Assert.assertEquals(venueNameFromListOfVenuesActual, venueNameExpected);
    }

    @Test(description = "Validate venue search functionality by giving user details on the application. " +
            "This test verifies that users can search for a venue, enter their details if prompted, and view accurate venue details.")
    public void validateSearchVenueByGivingUserDetails() {
        logStep("Test Scenario TS_02_Search: Validate venue search functionality by giving user detail on the application.");
        String venueNameExpected = JsonFileReader.readValueFromJson("venueName");
        logStep("Read venue name from JSON file");
        venuePage.enterAndSelectVenueInMapSearchField(venueNameExpected);
        logStep("Selected venue from list in map search field");
        signUpPage.enterNewUserDetailsAndCloseWindow();
        logStep("Enter user details and click on View button to sign up");
        String venueNameActual = venuePage.getTitleInVenueDetailWindow(venueNameExpected);
        logStep("Retrieved venue name from venue detail window");
        Assert.assertEquals(venueNameActual, venueNameExpected, "Title in Venue detail doesn't match");
        logStep("Validated venue name: Expected and Actual");
        venuePage.closeVenueDetailWindow();
        logStep("Closed venue detail window");
        String mapPopUpVenueNameActual = venuePage.getMapPopVenueName();
        Assert.assertEquals(mapPopUpVenueNameActual, venueNameExpected);
        String venueNameFromListOfVenuesActual =venuePage.getVenueName(venueNameExpected);
        Assert.assertEquals(venueNameFromListOfVenuesActual, venueNameExpected);
        venuePage.clickMapPopUpVenueName();
        String venueNameActualInVenueWindow = venuePage.getTitleInVenueDetailWindow(venueNameExpected);
        Assert.assertEquals(venueNameActualInVenueWindow, venueNameExpected, "Title in Venue detail doesn't match");
        venuePage.closeVenueDetailWindow();
    }
}
