package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;

public class ChatPage extends BaseClass {
    private WebDriver driver;

    public ChatPage(WebDriver driver) {
	this.driver = driver;
    }

    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    public String onlineStatus;

    //========================================================LOCATORS==========================================================================
    By support = By.id("chat_282");
    By messInput = By.id("message_text");
    By messSend = By.id("message_send");
    By lastMess = By.xpath("//*[@id='chat_block']/div[last()]//div[@class='chat__message-text text__p']");
    By messCount = By.xpath("//div[@id='chat_block']/div[not(@class='chat__message chat__message_history')]");
    By secondUser = By.xpath("//div[@id='contacts_list_block']/div[2]/a");
    By chatSearch = By.id("chat_search");
    By nocontactMess = By.id("chat_search_no_contacts");
    By firstUserName = By.xpath("(//*[@class='userpic-user__name js_contact_name'])[1]");
    By userNameTitle = By.id("chat_user_block");
    By searchResult = By.xpath("//div[@id='contacts_list_block']/div[not(@class) or @class='']");
    By userNameLink = By.xpath("//div[@id='chat_user_block']//a");
    //========================================================END LOCATORS======================================================================


    public void sendToSupport(String mess) {
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	home.waitForPageLoaded(driver);
	driver.findElement(support).click();
	driver.findElement(messInput).clear();
	driver.findElement(messInput).sendKeys(mess);
    }

    public void sendButton() {
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.findElement(messSend).click();
	home.waitForPageLoaded(driver);
    }

    public void sendEnter() {
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.findElement(messInput).sendKeys(Keys.RETURN);
	home.waitForPageLoaded(driver);
    }

    public String getLastMess(){
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	home.waitForPageLoaded(driver);
	return driver.findElement(lastMess).getText();
    }


    public int getUserMessCount(){
	return driver.findElements(messCount).size();
    }


    public void waitForMessSend(WebDriver driver,final int count) {
	ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		return count==getUserMessCount();
	    }
	};
	Wait <WebDriver> wait = new WebDriverWait(driver,10);
	try {
	    wait.until(expectation);
	} catch(Throwable error) {
	    LOG.info( error.getMessage() );
	}
    }


    public void assertChatPage() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(messInput));
    }


    public void searchChatUser(String name){
	driver.findElement(chatSearch).sendKeys(name);
	waitForPageLoaded(driver);
    }


    public String getEmptySearchMess(){
	return driver.findElement(nocontactMess).getText();
    }


    public String getFirstUserName(){
	LOG.info("First user in chat contact list is: " + driver.findElement(firstUserName).getText());
	return driver.findElement(firstUserName).getText();
    }


    public String getFirstUserTitle(){
	LOG.info("First title in chat contact list is: " + driver.findElement(userNameTitle).getText());
	return driver.findElement(userNameTitle).getText();
    }


    public String getSearchResultText(){
	return driver.findElement(searchResult).getText();
    }


    public void avoidSupport(){
	if (driver.findElements(userNameLink).size()==0){
	    driver.findElement(secondUser).click();
	}
    }


    public UserPage goToUserPage(){
	avoidSupport();
	driver.findElement(userNameLink).click();
	return PageFactory.initElements(driver, UserPage.class);
    }



}
