package utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.net.URL;

public class CommonFunctions {

    protected final Page page;

    public CommonFunctions(Page page) {
        this.page = page;
    }
    public boolean isImageValid(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            System.out.println("Exception while checking image: " + imageUrl);
            e.printStackTrace();
            return false;
        }
    }

    public void clickLinkByName(String name) {
        page.getByRole(AriaRole.valueOf("link"), new Page.GetByRoleOptions().setName(name)).click();
    }

    public void clickHeadingByName(String name) {
        page.getByRole(AriaRole.valueOf("heading"), new Page.GetByRoleOptions().setName(name)).click();
    }

    public void clickText(String text) {
        page.getByText(text).click();
    }

    public void clickExactText(String text) {
        page.getByText(text, new Page.GetByTextOptions().setExact(true)).click();
    }

    public void clickButtonByName(String name) {
        page.getByRole(AriaRole.valueOf("button"), new Page.GetByRoleOptions().setName(name)).click();
    }

    public void clickListByText(String text) {
        page.getByRole(AriaRole.valueOf("list")).getByText(text).click();
    }

    public void fillPlaceholder(String placeholder, String text) {
        page.getByPlaceholder(placeholder).click();
        page.getByPlaceholder(placeholder).fill(text);
    }

    public String getText(String selector) {
        return page.locator(selector).innerText();
    }


    public String getTextByPlaceholder(String placeholder) {
        return page.getByPlaceholder(placeholder).innerText();
    }

    public boolean isTextVisible(String text) {
        return page.isVisible("text=" + text);
    }

    public boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public boolean clickIfNotVisible(String selector) {
        Locator element = page.locator(selector);
        if (!element.isVisible()) {
            page.locator("//div[@class='sc-14ff4295-0 gjpaxc']").click();
        }
        return element.isVisible();
    }

    public void assertTextVisibleAndClick(String text) {
        boolean isVisible = page.isVisible("text=" + text);
        Assert.assertTrue(isVisible, "Text '" + text + "' is not visible on the page.");
        page.click("text=" + text);
    }

    public boolean isElementEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }
    public boolean isElementHidden(String selector) {
        return page.locator(selector).isHidden();
    }

    public void toggleCheckbox(String selector, boolean check) {
        Locator checkbox = page.locator(selector);
        if (checkbox.isChecked() != check) {
            checkbox.click();
        }
    }

    public void toggleSwitchByRole(String role, String name, boolean check) {
        Locator toggleSwitch = page.getByRole(AriaRole.valueOf(role), new Page.GetByRoleOptions().setName(name));
        if (toggleSwitch.isChecked() != check) {
            toggleSwitch.click();
        }
    }

    public String getAttributeValue(String selector, String attributeName) {
        Locator element = page.locator(selector).first();
        Assert.assertNotNull(element, "Element with selector '" + selector + "' not found.");
        Assert.assertTrue(element.isVisible(), "Element with selector '" + selector + "' is not visible.");
        return element.getAttribute(attributeName);
    }
    public double extractNumericValue(String text) {
        // Remove non-numeric characters and parse the number
        String numericValue = text.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numericValue);
    }
    public static String normalizeSpaces(String text) {
        // Replace all kinds of spaces (regular and non-breaking) with a single space
        return text.replaceAll("[\\s\\u00A0]+", " ").trim();
    }
    public void fillField(String selector, String value) {
        page.locator(selector).fill(value);
    }

    public FrameLocator navigateToIframeByName(String iframeLocator) {
        FrameLocator iframe = page.frameLocator(iframeLocator);
        Assert.assertNotNull(iframe, "Iframe with name '" + iframeLocator + "' not found.");
        return iframe;
    }
}
