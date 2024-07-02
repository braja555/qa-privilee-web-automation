package base;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.testng.annotations.*;
import pages.FilterPage;
import pages.PoolAndBeachPage;
import pages.SignUpPage;
import pages.VenuePage;
import utils.CommonFunctions;
import utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    PlaywrightFactory playwrightFactory;
    protected Page page;
    protected PoolAndBeachPage poolAndBeachPage;
    protected VenuePage venuePage;
    protected SignUpPage signUpPage;
    protected ServiceIntercept serviceIntercept;
    protected CommonFunctions commonFunctions;
    protected FilterPage filterPage;
    public static String baseURL;

    public static Map<String, String> map;



    @BeforeSuite
    public void flushScreenshotDirectory() {
        PlaywrightFactory.flushScreenshotDirectory();
    }

    @Parameters({"browser", "env"})
    @BeforeMethod
    public void setup(String browserName, String envName) {
        playwrightFactory = new PlaywrightFactory();

        baseURL = Dotenv.configure()
                .filename(".env." + ConfigLoader.getProperty("env"))
                .load().get("URL");

        page = playwrightFactory.initBrowser(baseURL);
        poolAndBeachPage = new PoolAndBeachPage(page);
        commonFunctions = new CommonFunctions(page);
        venuePage = new VenuePage(page);
        signUpPage = new SignUpPage(page);
        filterPage = new FilterPage(page);
        map = new HashMap<>();
        serviceIntercept = new ServiceIntercept();
    }

    @AfterMethod
    public void cleanup() {
        if (page != null) {
            page.close();
        }
    }

    @AfterSuite
    public void tearDown() {
        if (page.context().browser() != null) {
            page.context().browser().close();
        }
        playwrightFactory.tearDownPlaywright();
    }
}