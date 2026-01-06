package com.orangehrm.pages;

import com.orangehrm.actionclass.ActionClass;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionClass actionClass;

    private By usernameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginBtn = By.xpath("//button[text()=' Login ']");
    private By errorText = By.xpath("//p[text()='Invalid Credentials']");

    // Initializing the ActionClass object by passing Webdriver instance
    public LoginPage(WebDriver driver){
        this.actionClass = BaseClass.getActionClass();
    }
    // Method to perform Login
    public void login(String username , String password){
        actionClass.enterText(usernameField , username);
        actionClass.enterText(passwordField , password);
        actionClass.clickBtn(loginBtn);
    }
    // Method to check if error message is displayed
    public boolean isErrorMessageDisplayed(){
        return actionClass.isDisplayed(errorText);
    }
    // Method to get the text from error message
    public String getErrorMessageText(){
        return actionClass.getText(errorText);
    }
    // Verify id error is correct or not
    public boolean validateErrorMessage(String expectedText){
        return actionClass.compareText(errorText , expectedText);
    }

}
