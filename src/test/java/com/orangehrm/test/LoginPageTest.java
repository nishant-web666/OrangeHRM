package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homepage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homepage  = new HomePage(getDriver());
        }

    @Test
    public void verifyValidLoginTest(){
        loginPage.login("Admin" , "admin123");
        Assert.assertEquals(homepage.verifyAdminTab(), true);
        homepage.logout();
        staticWait(2);
    }

    public void invalidLoginTest(){
        String expectedErrorMessage = "Invalid Credentials";
        loginPage.login("admin" , "admin321");
        Assert.assertTrue(loginPage.validateErrorMessage(expectedErrorMessage), "Test Failed: Invalid error message");
    }

}
