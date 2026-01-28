package com.orangehrm.actionclass;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import com.orangehrm.base.BaseClass;

public class ActionClass {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;
    int explicitWait = Integer.parseInt(BaseClass.getProps().getProperty("explicitWait"));

    public ActionClass(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance is created");
    }
    // Method for waiting the element till it is clickable
    public void waitForElementToBeClickable(By by){
        try{
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Element is not clickable. " + e.getMessage());
        }
    }
    // Method for waiting the element till it is visible
    public void waitForElementToBeVisible(By by){
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible. " + e.getMessage());
        }
    }
    // Click Method
    public void clickBtn(By by){
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("Element successfully got clicked "+ getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to click. " + e.getMessage());
        }
    }
    // EnterText Method
    public void enterText(By by , String value){
        try{
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Sent keys successfully. " + getElementDescription(by)+ " -> " + value);
        } catch (Exception e) {
            logger.error("Unable to enter the values in input-box. " + e.getMessage());
        }
    }
    // Method to get text from an input-box
    public String getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get the text from the input-box. " + e.getMessage());
            return "";
        }
    }
    // Compare the texts
    public boolean compareText(By by , String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (actualText.equals(expectedText)){
                logger.info("The actual and expected texts are matched " + actualText + "=" + expectedText);
                return true;
            }else{
                logger.error("The given actual and expected texts are not matching " + actualText + "!=" + expectedText);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Unable to compare the texts. " + e.getMessage());
        }
        return false;
    }
    // Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            logger.info("Element displayed successfully" + " -> " + getElementDescription(by));
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            System.out.println("Unable to display the element. " + e.getMessage());
            return false;
        }
    }
    // Scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true);", element);
        } catch (Exception e) {
            System.out.println("Unable to find the element. " + e.getMessage());
        }
    }
    // Wait for page to load
    public void waitForPageLoad(int timeOutSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutSec)).until(WebDriver -> ((JavascriptExecutor)WebDriver).executeScript("return document.readyState").equals("complete"));
            System.out.println("Page loaded successfully");
        } catch (Exception e) {
            System.out.println("Unable to load the page in " + timeOutSec + ". Exception: " + e.getMessage());
        }
    }
    // Get element description method to make logs better
    public String getElementDescription(By locator){
        // Check for null driver to avoid NullPointer Exception
        if (driver==null){
            return "Driver is null";
        }
        if (locator==null){
            return "Locator is null";
        }
        // Find the element using the locator
        try {
            WebElement element = driver.findElement(locator);
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeholder = element.getDomAttribute("placeholder");
            // Switching the element based on given String
            if(isNotEmpty(name)){
                return "The element with name:"+" "+name;
            } else if (isNotEmpty(id)) {
                return "The element with id:"+" "+id;
            } else if (isNotEmpty(text)) {
                return "The element with text:"+" "+truncateString(text , 20);
            } else if (isNotEmpty(className)) {
                return "The element with class:"+" "+className;
            } else if (isNotEmpty(placeholder)) {
                return "The element with placeholder:"+" "+placeholder;
            }
        } catch (Exception e) {
            logger.error("Unable to describe the element" + e.getMessage());
        }
        return "Unable to describe the element";
    }
    // Utility method is to check that a String is not null or empty
    private boolean isNotEmpty(String value){
        return value!=null && !value.isEmpty();
    }
    // Utility method to truncate a long string
    private String truncateString(String value, int maxLength){
        if (value==null || value.length()<=maxLength){
            return value;
        }
        return value.substring(0 , maxLength)+"...";
    }

}
