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
import main.pages.RecordPage;
import main.pages.SearchPage;
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

public class RecordTest extends BaseClass {
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
    public void avideoProcessing() throws InterruptedException, AWTException{
	/*Create free conference with picture.Start and over it.Assert that page after conference is 'Video processing'.
	 * Assert that plitka-status of this video equals 'video processing'. Wait for proccessing ends*/
	LOG.info(">>>>>>>>>>>>>>>Test = videoProcessing()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    String hangNum = randomNumber();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangout = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		    getTodayData(), timeFuture(3), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	    create.saveHangout();
	    WebinarPage webinar = hangout.startHangout();
	    webinar.hideSwf();
	    RecordPage record = webinar.finishHangout();
	    Assert.assertEquals(DataProperties.get("record.process"),record.getStatus());
	    my.myVideos();
	    Assert.assertEquals(DataProperties.get("record.process"),record.getFirstPlitkaStatus());
	    record.waitRecordProccessed();
	}
    }


    @Test
    public void bplayVideo(){
	/*Find ready video in my videos list. Open video page. Play by pressing overlay button.
	 * Pause by pressing the same one. Do it twice. Play by pressing play button in main menu.
	 * Pause by pressing the same one.*/
	LOG.info(">>>>>>>>>>>>>>>Test = playVideo()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	RecordPage record = my.myVideos();
	record.openReadyVideo();
	record.playPauseOverlay();
	Assert.assertEquals("hidden", record.getPlayVisibility());
	record.playPauseOverlay();
	Assert.assertEquals("visible", record.getPlayVisibility());
	record.playPauseOverlay();
	Assert.assertEquals("hidden", record.getPlayVisibility());
	record.playPauseOverlay();
	Assert.assertEquals("visible", record.getPlayVisibility());
	record.playPauseMain();
	Assert.assertEquals("hidden", record.getPlayVisibility());
	record.playPauseMain();
	Assert.assertEquals("visible", record.getPlayVisibility());
    }


    @Test
    public void publishVideo(){
	/*Find unpublished video in my video list. Open it. Publish it. Assert that share elements has appeared.
	 * Assert that this video has appeared in search*/
	LOG.info(">>>>>>>>>>>>>>>Test = playVideo()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    RecordPage record = my.myVideos();
	    record.openLockedVideo();
	    String name = record.getVideoTitle();
	    record.publishRecord();
	    Assert.assertTrue(record.isSharePresent());
	    SearchPage search = new SearchPage(driver);
	    search.videoSearch(name);
	    Assert.assertEquals(name, search.getFirstResultName());
	}
    }


    @Test
    public void editVideoFree() throws InterruptedException{
	/*Open my videos. Find unpublished one. Open it.Edit title, description, pay mode (if it's not free)
	 * Assert that all changes saved properly*/
	LOG.info(">>>>>>>>>>>>>>>Test = editVideoFree()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	RecordPage record = my.myVideos();
	record.openLockedVideo();
	record.editVideoButt();
	record.editVideoData(DataProperties.get("video.title"), DataProperties.get("video.desc"), DataProperties.get("video.freemode"), 0);
	Assert.assertEquals(DataProperties.get("video.title"), record.getVideoTitle());
	Assert.assertEquals(DataProperties.get("video.desc"), record.getDescription());
	//Assert.assertEquals(DataProperties.get("video.free"), record.getPrice());
    }


    @Test
    public void editVideoPay() throws InterruptedException{
	/*Open my videos. Find unpublished one. Open it.Edit title, description, pay mode (if it's not pay), price.
	 * Assert that all changes saved properly*/
	LOG.info(">>>>>>>>>>>>>>>Test = editVideoPay()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	RecordPage record = my.myVideos();
	record.openLockedVideo();
	record.editVideoButt();
	record.editVideoData(DataProperties.get("video.title"), DataProperties.get("video.desc"), DataProperties.get("video.paymode"), 1);
	Assert.assertEquals(DataProperties.get("video.title"), record.getVideoTitle());
	Assert.assertEquals(DataProperties.get("video.desc"), record.getDescription());
	Assert.assertEquals("1", record.getPrice());
    }


    @Test
    public void editVideoNoChange() throws InterruptedException{
	/*Open my videos. Find unpublished one. Open it.Edit title, description, pay mode (if it's not free)
	 * Assert that nothing changed*/
	LOG.info(">>>>>>>>>>>>>>>Test = editVideoFree()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	RecordPage record = my.myVideos();
	record.openLockedVideo();
	String title = record.getVideoTitle();
	String description = record.getDescription();
	String price = record.getPrice();
	record.editVideoButt();
	record.saveVideo();
	Assert.assertEquals(title, record.getVideoTitle());
	Assert.assertEquals(description, record.getDescription());
	Assert.assertEquals(price, record.getPrice());
    }


    @Test
    public void shareFacebook() throws InterruptedException{
	/*Open my videos. Find published video. Press facebook share button.
	 * Assert that new window contains video name.*/
	LOG.info("_______________________Test = shareFacebook_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	HangoutPage hangouts = PageFactory.initElements(driver, HangoutPage.class);
	Thread.sleep(20000);
	RecordPage record = my.myVideos();
	record.openPublishedVideo();
	String videoName = record.getVideoTitle();
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareFaceb());
	home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordG"));
	Assert.assertTrue(hangouts.getHangoutNameFaceb().contains(videoName));
	driver.close();
	driver.switchTo().window(homePage);
    }


    @Test
    public void shareGmail() throws InterruptedException{
	/*Open my videos. Find published video. Press gmail share button.
	 * Assert that new window contains video name.*/
	LOG.info("_______________________Test = shareGmail_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	HangoutPage hangouts = PageFactory.initElements(driver, HangoutPage.class);
	RecordPage record = my.myVideos();
	record.openPublishedVideo();
	String videoName = record.getVideoTitle();
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareGoog());
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));
	if (!url.contains("testun")) Assert.assertTrue(hangouts.getHangoutNameGoog().contains(videoName));
	driver.close();
	driver.switchTo().window(homePage);
    }


    @Test
    public void shareTwitter(){
	/*Open my videos. Find published video. Press twitter share button.
	 * Assert that new window contains video name.*/
	LOG.info("_______________________Test = shareTwitter_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	HangoutPage hangouts = PageFactory.initElements(driver, HangoutPage.class);
	RecordPage record = my.myVideos();
	record.openPublishedVideo();
	String videoName = record.getVideoTitle();
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareTwit());
	Assert.assertTrue(hangouts.getHangoutNameTwit().contains(videoName));
	driver.close();
	driver.switchTo().window(homePage);
    }




    @Test
    public void emptyRecords(){
	LOG.info(">>>>>>>>>>>>>>>Test = emptyFollowing()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("clean.login"), DataProperties.get("valid.password"));
	my.myVideos();
	Assert.assertTrue(my.getEmptyText().contains(DataProperties.get("empty.record")),"Empty page must contain text about no records were found");
    }

}
