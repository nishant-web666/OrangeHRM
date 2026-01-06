package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class DemoTest2 extends BaseClass {
    @Test
    public void dummyTest2(){
        String title = driver.getTitle();
        assert title.equals("OrangeHRM") : "Test Failed: Title is not matching";

        System.out.println("Test Passed: Title matched " + title);
    }
}
