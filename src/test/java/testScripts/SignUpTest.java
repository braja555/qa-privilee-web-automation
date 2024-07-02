package testScripts;

import base.BaseTest;
import org.testng.annotations.Test;

import static listeners.ExtentReportListener.logStep;

public class SignUpTest extends BaseTest {

    @Test(description = "Validate signup and membership payment process for one adult user. " +
            "This test verifies the functionality of signing up for membership and processing payment for a single adult user.")
    public void validateSignupAndMembershipPaymentForOneAdult() {
        logStep("Test Scenario TS_01_SignUp: Validate signup and membership payment process for one adult user.");
        signUpPage.setJoinPrivileeToday(1, false, true, "12 months");
    }

    @Test(description = "Validate signup and membership payment process for two adult users. " +
            "This test verifies the functionality of signing up for membership and processing payment for two adult users.")
    public void validateSignupAndMembershipPaymentForTwoAdult() {
        logStep("Test Scenario TS_02_SignUp: Validate signup and membership payment process for two adult users");
        signUpPage.setJoinPrivileeToday(2, false, true, "12 months");
    }

    @Test(description = "Validate signup and membership payment process for two adults and children. " +
            "This test verifies the functionality of signing up for membership and processing payment for two adults along with children.")
    public void validateSignupAndMembershipPaymentForTwoAdultAndChildren() {
        logStep("Test Scenario TS_03_SignUp: Validate signup and membership payment process for two adults and children.");
        signUpPage.setJoinPrivileeToday(2, true, true, "12 months");
    }

    @Test(description = "Validate signup and membership payment process for two adults with monthly payment option. " +
            "This test verifies the functionality of signing up for membership and processing monthly payment for two adults.")
    public void validateSignupAndMembershipPaymentForTwoAdultAndPayMonthly() {
        logStep("Test Scenario TS_04_SignUp: Validate signup and membership payment process for two adults with monthly payment option");
        signUpPage.setJoinPrivileeToday(2, true, false, "12 months");
    }
}
