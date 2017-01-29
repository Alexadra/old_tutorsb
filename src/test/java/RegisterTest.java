
/*
10.06.2013 All passed. Оптимизировать driver.navigate().refresh(); . Используется для захвата нового текста ошибки (так как две проверки в одном тесте)
 */

package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.CreateHangoutPage;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.SearchPage;
import main.pages.SettingsPage;

import main.pages.MyPage;
import main.pages.RegisterPage;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;

public class RegisterTest extends BaseClass {

    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");
    private Cookie ck = new Cookie("04a09671af455c87714a2a1fd2a8f1af", "04a09671af455c87714a2a1fd2a8f1af");


    @BeforeClass
    public void setup() throws InterruptedException, IOException {
	LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+this.getClass()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	checkUnloginned(driver);
	driver.manage().addCookie(ck);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }


    @BeforeMethod
    public void setupMethod() throws IOException {
	LOG.info("______________________________________"+this.getClass()+"______________________________________");
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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


    // Валидность сообщения, если имя при регистрации не введено
    @Test
    public void invalidRegName() throws Exception {
	LOG.info(">>>>>>>>>>>>>>>Test = invalidRegName()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.navigate().refresh();
	RegisterPage register = home.registerClick();
	register.userReg(null, randomEmail(), DataProperties.get("valid.password"));
	home.findTextError();
	Assert.assertEquals(register.getNameError(), DataProperties.get("reg.noname"));
	closePopupButt(driver);
    }


    // Валидность сообщения, если эмеил при регистрации не введен , если эмеил в неверном формате, если эмеил уже используется
    @Test
    public void invalidRegEmail() throws AWTException {
	LOG.info("_______________________Test = invalidRegEmail_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	RegisterPage register = home.registerClick();
	register.userReg( DataProperties.get("user2.name") , null , DataProperties.get("valid.password"));
	Assert.assertEquals(register.getMailError(), DataProperties.get("reg.noemail"));
	driver.navigate().refresh();
	register.userReg( DataProperties.get("user2.name") , DataProperties.get("invalid.login") , DataProperties.get("valid.password"));
	Assert.assertEquals(register.getMailError(), DataProperties.get("reg.bademail"));
	driver.navigate().refresh();
	register.userReg( DataProperties.get("user2.name") , DataProperties.get("valid.login1") , DataProperties.get("valid.password"));
	Assert.assertTrue(register.getUsedMailErr().contains(DataProperties.get("reg.usedemail")));
	closePopupButt(driver);
    }


    // Валидность сообщения, если пароль при регистрации не введен, если пароль менее 8 цифр
    @Test
    public void invalidRegPass() throws AWTException {
	LOG.info("_______________________Test = invalidRegPass_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	RegisterPage register = home.registerClick();
	register.userReg( DataProperties.get("user2.name"), randomEmail(), null);
	Assert.assertEquals(register.getPassError(), DataProperties.get("error.nopass"));
	driver.navigate().refresh();
	register.userReg ( DataProperties.get("user2.name"), randomEmail() , DataProperties.get("invalid.password") );
	Assert.assertEquals(register.getPassError(), DataProperties.get("reg.shortpass"));
	closePopupButt(driver);
    }


    //стандартная регистрация
    @Test
    public void registration() throws AWTException {
	LOG.info("_______________________Test = registration_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	RegisterPage register = home.registerClick();		//link to open register popup
	register.userReg(DataProperties.get("user2.name"), randomEmail(), DataProperties.get("valid.password"));
	Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("user2.name"));		//assert that we came to necessary page
	exit(driver);			//unlogin
	driver.manage().deleteCookie(ck);
	home.registerClick();
	register.userReg(DataProperties.get("user2.name"), randomEmail(), DataProperties.get("valid.password"));
	register.assertCaptchaExists();
	driver.manage().addCookie(ck);
    }


    //стандартная регистрация
    @Test
    public void registerFromHeaderHangCreate() throws AWTException {
	LOG.info("_______________________Test = registration_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	CreateHangoutPage create = home.unloginedCreateHang();		//link to open register popup
	RegisterPage register = PageFactory.initElements(driver,RegisterPage.class);
	register.userReg(DataProperties.get("user2.name"), randomEmail(), DataProperties.get("valid.password"));
	Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("user2.name"));		//assert that we came to necessary page
	create.assertCreatePage();
	exit(driver);			//unlogin
    }


    //register from hangout page. Click take part in hangout and enter date in popup
    @Test
    public void registerFromHangoutPopup() throws Exception {
	try {
	    LOG.info("_______________________Test = registerFromHangoutPopup_______________________");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    driver.get(url+"/search");
	    SearchPage search = new SearchPage(driver);
	    HangoutPage hangout = search.openFirstFreeActive();
	    home.findTextError();
	    hangout.takePartHangoutUnloged();
	    //click on button "take part". as I unlogged , register popup will be shown:
	    RegisterPage register = PageFactory.initElements(driver, RegisterPage.class);
	    register.userReg(DataProperties.get("user2.name"), randomEmail(), DataProperties.get("valid.password"));
	    // Assert.assertTrue(register.assertInterestsValid());
	    // register.chooseInterest();
	    hangout.waitCommentInput();
	    waitForPageLoaded(driver);
	    hangout.assertUserTakes(getMyNameMenu(driver));
	} finally {
	    exit(driver);
	}
    }


    /*go to register popup, register with facebook (enter email and password). Cancel friends invite, choose interests.
     * On my page assert that user name is correct. To have an able to repeat the test, change user email to random one.)*/
    @Test
    public void registerWithFB() throws Exception {
	String homePage = driver.getWindowHandle();			//remember main page handle
	try {
	    LOG.info("_______________________________Test = registerWithFB_______________________________");
	    HomePage home = PageFactory.initElements(driver,HomePage.class);
	    RegisterPage register = home.registerClick();
	    home.switchToNewWindow(register.facebookBut());
	    home.facebooklogin(DataProperties.get("valid.registF"), DataProperties.get("valid.passwordF"));
	    driver.switchTo().window(homePage);
	    home.fbCancelInvite();
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("userF.name"));
	} finally {
	    driver.switchTo().window(homePage);
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    SettingsPage sett = my.openSettings(driver);
	    sett.accountSett();
	    sett.changeEmail(randomEmail());
	    sett.submitAccountSett();
	    exit(driver);
	    driver.get(DataProperties.get("url.fb"));
	    driver.manage().deleteAllCookies();
	}
    }


    /*go to register popup, register with facebook (enter email and password). User has no friends on fb, so invite popup doesn't open.
     * Choose interests. Get last mail from Gmail, get password from it. Exit. Try to login with email and new password from mail.
     *  On my page assert that user name is correct. To have an able to repeat the test, change user email to random one.)*/
    @Test
    public void registerNewPassWithFB() throws Exception {
	try {
	    LOG.info("_______________________________Test = registerNewPassWithFB()_______________________________");
	    HomePage home = PageFactory.initElements(driver,HomePage.class);
	    RegisterPage register = home.registerClick();
	    String homePage = driver.getWindowHandle();			//remember main page handle
	    home.switchToNewWindow(register.facebookBut());
	    home.facebooklogin(DataProperties.get("valid.registF2"), DataProperties.get("valid.passwordFb"));
	    driver.switchTo().window(homePage);
	    // MyPage my = register.chooseInterest();
	    /*  Gmail gmail = new Gmail();
	    String newPass = gmail.gmailGetPassword(DataProperties.get("valid.registF2"), DataProperties.get("valid.passwordFb"), "Tutorsband", false);
	    System.out.println("newPass="+newPass);
	    exit(driver);
	    LoginPage login = home.loginClick();
	    login.userLogin(DataProperties.get("valid.registF2"), newPass);*/		//assert that new password works
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("userF2.name"));
	} finally {
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    SettingsPage sett = my.openSettings(driver);
	    sett.accountSett();
	    sett.changeEmail(randomEmail());
	    sett.submitAccountSett();
	    exit(driver);
	    driver.get(DataProperties.get("url.fb"));
	    driver.manage().deleteAllCookies();
	}
    }


    /*go to register popup, register with gmail (enter email and password). Submit friends invite.
     * Choose interests. Get last mail from Gmail, get password from it. Exit. Try to login with email and new password from mail.
     *  On my page assert that user name is correct. Get last mail of user, that was invited by our user. Assert that mail text contains invitation.
     *   To have an able to repeat the test, change user email to random one.)*/
    @Test
    public void registerWithGM() throws Exception {
	try {
	    LOG.info("_______________________________Test = registerWithGM_______________________________");
	    HomePage home = PageFactory.initElements(driver,HomePage.class);
	    RegisterPage register = home.registerClick();
	    String homePage = driver.getWindowHandle();		//remember main page handle
	    home.switchToNewWindow(register.googleBut());
	    home.googlelogin(DataProperties.get("valid.registG"), DataProperties.get("valid.passwordG"));
	    driver.switchTo().window(homePage);
	    register.confirmInviteGM();
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("userG.name"));	//assert that user name is correct
	    /* Gmail gmail = new Gmail();
	    String newPass = gmail.gmailGetPassword(DataProperties.get("valid.registG"), DataProperties.get("valid.passwordG"), "Tutorsband", false);		//get new password, that was send to mail
	    exit(driver);
	    LoginPage login = home.loginClick(); 		//login form go
	    login.userLogin(DataProperties.get("valid.registG"), newPass);		//assert that new password works
	    home.findTextError();
	    Assert.assertEquals(getMyNameMenu(driver), DataProperties.get("userG.name"));*/
	} finally {
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    SettingsPage sett = my.openSettings(driver);
	    sett.accountSett();
	    sett.changeEmail(randomEmail());
	    sett.submitAccountSett();
	    exit(driver);
	    driver.get(DataProperties.get("url.gm"));
	    driver.manage().deleteAllCookies();
	}
    }






}
