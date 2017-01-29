package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;

public class UserPage extends BaseClass{
    private WebDriver driver;
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);


    public UserPage(WebDriver driver) {
	this.driver = driver;
    }

    By addUser = By.cssSelector(".button.button_second.button_block.add_to_contacts_js");
    By removeUser = By.cssSelector(".button.button_primary.button_block.remove_from_contacts_js");
    By bigName = By.xpath("//div[@class='userpic userpic_size_big']//*[@class='userpic-user__name']");


    public void addContact() {
	if (driver.findElement(addUser).isDisplayed()) {
	    driver.findElement(addUser).click();   //button "follow" on the users page
	    waitForPageLoaded(driver);
	}
    }


    public void removeContact() {
	if (driver.findElements(removeUser).size()>0) {
	    driver.findElement(removeUser).click();	// //button "unfollow" on the users page
	}
    }


    public String getUserName() {
	LOG.info("This page belong to user: " + driver.findElement(bigName).getText());
	return driver.findElement(bigName).getText();
    }

}
