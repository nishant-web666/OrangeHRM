package com.orangehrm.base;

import com.orangehrm.actionclass.ActionClass;
import com.orangehrm.utilities.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.Logger;

public class BaseClass {
    protected static Properties props;
    protected static WebDriver driver;
    private static ActionClass actionClass;
    public static final Logger logger = LogsManager.getLogger(BaseClass.class);
    //Driver getter method to call it outside from the package
    public static WebDriver getDriver(){
        if (driver == null){
            logger.error("WebDriver is not initialized.");
            throw new IllegalStateException("WebDriver is not initialized.");
        }
        return driver;
    }
    // Driver setter method to set the driver outside from the current package
    public void setDriver(WebDriver driver){
        this.driver = driver;
    }
    //ActionClass getter method to call it outside from the package
    public static ActionClass getActionClass(){
        if (actionClass == null){
            logger.error("WebDriver is not initialized.");
            throw new IllegalStateException("WebDriver is not initialized.");
        }
        return actionClass;
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        props = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        props.load(fis);
        logger.info("Properties files are initialized");
    }
    //Props getter method to call it outside from the package
    public static Properties getProps() {
        return props;
    }
    // Launch browser according to the defined
    private void launchBrowser(){
        // Fetching the browser property from config file
        String browser = props.getProperty("browser");
        // Setting browser according to the config file
        if (browser.equalsIgnoreCase("chrome")){
            driver = new ChromeDriver();
            logger.info("ChromeDriver instance is created.");
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
            logger.info("Firefox instance is created.");
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
            logger.info("Edge instance is created.");
        } else if (browser.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
            logger.info("Safari instance is created.");
        }else{
            throw new IllegalArgumentException("Driver not supported" + driver);
        }
    }
    //Configure browser settings such as implicit wait, maximize the window and navigate to the url
    private void configureBrowser(){
        // Implicit wait
        int implicitWait = Integer.parseInt(props.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        //Maximize the window
        driver.manage().window().maximize();
        //Navigate to url
        try {
            driver.get(props.getProperty("url"));
        }catch (Exception e){
            System.out.println("Failed to navigate to URL" + e.getMessage());
        }
    }

    @BeforeMethod
    public void setup() throws IOException{
        System.out.println("Setting up the WebDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
        logger.info("Webdriver Initialized! and Browser Maximized.");
        // Initialize the action driver class only once
        if (actionClass == null){
            actionClass = new ActionClass(driver);
            logger.info("ActionClass driver instance is created");
            logger.warn("Logger warn message.");
            logger.debug("Logger debug message.");
            logger.fatal("Logger fatal message.");
            logger.error("Logger error message.");
            logger.trace("Logger trace message.");
        }
    }

    @AfterMethod
    public void tearDown(){
        if (driver != null){
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Failed to quit the driver "+ e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed");
        driver = null;
        actionClass = null;
    }
    // Static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
