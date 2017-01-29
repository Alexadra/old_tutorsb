package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.SearchPage;
import main.pages.SettingsPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import main.Driver.DataProperties;
import  main.Driver.WebDriverFactory;


public class SearchTest extends BaseClass {
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
	home.failIfTextError();
	MyPage my = PageFactory.initElements(driver, MyPage.class);
	my.exit();
    }


    @Test
    public void hangoutSearch() throws Exception {
	LOG.info(">>>>>>>>>>>>>>>Test = hangoutSearch<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SearchPage search = my.getSearchResult (""); //
	home.findTextError();
	String hangName = search.getFirstResultName();
	my.getSearchResult (hangName);
	Assert.assertEquals(search.getFirstResultName(), hangName);
	home.failIfTextError();
    }


    @Test
    public void userTagSearch() throws AWTException{
	LOG.info(">>>>>>>>>>>>>>>Test = hangoutSearch<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!my.userTagExist()){
	    SettingsPage sett = my.openSettings();
	    sett.setTags(DataProperties.get("sett.interest"));
	    sett.submitSettings();
	}
	SearchPage search = my.clickUserTag();
	search.assertTagSearch(my.tagText);
    }


    @Test
    public void hangoutTagSearch() throws AWTException{
	LOG.info(">>>>>>>>>>>>>>>Test = hangoutSearch<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (url.contains("tutorsband")) driver.get(url + DataProperties.get("hang.tagstut"));
	else driver.get(url + DataProperties.get("hang.tags"));
	SearchPage search = my.clickHangTag();
	search.assertTagSearch(my.tagText);
    }


    @Test
    public void authorHangoutSearch() throws Exception {
	LOG.info(">>>>>>>>>>>>>>>Test = hangoutSearch<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SearchPage search = my.getSearchResult (""); //
	home.findTextError();
	String hangAuthor = search.getFirstResultAuthor();
	my.getSearchResult (hangAuthor);
	search.assertAllAuthors(hangAuthor);
	home.failIfTextError();
    }


    @Test
    public void videoSearch() throws Exception {
	LOG.info("_______________________________Test = videoSearch()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SearchPage search = my.getSearchResult ("");
	search.videoTab();
	home.findTextError();
	String videoName = search.getFirstResultName();
	my.getSearchResult (videoName);
	Assert.assertEquals(search.getFirstResultName(), videoName);
	home.failIfTextError();
    }


    @Test
    public void foundNoHangout(){
	LOG.info("_______________________________Test = videoSearch()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SearchPage search = my.getSearchResult ("draerw34q3easra2314");
	Assert.assertEquals(search.getEmptySearchMess(), DataProperties.get("search.emptytext"));
    }


    @Test
    public void foundNoVideo(){
	LOG.info("_______________________________Test = foundNoVideo()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SearchPage search = my.getSearchResult ("draerw34q3easra2314");
	search.videoTab();
	waitForPageLoaded(driver);
	Assert.assertEquals(search.getEmptySearchMess(), DataProperties.get("search.emptytext"));
    }




}
