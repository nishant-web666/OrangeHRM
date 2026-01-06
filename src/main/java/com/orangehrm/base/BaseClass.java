package com.orangehrm.base;

import com.orangehrm.actionclass.ActionClass;
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

public class BaseClass {
    protected static Properties props;
    protected static WebDriver driver;
    private static ActionClass actionClass;
    //Driver getter method to call it outside from the package
    public static WebDriver getDriver(){
        if (driver == null){
            System.out.println("WebDriver is not initialized.");
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
            System.out.println("WebDriver is not initialized.");
            throw new IllegalStateException("WebDriver is not initialized.");
        }
        return actionClass;
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        props = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        props.load(fis);
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
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } else if (browser.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
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
        // Initialize the action driver class only once
        if (actionClass == null){
            actionClass = new ActionClass(driver);
            System.out.println("ActionClass driver instance is created");
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
        System.out.println("WebDriver instance is closed");
        driver = null;
        actionClass = null;
    }
    // Static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
