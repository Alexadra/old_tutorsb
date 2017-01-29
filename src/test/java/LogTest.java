package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.ChatPage;
import main.pages.ContactPage;
import main.pages.CreateHangoutPage;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MoneyPage;
import main.pages.MyPage;
import main.pages.NewsPage;
import main.pages.RecordPage;
import main.pages.SearchPage;
import main.pages.SettingsPage;
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
import  main.Driver.DataProperties;
import  main.Driver.WebDriverFactory;


//15.05.2013 All passed.
public class LogTest extends BaseClass {
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
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws Exception {
	LOG.info(">>>>>>>>>>@AfterMethod<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
    }


    @AfterClass
    public void closeDriver(){
	LOG.info(">>>>>>>>>>@AfterClass<<<<<<<<<");
	WebDriverFactory.dismissDriver();
    }


    /******************************************  MAIN PAGE  ******************************************/

    @Test
    public void validLoginPopup() throws Exception {
	try {
	    LOG.info("_______________________________Test = validLoginPopup_______________________________");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    LoginPage login = home.loginClick(); 		//login form go
	    home.findTextError();
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	    home.findTextError();										//check *** doesn't exists
	    Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	} finally {
	    exit(driver);
	}
    }


    @Test
    public void validLoginPage() throws Exception {
	try {
	    LOG.info("_______________________________Test = validLoginPage_______________________________");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	    driver.get(url+"login");	//login form go
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	    home.findTextError();										//check *** doesn't exists
	    Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	} finally {
	    exit(driver);
	}
    }


    @Test
    public void ifacebookValidLogin() throws InterruptedException, AWTException {
	try {
	    LOG.info("_______________________________Test = facebookValidLogin()_______________________________");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    driver.get(DataProperties.get("url.fb"));
	    home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordFb"));		//login in facebook system
	    driver.get(url);							//return to our page
	    LoginPage login = home.loginClick(); 		//login form go
	    login.loginWithFB();											// click to login with Facebook
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("loginF.name"));		//assert that user name is correct
	} finally {
	    exit(driver);
	    driver.get(DataProperties.get("url.fb"));
	    driver.manage().deleteAllCookies();
	    driver.navigate().refresh();
	}
    }


    @Test
    public void ifacebookValidLoginPopup() throws InterruptedException, AWTException {
	LOG.info("_______________________________Test = facebookValidLoginPopup()_______________________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.facebookBut());		//switch to facebook window
	home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordFb"));		//login in facebook
	PageFactory.initElements(driver,MyPage.class);
	driver.switchTo().window(homePage);							//switch to main page
	checkLangLogged(driver);
	Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("loginF.name"));	//assert that user name is correct
	exit(driver);
	driver.get(DataProperties.get("url.fb"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    @Test
    public void googleValidLogin() throws InterruptedException, AWTException {
	LOG.info("_______________________________Test = googleValidLogin()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(DataProperties.get("url.gm"));
	driver.manage().deleteAllCookies();
	driver.get(DataProperties.get("url.gm"));
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));		//login in Gmail system
	driver.get(url);		//return to our page
	LoginPage login = home.loginClick(); 		//login form go
	login.loginWithGM();		// click to login with Gmail
	home.jsErrorExist();
	Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("user3.name"));		//assert that user name is correct
	exit(driver);
	driver.get(DataProperties.get("url.gm"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    @Test
    public void googleValidLoginPopup() throws InterruptedException, AWTException {
	LOG.info("_______________________Test = googleValidLoginPopup_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.googleBut());		//switch to facebook window
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));
	PageFactory.initElements(driver,MyPage.class);
	driver.switchTo().window(homePage);							//switch to main page
	checkLangLogged(driver);
	Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("user3.name"));	//assert that user name is correct
	exit(driver);
	driver.get(DataProperties.get("url.gm"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    /******************************************  ERROR MESSAGES  ******************************************/


    @Test
    public void invalidLoginNoEmail() throws Exception {
	LOG.info("_______________________Test = invalidLoginNoEmail_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	login.userFailLogin("", DataProperties.get("valid.password"));
	home.findTextError();
	Assert.assertEquals(login.getEmailError(), DataProperties.get("error.noemail"));
    }


    @Test
    public void invalidLoginNoPass() {
	LOG.info("_______________________Test = invalidLoginNoPass_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	login.userFailLogin(DataProperties.get("valid.login1"), "");
	Assert.assertEquals(login.getPassError(), DataProperties.get("error.nopass"));
    }


    @Test
    public void invalidLoginWrongEmail() {
	LOG.info("_______________________Test = invalidLoginWrongEmail_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	login.userFailLogin ( DataProperties.get("invalid.login"), DataProperties.get("valid.password") );
	Assert.assertEquals (login.getEmailError(), DataProperties.get("error.bademail"));
    }


    @Test
    public void invalidLoginWrongPass() {
	LOG.info("_______________________Test = invalidLoginWrongPass_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	login.userFailLogin(DataProperties.get("valid.login1"), DataProperties.get("invalid.password"));
	Assert.assertEquals(login.getBadPass(), DataProperties.get("error.badpass"));
    }


    @Test
    public void loginRegGo() throws Exception {
	LOG.info("_______________________Test = invalidLoginWrongPass_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();
	login.toRegistLink();
	home.findTextError();
	Assert.assertTrue(login.singupPopupOpened(), "Signup popup isn't opened");
	login.toLoginLink();
	Assert.assertTrue(login.singinPopupOpened(), "Signin popup isn't opened");
    }


    @Test
    public void zcaptchaWrongPass() throws Exception {
	LOG.info("_______________________Test = captchaWrongPass_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	int i = 1;
	while (i<=6) {
	    login.userFailLogin(DataProperties.get("valid.login3"), DataProperties.get("invalid.password"));
	    i++;
	}
	home.findTextError();
	login.assertCaptchaExists();	//check that captcha appeared
    }


    /******************************************  HANGOUT PAGE  ******************************************/


    @Test
    public void loginFromHangoutPopup() throws Exception {
	LOG.info("_______________________Test = loginFromHangoutPopup_______________________");
	HomePage home= PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFreeActive();
	home.findTextError();
	hangout.takePartHangoutUnloged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	PageFactory.initElements(driver,MyPage.class);
	hangout.assertUserTakes(getMyNameMenu(driver));
	exit(driver);
    }



    @Test
    public void loginFBHangoutPopup() throws Exception {
	LOG.info("_______________________Test = loginFBHangoutPopup_______________________");
	HomePage home= PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFreeActive();
	home.findTextError();
	hangout.takePartHangoutUnloged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.facebookBut());		//switch to facebook window
	home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordFb"));
	driver.switchTo().window(homePage);
	PageFactory.initElements(driver,MyPage.class);
	hangout.assertUserTakes(getMyNameMenu(driver));
	exit(driver);
	driver.get(DataProperties.get("url.fb"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }



    @Test
    public void loginGMHangoutPopup() throws Exception {
	LOG.info("_______________________Test = loginGMHangoutPopup_______________________");
	HomePage home= PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFreeActive();
	home.findTextError();
	hangout.takePartHangoutUnloged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.googleBut());		//switch to facebook window
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));
	driver.switchTo().window(homePage);
	PageFactory.initElements(driver,MyPage.class);
	hangout.assertUserTakes(getMyNameMenu(driver));
	exit(driver);
	driver.get(DataProperties.get("url.gm"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    @Test
    public void loginFromHangoutHeader() throws Exception {
	LOG.info("_______________________Test = loginFromHangoutHeader_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFoundHangout();
	home.findTextError();
	LoginPage login = home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	hangout.waitCommentInput();
	exit(driver);
    }


    /******************************************  HANGOUT CREATION  ******************************************/


    @Test
    public void loginFromHangCreate() throws Exception {
	LOG.info("_______________________Test = loginFromHangCreate_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("create.url"));		//go to create hang page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	CreateHangoutPage create = PageFactory.initElements(driver, CreateHangoutPage.class);
	home.findTextError();
	create.assertCreatePage();
	exit(driver);
    }


    @Test
    public void loginFromHeaderHangCreate() throws Exception {
	LOG.info("_______________________Test = loginFromHeaderHangCreate_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	CreateHangoutPage create = home.unloginedCreateHang();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	home.findTextError();
	create.assertCreatePage();
	exit(driver);
    }


    @Test
    public void loginGMHeaderHangCreate() throws Exception {
	LOG.info("_______________________Test = loginFromHeaderHangCreate_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	CreateHangoutPage create = home.unloginedCreateHang();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.googleBut());		//switch to facebook window
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));
	PageFactory.initElements(driver, MyPage.class);
	driver.switchTo().window(homePage);
	home.findTextError();
	create.assertCreatePage();
	exit(driver);
	driver.get(DataProperties.get("url.gm"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    @Test
    public void loginFBHeaderHangCreate() throws Exception {
	LOG.info("_______________________Test = loginFromHeaderHangCreate_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	CreateHangoutPage create = home.unloginedCreateHang();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();
	String homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(login.facebookBut());		//switch to facebook window
	home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordFb"));		//login in facebook
	driver.switchTo().window(homePage);
	home.findTextError();
	create.assertCreatePage();
	PageFactory.initElements(driver,MyPage.class);
	exit(driver);
	driver.get(DataProperties.get("url.fb"));
	driver.manage().deleteAllCookies();
	driver.navigate().refresh();
    }


    /******************************************  CLOSED PAGES  ******************************************/


    @Test
    public void loginFromNews() throws Exception {
	LOG.info("_______________________Test = loginFromNews_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("news.url"));		//go to news page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	home.findTextError();
	NewsPage news = PageFactory.initElements(driver, NewsPage.class);
	news.assertNewsPage();
	exit(driver);
    }


    @Test
    public void loginFromFollowing() throws Exception {
	LOG.info("_______________________Test = loginFromFollowing_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("contact.url"));		//go to following page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	ContactPage contact = PageFactory.initElements(driver, ContactPage.class);
	home.findTextError();
	contact.assertContactPage();
	exit(driver);
    }


    @Test
    public void loginFromFollowers() throws Exception {
	LOG.info("_______________________Test = loginFromFollowers_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("followers.url"));		//go to followers page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	ContactPage contact = PageFactory.initElements(driver, ContactPage.class);
	home.findTextError();
	contact.assertContactPage();
	exit(driver);
    }


    @Test
    public void loginFromSettings() throws Exception {
	LOG.info("_______________________Test = loginFromSettings_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("config.url"));		//go to settings page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	SettingsPage config = PageFactory.initElements(driver, SettingsPage.class);
	home.findTextError();
	config.assertSettPage();
	exit(driver);
    }


    @Test
    public void loginFromSettAccount() throws Exception {
	LOG.info("_______________________Test = loginFromSettAccount_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("account.url"));		//go to settings acount page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	SettingsPage config = PageFactory.initElements(driver, SettingsPage.class);
	home.findTextError();
	config.assertAccountPage();
	exit(driver);
    }


    @Test
    public void loginFromChat() throws Exception {
	LOG.info("_______________________Test = loginFromChat_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("chat.url"));		//go to chat page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	ChatPage chat = PageFactory.initElements(driver, ChatPage.class);
	home.findTextError();
	chat.assertChatPage();
	exit(driver);
    }


    @Test
    public void loginFromMoney() throws Exception {
	LOG.info("_______________________Test = loginFromMoney_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+DataProperties.get("money.url"));		//go to money page, as Im not logged in, login form will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data, click login
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	home.findTextError();
	money.assertMoneyPage();
	exit(driver);
    }


    @Test
    public void loginFromComments() throws Exception { //to do login from comments
	LOG.info("_______________________Test = loginFromComments_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	HangoutPage hangout = search.openFirstFoundHangout();
	home.findTextError();
	LoginPage login = hangout.loginLinkFromComments();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));		//login data
	hangout.waitCommentInput();		//assert that after login I was brought to hangout page (assert that comment input exists)
	exit(driver);
    }


    @Test
    public void iloginFromVideoPay() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	driver.get(url+"/search");
	SearchPage search = new SearchPage(driver);
	search.videoTab();
	RecordPage record = search.openFirstPaidVideo();
	home.findTextError();
	MoneyPage money = record.buyVideo();		//click on button "take part". as I unlogged , register popup will be shown
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	money.assertPayPopup();
	closePopupButt(driver);
	exit(driver);
    }


    /******************************************  PRICING  ******************************************/


    @Test
    public void loginFromPricingFree() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyFreePricingUnlogged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	exit(driver);
    }


    @Test
    public void loginFromPricingFreeBottom() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyFreePricingUnlogBottom();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	exit(driver);
    }


    @Test
    public void loginFromPricingSilver() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buySilverPricingUnlogged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	money.assertPayPopup();
	closePopupButt(driver);
	exit(driver);
    }


    @Test
    public void loginFromPricingSilverBottom() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buySilverPricingUnlogBottom();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	money.assertPayPopup();
	closePopupButt(driver);
	exit(driver);
    }


    @Test
    public void loginFromPricingGold() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyGoldPricingUnlogged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	money.assertPayPopup();
	closePopupButt(driver);
	exit(driver);
    }


    @Test
    public void loginFromPricingGoldBottom() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyGoldPricingUnlogBottom();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	money.assertPayPopup();
	closePopupButt(driver);
	exit(driver);
    }


    @Test
    public void loginFromPricingEnterp() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyEnterpPricingUnlogged();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	ChatPage chat = PageFactory.initElements(driver, ChatPage.class);
	home.findTextError();
	chat.assertChatPage();
	exit(driver);
    }


    @Test
    public void loginFromPricingEnterpBottom() throws Exception{
	LOG.info("_______________________Test = loginFromVideoPay _______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.get(url+"/pricing");
	home.findTextError();
	MoneyPage money = PageFactory.initElements(driver, MoneyPage.class);
	money.buyEnterpPricingUnlogBottom();
	LoginPage login = PageFactory.initElements(driver, LoginPage.class);
	login.toLoginLink();		//click on login link in register popup
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	Assert.assertTrue(getMyNameMenu(driver).contains(DataProperties.get("user1.name").substring(0, 23)));
	ChatPage chat = PageFactory.initElements(driver, ChatPage.class);
	home.findTextError();
	chat.assertChatPage();
	exit(driver);
    }

























}
