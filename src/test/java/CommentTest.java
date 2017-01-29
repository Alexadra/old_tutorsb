package test.java;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;
import main.pages.BaseClass;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.SearchPage;

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

public class CommentTest extends BaseClass {
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
	checkUnloginned(driver);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws AssertionError, Exception {
	LOG.info("_______________________________@AfterMethod_______________________________");
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
    public void addComment() throws Exception {
	LOG.info("_______________________________Test = addComment()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFoundHangout();
	hangout.addComment(DataProperties.get("comment"));
	home.findTextError();
	Assert.assertEquals(hangout.getCommenterName(), DataProperties.get("user2.name"));
	Assert.assertEquals(hangout.getCommentText(), DataProperties.get("comment"));
    }


}
