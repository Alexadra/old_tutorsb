package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.SettingsPage;

import org.openqa.selenium.By;
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
import main.Driver.WebDriverFactory;

//10.06.2013 All passed.
public class SettingTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");

    By imageMypage = By.id("userinfo_upload_avatar_main");
    By imageSetting = By.id("userinfo_upload_avatar");


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
    public void mainSetting() throws Exception {
	try {
	    LOG.info(">>>>>>>>>>>>>>>Test = MainSetting<<<<<<<<<<<<<<<");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    LoginPage login = home.loginClick(); 		//login form go
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login
	    SettingsPage sett = openSettings(driver);										//open settings
	    home.findTextError();														//find text missing
	    sett.inputSett(DataProperties.get("user.newname"), DataProperties.get("sett.town"),
		    DataProperties.get("sett.interest"), DataProperties.get("sett.url"), DataProperties.get("sett.about"));	//введение данных во все формы настроек
	    driver.navigate().refresh();
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("user.newname"));	//assert name was saved
	    // Assert.assertTrue(my.getTown().contains(DataProperties.get("sett.fullTown").split(",")[0].trim()));	//assert city was saved
	    // Assert.assertEquals(my.getInterest(), DataProperties.get("sett.interest"));	//assert interest was saved
	    Assert.assertEquals(driver.getCurrentUrl(), url+DataProperties.get("sett.url"));	//assert url was changed
	    //  Assert.assertTrue(my.getInfo().contains(DataProperties.get("sett.about")));		//assert info was saved
	} finally {
	    SettingsPage sett = openSettings(driver);
	    sett.inputSett(DataProperties.get("user1.name"),null,null,"","");
	}
    }


    @Test
    public void settPhotoInput() throws AWTException {
	//download avatar from settings
	LOG.info(">>>>>>>>>>>>>>>Test = photoInput()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login
	openSettings(driver);
	if (my.isImagenotPresent() == true) {		//assert if user has no image
	    driver.findElement(imageSetting).sendKeys(DataProperties.path("file.name"));
	    my.deleteImage();
	} else {					//if user already has simage
	    my.deleteImage();		//first delete image
	    openSettings(driver);
	    driver.findElement(imageSetting).sendKeys(DataProperties.path("file.name"));
	    my.deleteImage();
	}
    }


    @Test
    public void mainSettInvalid() throws AWTException{
	LOG.info(">>>>>>>>>>>>>>>Test = MainSettInvalid<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SettingsPage sett = openSettings(driver);                                         //открываем headUser и настройки
	sett.inputSettNoname(null);                                              //оставление пустой формы имени
	Assert.assertEquals(sett.errorName(DataProperties.get("error.noname")), DataProperties.get("error.noname"));     //верификация ошибки
    }


    @Test
    public void mainSettUrlInvalid() throws AWTException {
	LOG.info(">>>>>>>>>>>>>>>Test = aMainSettUrlInvalid<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SettingsPage sett = openSettings(driver);
	sett.inputSettBadUrl(DataProperties.get("user1.name"),DataProperties.get("url.bad"));
	Assert.assertEquals(sett.errorUrl(DataProperties.get("error.badurl")),DataProperties.get("error.badurl"));
	driver.navigate().refresh();                                   //refresh для того, чтоб работал wait untill visibilityOf error message
	sett.inputSettBadUrl(DataProperties.get("user1.name"),DataProperties.get("url.reserv"));
	Assert.assertEquals(sett.errorUrl(DataProperties.get("error.resurl")),DataProperties.get("error.resurl"));
	driver.navigate().refresh();
	sett.inputSettBadUrl(DataProperties.get("user1.name"),DataProperties.get("url.short"));
	Assert.assertEquals(sett.errorUrl(DataProperties.get("error.shorturl")),DataProperties.get("error.shorturl"));
	driver.navigate().refresh();
	sett.inputSettBadUrl(DataProperties.get("user1.name"),DataProperties.get("url.used"));
	Assert.assertEquals(sett.errorUrl(DataProperties.get("error.usedurl")),DataProperties.get("error.usedurl")); //пока что эти шаги создают баг 404
    }


    @Test
    public void invalidMyAccount() throws Exception {
	LOG.info(">>>>>>>>>>>>>>>Test = InvalidMyAccount()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	login.userLogin ( DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	SettingsPage sett = openSettings(driver);
	sett.accountSett();
	sett.inputAccountData("", DataProperties.get("valid.password"));
	home.findTextError();
	Assert.assertEquals ( sett.errorEmail(), DataProperties.get("error.bademail"));
	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	sett.inputAccountData( DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	Assert.assertEquals ( sett.errorEmail(), DataProperties.get("error.usedemail"));
	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	sett.inputAccountData ( DataProperties.get("invalid.login") , DataProperties.get("valid.password"));
	Assert.assertEquals(sett.errorEmail(), DataProperties.get("error.bademail"));
	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);							//временное решение!! не успевает выполнить ассерт
	sett.inputAccountData ( DataProperties.get("valid.login1"), DataProperties.get("invalid.password"));
	Assert.assertEquals(sett.errorPass(), DataProperties.get("error.shortpass"));
    }


}