package main.pages;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;


public class ContactPage extends BaseClass {
    private WebDriver driver;
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);

    public ContactPage (WebDriver driver) {
	this.driver = driver;
    }


    //========================================================LOCATORS==========================================================================
    By firstName = By.xpath("(//div[@class='contact-item clearfix'][1]//a)[1]");
    By allNames = By.cssSelector(".userpic-user__name");
    By removeUser = By.cssSelector(".button.button_primary.remove_from_contacts_js");
    By contactList = By.cssSelector(".contacts-list");
    //========================================================END LOCATORS======================================================================

    public String getFirstName() {
	LOG.info("First user name is: "+driver.findElement(firstName).getText());
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(firstName));
	return driver.findElement(firstName).getText();
    }


    public String getFirstUserLink() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(firstName));
	return driver.findElement(firstName).getAttribute("href");
    }


    public void removeFirstUser() {
	driver.findElement(removeUser).click();
	waitForPageLoaded(driver);
    }


    public void goToFirstUser() {
	driver.findElement(firstName).click();
	waitForPageLoaded(driver);
    }


    public boolean assertAllUsers(String mainName) {
	List <WebElement> usersNames = driver.findElements(allNames); //создаем список имен всех контактов во вкладке Возможные контакты
	for(WebElement name: usersNames) {											//проходимся по всем элементам
	    if (name.getText().equals(mainName)) {                        // если имя контакта = искомому имени
		return true;
	    } else continue;														//если имя контакта не = искомому имени , берем следующее имя
	}
	return false;
    }


    public void assertContactPage() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(contactList));
    }


}
