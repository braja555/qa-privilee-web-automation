package pages;

import com.github.javafaker.Faker;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import utils.CommonFunctions;
import utils.JsonFileReader;

import static listeners.ExtentReportListener.logStep;

public class SignUpPage extends CommonFunctions {

    private static final Logger log = LoggerFactory.getLogger(SignUpPage.class);

    private final String txtSignUpHeader = "//h3[text() = 'Explore Privilee venues']";
    private final String firstName = "input[name='first_name']";
    private final String email = "input[name='email']";
    private final String additionalEmail = "input[name='additionalEmail']";
    private final String phoneNumber = "input[name='mobile']";
    private final String btnView = "//button[@type = 'submit' and text() = 'View' ]";
    private final String closeIconOfSignUpWindow = ".sc-3e858505-2";
    private final String joinPrivileeToday = "//a[text()='Join Privilee today!']";
    private final String membershipTitle = "Select your membership";
    private final String adultTxt = "Adults (18+)";
    private final String defaultAdultCountOne = "1";
    private final String payUpFrontToggleOn = "//span[text()='Pay upfront']//following::div[starts-with(@class, 'sc-14ff4295-0')]";
    private final String adultIncrementBtn = "+";
    private final String childrenBundleToggle = "(//div[starts-with(@class, 'sc-14ff4295-0')])[1]";
    private final String payUpFrontTxt = "Pay upfront";
    private final String payMonthlyTxt = "Pay monthly";
    private final String individualMembershipTxt = "Individual membership";
    private final String coupleMembershipTxt = "Couple membership ";
    private final String payUpFrontPrice = "//span[text()='Pay upfront']//following::span[starts-with(@class, 'sc-c478b9d7-7')]";
    private final String twelveMonthsTxt = "12 months";
    private final String continueBtn = "Continue";
    private final String twelveMonthsInSelectedBar = "//input[@value = '12 months']";
    private final String bestValueInSelectedBar = "(//input[@value = '12 months']//following::span[text()='Best value'])[1]";
    private final String selectedMembershipTitle = "Selected membership";
    private final String membershipPricePerAdult = "//h3[text() = 'Selected membership']//following::span[1]";
    private final String membershipPricePerAdultForTwelveMonths = "//h3[text() = 'Selected membership']//following::small";
    private final String paySubmitBtn = "//button[@type='submit' and starts-with(text(), 'Pay')]";
    private final String cardIframeLayout = "(//iframe[starts-with(@name, '__privateStripeFrame')])[1]";
    private final String cardNumber = "//input[@name='cardnumber']";
    private String childrenAccessTxt = "Children access";
    private String closeIconChildrenAccess = "//button[contains(@class, 'children-modal-close')]";


    public SignUpPage(Page page) {
        super(page);
    }

    public void setJoinPrivileeToday(int numberOfAdults, boolean isChildren, boolean isPayUpFront, String tenure) {
        page.waitForSelector(joinPrivileeToday);
        page.locator(joinPrivileeToday).click();
        page.waitForSelector(payUpFrontToggleOn);
        logStep("Step 1: Loaded initial page with membership options.");
        Assert.assertTrue(isTextVisible(membershipTitle), "Membership title is not visible");
        logStep("Step 2: Membership title is visible.");

        Assert.assertTrue(isTextVisible(adultTxt), "Adult text is not visible");
        logStep("Step 3: Adult text is visible.");

        Assert.assertTrue(isTextVisible(defaultAdultCountOne), "Default adult count is not visible");
        logStep("Step 4: Default adult count is visible.");

        clickIfNotVisible(payUpFrontToggleOn);
        logStep("Step 5: Toggled payment options.");

        Assert.assertTrue(isTextVisible(payUpFrontTxt), "Pay upfront text is not visible");
        logStep("Step 6: Pay upfront text is visible.");

        Assert.assertTrue(isTextVisible(payMonthlyTxt), "Pay monthly text is not visible");
        logStep("Step 7: Pay monthly text is visible.");

        Assert.assertTrue(isTextVisible(individualMembershipTxt), "Individual membership text is not visible");
        logStep("Step 8: Individual membership text is visible.");

        if (numberOfAdults > 1) {
            clickExactText(adultIncrementBtn);
            logStep("Step 9: Incremented the adult count.");

            Assert.assertTrue(isTextVisible(coupleMembershipTxt), "Couple membership text is not visible");
            logStep("Step 10: Couple membership text is visible.");
        }

        if (isChildren) {
            page.locator(childrenBundleToggle).click();
            logStep("Step 11: Toggled children bundle.");
        }

        String membershipPrice;
        if (isPayUpFront) {
            if (!isElementVisible(payUpFrontToggleOn)) {
                page.locator(payUpFrontToggleOn).click();
            }
            membershipPrice = getText(payUpFrontPrice);
            Assert.assertEquals(normalizeSpaces(membershipPrice), "AED 549/month");
            logStep("Step 12: Set membership price for pay upfront: AED 549/month.");
        } else {
            page.locator(payUpFrontToggleOn).click();
            membershipPrice = getText(payUpFrontPrice);
            Assert.assertEquals(normalizeSpaces(membershipPrice), "AED 649/month");
            logStep("Step 13: Set membership price for monthly payment: AED 649/month.");
        }

        if (tenure.equalsIgnoreCase("12 months") && isTextVisible(twelveMonthsTxt)) {
            clickExactText(twelveMonthsTxt);
            Assert.assertEquals(getAttributeValue(twelveMonthsInSelectedBar, "value"), "12 months");
            Assert.assertEquals(getText(bestValueInSelectedBar), "Best value");
            logStep("Step 14: Selected 12 months tenure with best value.");
        }

        clickExactText(continueBtn);
        logStep("Step 15: Clicked continue button.");

        if (isTextVisible(childrenAccessTxt)) {
            page.locator(closeIconChildrenAccess).click();
            clickExactText(continueBtn);
            logStep("Step 16: Closed children access prompt and continued.");
        }

        page.waitForLoadState();
        Assert.assertTrue(isTextVisible(selectedMembershipTitle), "Selected membership title is not visible");
        logStep("Step 17: Selected membership title is visible.");

        String membershipPriceTxt = getText(membershipPricePerAdult);
        Assert.assertEquals(membershipPriceTxt, membershipPrice + " per adult", "The membership price doesn't match");
        logStep("Step 18: Verified membership price per adult.");

        double numericValue = extractNumericValue(membershipPriceTxt);
        logStep("Step 19: Extracted numeric value from membership price: " + numericValue);

        double yearlyAmount = numericValue * (isPayUpFront ? 12 : 1) * (numberOfAdults > 1 ? 2 : 1);
        String formattedYearlyAmount = String.format("%,.0f", yearlyAmount);
        logStep("Step 20: Calculated yearly amount: " + formattedYearlyAmount);

        String expectedValue = getText(membershipPricePerAdultForTwelveMonths);
        String actualValue = (isPayUpFront ? "One payment of AED " : "12 payments of AED ") + formattedYearlyAmount;
        Assert.assertEquals(normalizeSpaces(actualValue), normalizeSpaces(expectedValue));
        logStep("Step 21: Validated the yearly amount.");

        page.locator(email).fill(generateRandomEmail());
        if (numberOfAdults > 1) {
            page.locator(additionalEmail).fill(generateRandomEmail());
        }
        logStep("Step 22: Filled email details.");

        FrameLocator iframe = page.frameLocator(cardIframeLayout);
        iframe.locator(cardNumber).click();
        iframe.locator(cardNumber).fill(JsonFileReader.readValueFromJson("cardNumber"));
        iframe.getByPlaceholder("MM / YY").click();
        iframe.getByPlaceholder("MM / YY").fill(JsonFileReader.readValueFromJson("mm/yy"));
        iframe.getByPlaceholder("CVC").click();
        iframe.getByPlaceholder("CVC").fill(JsonFileReader.readValueFromJson("cvc"));
        logStep("Step 23: Filled card details.");

        String payPriceInSubmit = getText(paySubmitBtn);
        String expectedValueForSubmit = normalizeSpaces("Pay AED " + formattedYearlyAmount);
        Assert.assertEquals(normalizeSpaces(payPriceInSubmit), expectedValueForSubmit);
        logStep("Step 24: Validated payment button text.");
    }

    public void enterNewUserDetailsAndCloseWindow() {
        try {
            page.locator(firstName).fill(generateRandomFirstName());
            page.locator(email).fill(generateRandomEmail());
            page.locator(phoneNumber).fill(generateRandomPhoneNumber());
            page.locator(btnView).click();
        } catch (Exception e) {
            log.error("Failed to select the venue from the list: {}", e.getMessage(), e);
        }
    }

    public void closeSignUpWindow() {
        try {
            logStep("Attempting to close sign-up window");
            Locator signUpHeader = page.locator(txtSignUpHeader);
            if (signUpHeader.isVisible()) {
                page.locator(closeIconOfSignUpWindow).click();
                logStep("Sign-up window closed successfully");
            } else {
                logStep("Sign-up window is not present");
            }
        } catch (Exception e) {
            log.error("Sign-up window is not present or could not be closed: {}", e.getMessage(), e);
        }
    }

    private String generateRandomFirstName() {
        return new Faker().name().firstName();
    }

    private String generateRandomEmail() {
        return new Faker().letterify("random_????") + "@example.com";
    }

    private String generateRandomPhoneNumber() {
        return "0" + new Faker().number().digits(9);
    }
}
