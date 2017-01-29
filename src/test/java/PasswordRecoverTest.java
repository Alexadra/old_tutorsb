package test.java;

import java.awt.AWTException;
import java.io.IOException;
import main.pages.BaseClass;
import main.pages.Gmail;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;

import main.pages.RecoverPage;


import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;

//10.06.2013 All passed.
public class PasswordRecoverTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");		//to change url go to data.properties file. It will change url in all tests
    private Cookie ck = new Cookie("04a09671af455c87714a2a1fd2a8f1af", "04a09671af455c87714a2a1fd2a8f1af");

    @BeforeClass
    public void setup() throws IOException {
	LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+this.getClass()+"<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	driver.manage().addCookie(ck);
	selectLang(DataProperties.get("language"), driver);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws Exception {
	LOG.info(">>>>>>>>>>@AfterMethod<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	home.failIfTextError();
    }


    @Test
    public void validRecoverStandartPass() throws Exception {
	//Asserts that you get recover letter with correct link and new password. Asserts that you can login by this new password.
	try {
	    LOG.info(">>>>>>>>>>>>>>>Test = validRecover()<<<<<<<<<<<<<<<");
	    HomePage home = PageFactory.initElements(driver,HomePage.class);
	    LoginPage login = home.loginClick();		//open login popup
	    RecoverPage recover = login.resetPass();		//link to recover
	    recover.resetPassword(DataProperties.get("valid.loginG2"));		//enter email for recover
	    home.findTextError();
	    Assert.assertEquals(recover.getResetMess(DataProperties.get("recover.corr")), DataProperties.get("recover.corr"));		//assert correct recover message
	    Thread.sleep(3000);
	    Gmail gmail = new Gmail();
	    gmail.gmailRecoverMess(DataProperties.get("valid.loginG2"), DataProperties.get("valid.passwordG2"), "Tutorsband", false);		//get last gmail letter from Tutorsband
	    driver.get(gmail.urlRecover);			//go to recover url from letter
	    home.findTextError();
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    my.exit();				//exit
	    home.loginClick();		// get login popup
	    login.userLogin(DataProperties.get("valid.loginG2"), gmail.newPass);		//enter login info : used email, password we got in letter
	    Assert.assertEquals(my.getMyName(), DataProperties.get("user.recover")); 		//assert that we logined with new password
	    home.failIfTextError();
	} catch(Exception e) {
	    e.printStackTrace();
	    throw e;
	} finally {
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    my.exit();
	}
    }


    @Test
    public void recoverLinkExpired() throws Exception {
	//asserts that u cant use recover link iwice
	try {
	    LOG.info(">>>>>>>>>>>>>>>Test = validRecover()<<<<<<<<<<<<<<<");
	    HomePage home= PageFactory.initElements(driver,HomePage.class);
	    LoginPage login=home.loginClick();		//open login popup
	    RecoverPage recover=login.resetPass();		//link to recover
	    recover.resetPassword(DataProperties.get("valid.loginG2"));		//enter email for recover
	    home.findTextError();
	    Assert.assertEquals(recover.getResetMess(DataProperties.get("recover.corr")), DataProperties.get("recover.corr"));		//assert correct recover message
	    Thread.sleep(3000);
	    Gmail gmail = new Gmail();
	    gmail.gmailRecoverMess(DataProperties.get("valid.loginG2"), DataProperties.get("valid.passwordG2"), "Tutorsband", false);		//get last gmail letter from Tutorsband
	    driver.get(gmail.urlRecover);			//go to recover url from letter
	    home.findTextError();
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    my.exit();				//exit
	    driver.get(gmail.urlRecover);		//go to recover url from letter again
	    Assert.assertEquals(recover.getRecovTitle(), DataProperties.get("recov.titleExpired"));		//assert that pop-up title is Expired url
	    Assert.assertEquals(recover.getExpiredEmail(), DataProperties.get("valid.loginG2"));		//assert that autocompleted email is correct
	} catch(Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }


    @Test
    public void validRecoverNewPass() throws Exception {
	//asserts that if u enter new password in 'new password popup' , u will be able to login with this new password
	try {
	    LOG.info(">>>>>>>>>>>>>>>Test = validRecover()<<<<<<<<<<<<<<<");
	    HomePage home= PageFactory.initElements(driver,HomePage.class);
	    LoginPage login=home.loginClick();		//open login popup
	    RecoverPage recover=login.resetPass();		//link to recover
	    recover.resetPassword(DataProperties.get("valid.loginG2"));		//enter email for recover
	    home.findTextError();
	    Assert.assertEquals(recover.getResetMess(DataProperties.get("recover.corr")), DataProperties.get("recover.corr"));
	    Thread.sleep(3000);
	    Gmail gmail = new Gmail();
	    gmail.gmailRecoverMess(DataProperties.get("valid.loginG2"), DataProperties.get("valid.passwordG2"), "Tutorsband", false);
	    driver.get(gmail.urlRecover);			//go to recover url from letter
	    recover.newPassword(DataProperties.get("valid.password"));
	    home.findTextError();
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    my.exit();				//exit
	    home.loginClick();		// get login popup
	    login.userLogin(DataProperties.get("valid.loginG2"), DataProperties.get("valid.password"));		//enter login info : used email, password we got in letter
	    Assert.assertEquals(my.getMyName(), DataProperties.get("user.recover")); 		//assert that we logined correctly
	    home.failIfTextError();
	} catch(Exception e) {
	    e.printStackTrace();
	    throw e;
	} finally {
	    MyPage my = PageFactory.initElements(driver,MyPage.class);
	    my.exit();
	}
    }


    @Test
    public void invalidRecover() throws AWTException {
	//asserts message if entered email is 100
	LOG.info(">>>>>>>>>>>>>>>Test = invalidRecover()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	waitForPageLoaded(driver);
	LoginPage login = home.loginClick();		//open login popup
	RecoverPage recover = login.resetPass();		//link to recover
	recover.resetPassword("100");
	Assert.assertEquals(recover.getErrorReset(DataProperties.get("error.bademail")), DataProperties.get("error.bademail"));
    }


    @Test
    public void invalidNoRecover() throws AWTException {
	//asserts message if entered email is null
	LOG.info(">>>>>>>>>>>>>>>Test = invalidNoRecover()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	LoginPage login = home.loginClick();		//open login popup
	RecoverPage recover = login.resetPass();
	recover.resetPassword("");
	Assert.assertEquals(recover.getErrorNoReset(DataProperties.get("error.noemail")), DataProperties.get("error.noemail"));
	closePopupButt(driver);
    }


    @Test
    public void zcaptchaDoubleRecover() throws Exception {
	LOG.info("_______________________Test = captchaDoubleRecover_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	driver.manage().deleteCookie(ck);
	LoginPage login = home.loginClick(); 		//login form go
	RecoverPage recover = login.resetPass();
	recover.resetPassword(DataProperties.get("valid.loginG2"));
	recover.resetPassword(DataProperties.get("valid.loginG2"));
	recover.assertCaptchaExists();
	driver.manage().addCookie(ck);
    }

}
