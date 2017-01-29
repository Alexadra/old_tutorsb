package test.java;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.ChatPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.UserPage;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Test;

import main.Driver.DataProperties;
import  main.Driver.WebDriverFactory;


public class ChatTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");

    @BeforeMethod
    public void setup() throws IOException {
	LOG.info("______________________________________"+this.getClass()+"______________________________________");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod (alwaysRun = true)
    public void unLogin() throws AssertionError, Exception {
	LOG.info(">>>>>>>>>>@AfterMethod<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	exit(driver);
    }


    @AfterClass
    public void closeDriver(){
	LOG.info(">>>>>>>>>>@AfterClass<<<<<<<<<");
	WebDriverFactory.dismissDriver();
    }



    @Test
    public void sendHelpMessage() throws Exception {
	LOG.info(">>>>>>>>>>>>>>>Test = sendMessage()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	ChatPage chat = my.myMessages();
	home.findTextError();
	chat.sendToSupport("Help1");
	int count = chat.getUserMessCount();
	chat.sendButton();
	chat.waitForMessSend(driver, count+1);
	Assert.assertEquals(chat.getLastMess(), "Help1");
	chat.sendToSupport("Help3");
	chat.sendEnter();
	chat.waitForMessSend(driver, count+2);
	Assert.assertEquals(chat.getLastMess(), "Help3");
    }


    @Test
    public void searchNoUserChat(){
	LOG.info(">>>>>>>>>>>>>>>Test = searchNoUserChat()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	ChatPage chat = my.myMessages();
	chat.searchChatUser("dfgdgsfgdf");
	Assert.assertEquals(chat.getEmptySearchMess(), "Собеседник не найден");
    }


    @Test
    public void searchUserChat(){
	LOG.info(">>>>>>>>>>>>>>>Test = searchUserChat()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	ChatPage chat = my.myMessages();
	String name = chat.getFirstUserName();
	chat.searchChatUser(name);
	Assert.assertTrue(chat.getSearchResultText().contains(name));
    }


    @Test
    public void assertGoToUserPage(){
	LOG.info(">>>>>>>>>>>>>>>Test = assertGoToUserPage()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	ChatPage chat = my.myMessages();
	String name = chat.getFirstUserTitle();
	UserPage user = chat.goToUserPage();
	Assert.assertEquals(user.getUserName(), name);
    }
}