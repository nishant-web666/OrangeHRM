package com.orangehrm.pages;

import com.orangehrm.actionclass.ActionClass;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private ActionClass actionClass;

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logOutBtn = By.xpath("//a[text()='Logout']");
    private By orangeHrmLogo = By.xpath("//div[@class='oxd-brand-banner-");

    public HomePage(WebDriver driver){
        this.actionClass = BaseClass.getActionClass();
    }
    // Verify if Admin tab is visible or not
    public boolean verifyAdminTab(){
        return actionClass.isDisplayed(adminTab);
    }
    // Verify OrangeHRM logo is visible
    public boolean verifyLogo(){
        return actionClass.isDisplayed(orangeHrmLogo);
    }
    // Method to perform logout operation
    public void logout(){
        actionClass.clickBtn(userIdButton);
        actionClass.clickBtn(logOutBtn);
    }
}
