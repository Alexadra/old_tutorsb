package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;
import main.pages.BaseClass;
import main.pages.CreateHangoutPage;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.NotifyPage;
import main.pages.RecordPage;
import main.pages.WebinarPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NotifyTest extends BaseClass{

    private static Logger LOG = LoggerFactory.getLogger(NotifyTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");


    @BeforeMethod
    public void setup() throws IOException {
	LOG.info("______________________________________"+this.getClass()+"______________________________________");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	driver.get(DataProperties.get("urlcookie"));
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
    public void commentNotify() throws AWTException, InterruptedException{
	LOG.info(">>>>>>>>>>>>>>>Test = participateHangout()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(10), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveHangout();
	String hangUrl = driver.getCurrentUrl();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	driver.get(hangUrl);
	hangout.addComment(DataProperties.get("comment"));
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	NotifyPage notify = my.openNotify();
	Assert.assertTrue(notify.getFirstNotific().contains(DataProperties.get("notific.comment")));
    }


    @Test
    public void recordNotifies() throws AWTException, InterruptedException{
	LOG.info(">>>>>>>>>>>>>>>Test = participateHangout()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    String hangNum = randomNumber();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		    getTodayData(), timeFuture(3), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	    create.saveHangout();
	    String hangUrl = driver.getCurrentUrl();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	    driver.get(hangUrl);
	    hangout.takePartFreeHangout();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	    WebinarPage webinar = hangout.startHangout(hangUrl);
	    RecordPage record = webinar.finishHangout();
	    record.publishRecord();
	    NotifyPage notify = my.openNotify();
	    Assert.assertTrue(notify.getFirstNotific().contains(DataProperties.get("notific.recordready")));
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	    my.openNotify();
	    Assert.assertTrue(notify.getFirstNotific().contains(DataProperties.get("notific.recordpublish")));
	}
    }


    @Test
    public void hangoutDeletedNotify() throws AWTException, InterruptedException{
	LOG.info(">>>>>>>>>>>>>>>Test = participateHangout()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(10), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveHangout();
	String hangUrl = driver.getCurrentUrl();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	driver.get(hangUrl);
	hangout.takePartFreeHangout();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	driver.get(hangUrl);
	hangout.deleteHangout();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	NotifyPage notify = my.openNotify();
	Assert.assertTrue(notify.getFirstNotific().contains(DataProperties.get("notific.hangdeleted")));
    }


    @Test
    public void hangoutStarted() throws InterruptedException, AWTException{
	LOG.info(">>>>>>>>>>>>>>>Test = participateHangout()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    String hangNum = randomNumber();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		    getTodayData(), timeFuture(3), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	    create.saveHangout();
	    String hangUrl = driver.getCurrentUrl();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	    driver.get(hangUrl);
	    hangout.takePartFreeHangout();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	    hangout.startHangout(hangUrl);
	    home.home();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	    NotifyPage notify = my.openNotify();
	    Assert.assertTrue(notify.getFirstNotific().contains(DataProperties.get("notific.started")));
	}
    }




}
