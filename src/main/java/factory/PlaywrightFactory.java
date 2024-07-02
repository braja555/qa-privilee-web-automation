package factory;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ViewportSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;

public class PlaywrightFactory {

    private static final Logger log = LoggerFactory.getLogger(PlaywrightFactory.class);

    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }
    public static Browser getBrowser() {
        return tlBrowser.get();
    }
    public static BrowserContext getBrowserContext() {
        return tlBrowserContext.get();
    }
    public static Page getPage() {
        return tlPage.get();
    }
    public Page initBrowser(String baseURL) {
        initializePlaywright();
        String browserName = ConfigLoader.getProperty("browser").trim();
        boolean isHeadless = Boolean.parseBoolean(ConfigLoader.getProperty("headless").trim());
        log.info("Initializing browser: {}", browserName);

        try {
            setupBrowser(browserName, isHeadless);
            setupBrowserContext();
            navigateToUrl(baseURL, "/map");
            log.info("Browser initialized and navigated to URL: {}{}", baseURL, "/map");
        } catch (Exception e) {
            log.error("Failed to initialize browser: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return getPage();
    }

    private void initializePlaywright() {
        tlPlaywright.set(Playwright.create());
    }

    private void setupBrowser(String browserName, boolean isHeadless) {
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(isHeadless);
        switch (browserName.toLowerCase()) {
            case "chromium":
                tlBrowser.set(getPlaywright().chromium().launch(launchOptions));
                break;
            case "firefox":
                tlBrowser.set(getPlaywright().firefox().launch(launchOptions));
                break;
            case "safari":
                tlBrowser.set(getPlaywright().webkit().launch(launchOptions));
                break;
            case "chrome":
                tlBrowser.set(getPlaywright().chromium().launch(launchOptions.setChannel("chrome")));
                break;
            case "edge":
                tlBrowser.set(getPlaywright().chromium().launch(launchOptions.setChannel("msedge")));
                break;
            default:
                log.error("Unsupported browser: {}", browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }

    private void setupBrowserContext() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        tlBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions()
                .setViewportSize(new ViewportSize(width, height))
                .setPermissions(java.util.Arrays.asList("geolocation"))));

        tlPage.set(getBrowserContext().newPage());
    }
    private void navigateToUrl(String url, String endPoint) {
        getPage().navigate(url + endPoint);
    }

    public static String takeScreenshot(String testName) {
        String timestamp = LocalDateTime.now().toString().replace(":", "-");
        String path = System.getProperty("user.dir") + "/result-artifacts/screenshots/" + testName + "_" + timestamp + ".png";
        boolean isFullScreenshot = Boolean.parseBoolean(ConfigLoader.getProperty("fullscreenshot").trim());
        try {
            byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(isFullScreenshot));
            String base64Path = Base64.getEncoder().encodeToString(buffer);
            log.info("Screenshot taken: {}", path);
            return base64Path;
        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    public static void flushScreenshotDirectory() {
        String screenshotDir = System.getProperty("user.dir") + "/result-artifacts";
        File directory = new File(screenshotDir);
        if (directory.exists()) {
            try {
                Files.walk(directory.toPath())
                        .map(Path::toFile)
                        .forEach(File::delete);
                log.info("Screenshot directory flushed: {}", screenshotDir);
            } catch (IOException e) {
                log.error("Failed to flush screenshot directory: {}", e.getMessage(), e);
            }
        } else {
            directory.mkdirs();
            log.info("Screenshot directory created: {}", screenshotDir);
        }
    }
    public void tearDownPlaywright(){
        if (tlPlaywright.get() != null) {
            tlPlaywright.get().close();
            tlPlaywright.remove();
        }
    }
}
