package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;
import main.pages.BaseClass;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.WebinarPage;

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

public class WebinarTest extends BaseClass {
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
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	exit(driver);
    }


    @AfterClass
    public void closeDriver(){
	LOG.info(">>>>>>>>>>@AfterClass<<<<<<<<<");
	WebDriverFactory.dismissDriver();
    }


    /******************************************  PRESENTATION  ******************************************/


    @Test
    public void uploadPresentation() throws Exception {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("pdf");
	webinar.assertFirstFileLoaded("pdf");
	webinar.uploadFile("docx");
	webinar.waitForFileinList("docx");
	webinar.uploadFile("txt");
	webinar.waitForFileinList("txt");
	webinar.uploadFile("odt");
	webinar.waitForFileinList("odt");
	webinar.uploadFile("rtf");
	webinar.waitForFileinList("rtf");
	webinar.uploadFile("png");
	webinar.waitForFileinList("png");
	webinar.uploadFile("jpg");
	webinar.waitForFileinList("jpg");
	webinar.uploadFile("ppt");
	webinar.waitForFileinList("ppt");
	webinar.uploadFile("pptx");
	webinar.waitForFileinList("pptx");
	webinar.uploadFile("odp");
	webinar.waitForFileinList("odp");
	webinar.uploadFile("odg");
	webinar.waitForFileinList("odg");
	webinar.uploadFile("xlsx");
	webinar.waitForFileinList("xlsx");
	webinar.uploadFile("ods");
	webinar.waitForFileinList("ods");
	webinar.finishHangout();
    }


    @Test
    public void zdeletePresentation() throws Exception {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("pdf");
	webinar.assertFirstFileLoaded("pdf");
	webinar.deleteFile("pdf");
	webinar.uploadFirstFile("pdf");
	webinar.assertFirstFileLoaded("pdf");
	webinar.uploadFile("docx");
	webinar.waitForFileinList("docx");
	webinar.deleteFile("docx");
	webinar.deleteFile("pdf");
	webinar.waitForCenterButtPresent();
	webinar.finishHangout();
    }


    @Test
    public void upload200PagesPresentation() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("202p.pdf");
	Assert.assertTrue(webinar.errorIconExists("202p.pdf"), "No error icon found");
	webinar.finishHangout();
    }


    @Test
    public void uploadWrongPresentation() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("djvu");
	Assert.assertTrue(webinar.errorIconExists("djvu"), "No error icon found");
	webinar.finishHangout();
    }


    @Test
    public void uploadBigPresentation() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("50MB");
	Assert.assertTrue(webinar.errorIconExists("50MB"), "No error icon found");
	webinar.finishHangout();
    }


    @Test
    public void slidesPresentation() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("pdf");
	webinar.assertFirstFileLoaded("pdf");
	webinar.clickSlidePreview(2);
	Assert.assertEquals(2, webinar.getActiveSlide());
	webinar.clickSlidePreview(1);
	Assert.assertEquals(1, webinar.getActiveSlide());
	webinar.clickNextSlide();
	Assert.assertEquals(2, webinar.getActiveSlide());
	webinar.clickPrevSlide();
	Assert.assertEquals(1, webinar.getActiveSlide());
	webinar.finishHangout();
    }


    @Test
    public void presentationScaleMove() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = uploadPresentation()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.uploadFirstFile("pdf");
	webinar.assertFirstFileLoaded("pdf");
	webinar.scalePresentationWheel();
	webinar.scalePresentation();
	Thread.sleep(2000);
	webinar.finishHangout();
    }






    /******************************************  VIDEO  ******************************************/


    @Test
    public void videoTabPermiss() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = videoTabStub()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	webinar.goToPresentationTab();
	webinar.goToVideoTab();
	webinar.hideSwf();
	webinar.finishHangout();
    }


    /* @Test
    public void videoBroadcast() throws AWTException, InterruptedException {
	LOG.info("_______________________________Test = videoTabStub()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	WebinarPage webinar = my.startDemo();
	webinar.hideSwf();
	if(!webinar.getActiveCamera().equals("null"));
	webinar.finishHangout();
    }*/


    /*@Test
    public void userNoPresentation() throws AWTException, InterruptedException{
	LOG.info(">>>>>>>>>>>>>>>Test = startDemo()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    String hangNum = randomNumber();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		    getTodayData(), timeFuture(3), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	    String hangUrl = driver.getCurrentUrl();
	    WebinarPage webinar = hangout.startHangout();
	    driver.navigate().back();
	    my.exit();
	    home.loginClick();
	    login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	    driver.get(hangUrl);
	    hangout.takePartFreeHangout();
	    hangout.startHangout();
	    Assert.assertTrue(webinar.getStubText().contains(DataProperties.get("freehang.name")+hangNum),"Stub doesn't contain hangout name");
	    webinar.finishHangout();
	    my.exit();
	}
    }*/

}
