package listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import factory.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import utils.ConfigLoader;

import static factory.PlaywrightFactory.takeScreenshot;

public class ExtentReportListener implements ITestListener {
    private static final String OUTPUT_FOLDER = "./result-artifacts/reports/";
    private static final String FILE_NAME = "TestExecutionReport.html";
    private static final Logger log = LoggerFactory.getLogger(ExtentReportListener.class);

    private static ExtentReports extent = init();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ExtentReports extentReports;

    private static PlaywrightFactory playwrightFactory;

    private static ExtentReports init() {

        Path path = Paths.get(OUTPUT_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                log.info("Created report directory at {}", OUTPUT_FOLDER);
            } catch (IOException e) {
                log.error("Failed to create report directory: {}", e.getMessage(), e);
            }
        }
        playwrightFactory = new PlaywrightFactory();
        extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
        reporter.config().setReportName("Privilee Automation Test Results");
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Author", ConfigLoader.getProperty("author").trim());
        extentReports.setSystemInfo("Team", ConfigLoader.getProperty("team").trim());
        extentReports.setSystemInfo("System", ConfigLoader.getProperty("platformOS").trim());
        extentReports.setSystemInfo("Browser", ConfigLoader.getProperty("browser").trim());
        extentReports.setSystemInfo("Environment", ConfigLoader.getProperty("env").trim());
        extentReports.setSystemInfo("Build#", ConfigLoader.getProperty("build").trim());

        log.info("Initialized Extent Reports with the following details: Author={}, Team={}, System={}, Browser={}, Environment={}, Build#={}",
                ConfigLoader.getProperty("author").trim(),
                ConfigLoader.getProperty("team").trim(),
                ConfigLoader.getProperty("platformOS").trim(),
                ConfigLoader.getProperty("browser").trim(),
                ConfigLoader.getProperty("env").trim(),
                ConfigLoader.getProperty("build").trim());

        log.info("Extent Reports initialization complete.");
        return extentReports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        log.info("Test Suite started: {}", context.getName());
        System.out.println("Test Suite started!");
        logStep("Test Suite started: " + context.getName());
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite is ending!");
        log.info("Test Suite is ending: {}", context.getName());
        extent.flush();
        test.remove();
        logStep("Test Suite is ending: " + context.getName());
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String qualifiedName = result.getMethod().getQualifiedName();
        int last = qualifiedName.lastIndexOf(".");
        int mid = qualifiedName.substring(0, last).lastIndexOf(".");
        String className = qualifiedName.substring(mid + 1, last);

        System.out.println(methodName + " started!");
        log.info("Test started: {} in class: {}", methodName, className);

        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());

        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        extentTest.assignCategory(className);
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));

        logStep("Test started: " + methodName + " in class: " + className);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", result.getMethod().getMethodName());
        test.get().pass("Test passed: " + result.getMethod().getMethodName());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        System.out.println((result.getMethod().getMethodName() + " failed!"));
        log.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        test.get().fail(result.getThrowable(),
                MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(result.getMethod().getMethodName()), result.getMethod().getMethodName()).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));

        logStep("Test failed: " + result.getMethod().getMethodName());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        System.out.println((result.getMethod().getMethodName() + " skipped!"));
        log.warn("Test skipped: {}", result.getMethod().getMethodName());

        test.get().skip(result.getThrowable(),
                MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot(result.getMethod().getMethodName()), result.getMethod().getMethodName()).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));

        logStep("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
        log.warn("Test failed but within success percentage: {}", result.getMethod().getMethodName());

        logStep("Test failed but within success percentage: " + result.getMethod().getMethodName());
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    public static void logStep(String message, String... value) {
        if (test.get() != null) {
            test.get().info(message);
        }
    }
}
