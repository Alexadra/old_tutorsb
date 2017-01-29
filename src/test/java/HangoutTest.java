package test.java;


import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.CreateHangoutPage;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Driver.DataProperties;
import  main.Driver.WebDriverFactory;

//10.06.2013 All passed. . DELETE doesnt work

public class HangoutTest extends BaseClass {
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
	LOG.info("_______________________@AfterMethod_______________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	exit(driver);
    }


    /******************************************  PARTICIPATION  *****************************************/

    //unlogined participation in LogTest


    @Test
    public void participateFreeHangout() {
	LOG.info("_______________________Test = participateHangout()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstFreeName();
	hangouts.openFirstFreeActive();
	hangouts.takePartFreeHangout();
	home.home();
	hangouts.assertHangSaved(hangName);
	hangouts.openHangoutByName(hangName);
	hangouts.assertUserTakes(DataProperties.get("user1.name"));
    }


    /******************************************  CREATION  ******************************************/


    @Test
    public void freeHangoutCreation()  throws Exception {
	//creates free hangout, asserts that it was created correctly and finally deletes it
	LOG.info("_______________________Test = freeHangoutCreation()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	my.avoidNotifyDisturb();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(20),"", "Bogota");
	create.saveHangout();
	new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Трансляция \"Free hangout "+hangNum+"\""));
	home.home();
	Assert.assertTrue(hangouts.hangPrice(DataProperties.get("freehang.name")+hangNum).contains("Бесплатно"),
		"doesnt contains Бесплатно"); //assert that created hangout is in this list and that it is free
	hangouts.openHangoutByName(DataProperties.get("freehang.name")+hangNum);
	Assert.assertEquals(hangouts.deleteHangoutStatus(DataProperties.get("freehang.name")+hangNum), DataProperties.get("hang.canceled")); //удаление встречи и проверка, что её статус стал Отменен
	waitForPageLoaded(driver);
    }


    @Test
    public void payHangoutCreation()  throws Exception {
	//creates pay hangout, asserts that it was created correctly and finally deletes it
	LOG.info("_______________________Test = payHangoutCreation()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	my.avoidNotifyDisturb();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangouts = create.hangoutData
		("pay", DataProperties.get("payhang.name")+hangNum, DataProperties.get("payhang.text"),
			getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"), DataProperties.get("hangout.price"));
	create.saveHangout();
	new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Трансляция \"Paid hangout "+hangNum+"\""));
	home.home();	//go back to the hangout list
	Assert.assertTrue(hangouts.hangPrice(DataProperties.get("payhang.name")+hangNum).contains(DataProperties.get("hangout.price")+" USD"),"price is not correct");
	hangouts.openHangoutByName(DataProperties.get("payhang.name")+hangNum);
	Assert.assertEquals(hangouts.deleteHangoutStatus(DataProperties.get("payhang.name")+hangNum), DataProperties.get("hang.canceled"));
    }


    @Test
    public void invalidCreation() throws AWTException, InterruptedException {
	LOG.info("_______________________Test = invalidCreation()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	my.avoidNotifyDisturb();
	CreateHangoutPage create = my.createHangout();
	create.hangoutInvalidData("free", null, "Описание видеовстречи", getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"), "");
	create.assertTitleInputError();
	driver.navigate().refresh();
	/*create.hangoutInvalidData("free", "Видеовстреча закрытая ", null, getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"), "");
	Assert.assertEquals(create.getErrorText("hangout_text"), DataProperties.get("error.hangText"));
	driver.navigate().refresh();*/
	create.hangoutInvalidData("free", "Видеовстреча закрытая ", "Описание видеовстречи", getTodayData(), timePast(), DataProperties.get("sett.interest"), DataProperties.path("file.name"), "");
	Assert.assertEquals(create.getTimeError(), DataProperties.get("error.hangTime"));
	driver.navigate().refresh();
	create.setImage(DataProperties.path("notPic"));
	Assert.assertEquals(create.getImageError(), DataProperties.get("errCreate.wrongImage"));
	driver.navigate().refresh();
	create.setImage(DataProperties.path("small.file"));
	Assert.assertEquals(create.getImageError(), DataProperties.get("errCreate.smallImage"));
    }


    @Test
    public void hangoutPriceAsserts() throws InterruptedException {
	LOG.info("_______________________Test = invalidCreation_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	my.avoidNotifyDisturb();
	CreateHangoutPage create = my.createHangout();
	create.setPayMode("0");
	create.setPayMode("yw@");
	create.setPayMode("50");
	create.setPayMode("99999");
	create.setPayMode("0,1");
	create.setPayMode("01");
    }


    /******************************************  PRIVATE CREATION  ******************************************/


    /*    @Test
    public void privateHangoutCreation()  throws Exception {
	//creates free hangout, asserts that it was created correctly and finally deletes it
	LOG.info("_______________________Test = freeHangoutCreation()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	my.avoidNotifyDisturb();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.privateMode();
	create.invitePrivUser("Petrikov Vasilii");
	Thread.sleep(3000);
	create.invitePrivEmail("testuser5@ru.ru");
	Thread.sleep(3000);
	create.saveHangout();
	new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Видеовстреча \"Free hangout "+hangNum+"\""));
	home.home();
	Assert.assertTrue(hangouts.hangPrice(DataProperties.get("freehang.name")+hangNum).contains("Бесплатно"),
		hangouts.hangPrice(DataProperties.get("freehang.name")+hangNum)+"doesnt contains Бесплатно"); //assert that created hangout is in this list and that it is free
	hangouts.openHangoutByName(DataProperties.get("freehang.name")+hangNum);
	Assert.assertEquals(hangouts.deleteHangoutStatus(DataProperties.get("freehang.name")+hangNum), DataProperties.get("hang.canceled")); //удаление встречи и проверка, что её статус стал Отменен
	     }
     */


    /******************************************  EDITING  ******************************************/


    @Test
    public void editHangoutNoChange() throws AWTException, InterruptedException{
	/*create hangout, click edit , change nothing and save. Then assert if title,
	  description, tags and image didnt changed*/
	LOG.info("_______________________Test = editHangoutNoChange_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    my.avoidNotifyDisturb();
	    String hangNum = randomNumber();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum,
		    DataProperties.get("freehang.text"), getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	    create.saveHangout();
	    create = hangouts.editClick();
	    hangouts = create.saveHangout();
	    driver.manage().window().maximize();
	    Assert.assertTrue(hangouts.getHangName().contains(DataProperties.get("freehang.name")+hangNum));
	    Assert.assertEquals(hangouts.getDescript(), DataProperties.get("freehang.text"));
	    Assert.assertEquals(hangouts.getTag(), DataProperties.get("sett.interest"));
	    Assert.assertTrue(hangouts.isImageExists());
	}
    }


    @Test
    public void editHangoutChangeAll() throws AWTException, InterruptedException{
	/*create hangout, click edit , change nothing and save. Then assert if title,
	  description, tags and image didnt changed*/
	LOG.info("_______________________Test = editHangoutNoChange_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	if (!url.contains("tutorsband")){
	    String hangNum = randomNumber();
	    my.avoidNotifyDisturb();
	    CreateHangoutPage create = my.createHangout();
	    HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum,
		    DataProperties.get("freehang.text"), getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), "", DataProperties.get("timezone"));
	    create.saveHangout();
	    create = hangouts.editClick();
	    create.hangoutEditData ("title", "text", "interest", "");
	    waitForPageLoaded(driver);
	    Assert.assertTrue(hangouts.getHangName().contains("title"),hangouts.getHangName()+"doesnt contain title");
	    Assert.assertEquals(hangouts.getDescript(), "text");
	    Assert.assertEquals(hangouts.getTag(), "interest");
	    Assert.assertFalse(hangouts.isImageExists(),"Picture wasnt deleted");
	}
    }


    /******************************************  SHARING  ******************************************/


    @Test
    public void shareFacebook() throws InterruptedException{
	/*Open recommended hangouts. Open first hangout. Press facebook share button.
	 * Assert that new window contains hangout name.*/
	LOG.info("_______________________Test = shareFacebook_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstName();
	hangouts.openHangoutByNum(1);
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareFaceb());
	home.facebooklogin(DataProperties.get("valid.loginF"), DataProperties.get("valid.passwordFb"));
	Assert.assertTrue(hangouts.getHangoutNameFaceb().contains(hangName), hangouts.getHangoutNameFaceb()+"doesnt contain "+hangName);
	driver.close();
	driver.switchTo().window(homePage);
    }


    @Test
    public void shareGmail() throws InterruptedException{
	/*Open recommended hangouts. Open first hangout. Press gmail share button.
	 * Assert that new window contains hangout name.*/
	LOG.info("_______________________Test = shareGmail_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstName();
	hangouts.openHangoutByNum(1);
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareGoog());
	home.googlelogin(DataProperties.get("valid.loginG"), DataProperties.get("valid.passwordG"));
	if (!url.contains("testun")) Assert.assertTrue(hangouts.getHangoutNameGoog().contains(hangName));
	driver.close();
	driver.switchTo().window(homePage);
    }


    @Test
    public void shareTwitter(){
	/*Open recommended hangouts. Open first hangout. Press twitter share button.
	 * Assert that new window contains hangout name.*/
	LOG.info("_______________________Test = shareTwitter_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstName();
	hangouts.openHangoutByNum(1);
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareTwit());
	Assert.assertTrue(hangouts.getHangoutNameTwit().contains(hangName));
	driver.close();
	driver.switchTo().window(homePage);
    }


    @Test
    public void shareVk(){
	/*Open recommended hangouts. Open first hangout. Press vk share button.
	 * Assert that new window contains hangout name.*/
	LOG.info("_______________________Test = shareVk_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstName();
	hangouts.openHangoutByNum(1);
	String homePage = driver.getWindowHandle();
	home.switchToNewWindow(hangouts.shareVk());
	home.vkLogin(DataProperties.get("valid.loginVk"), DataProperties.get("valid.passwordVk"));
	Assert.assertTrue(hangouts.getHangoutNameVk().contains(hangName));
	driver.close();
	driver.switchTo().window(homePage);
    }


    /******************************************  STATUS  ******************************************/


    @Test
    public void statusStartTimeAndDelete()  throws Exception {
	/*create free hangout , that should be stasted in 4 minutes. Start button must be already there.
	 * Start hangout and assert that its status is "On air" */
	LOG.info("_______________________________Test = statusStartTimeAndDelete()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();
	HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(4), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveHangout();
	Assert.assertTrue(hangouts.getHangoutStatus().contains(DataProperties.get("status.start")),
		hangouts.getHangoutStatus()+"doesnt contain "+ DataProperties.get("status.start"));
	Assert.assertEquals(hangouts.deleteHangoutStatus(DataProperties.get("freehang.name")+hangNum), DataProperties.get("hang.canceled"));
    }


    @Test
    public void statusWaitTimer()  throws Exception {
	/*create free hangout , that should be stasted in 4 minutes. Start button must be already there.
	 * Start hangout and assert that its status is "On air" */
	LOG.info("_______________________________Test = statusWaitTimer()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));	//login data
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();	//click on button Create hangout
	HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(5), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveHangout();
	String hangUrl = driver.getCurrentUrl();
	exit(driver);
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	driver.get(hangUrl);
	Assert.assertTrue(hangouts.getHangoutStatus().contains(DataProperties.get("status.wait")),
		hangouts.getHangoutStatus()+"doesnt contain "+ DataProperties.get("status.wait") );
    }


    @Test
    public void statusOnAir()  throws Exception {
	/*create free hangout , that should be stasted in 4 minutes. Start button must be already there.
	 * Start hangout and assert that its status is "On air" */
	LOG.info("_______________________________Test = statusOnAir()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));	//login data
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();	//click on button Create hangout
	HangoutPage hangouts = create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(5), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveStartHangout();
	String hangUrl = driver.getCurrentUrl();
	driver.navigate().back();
	exit(driver);
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	driver.get(hangUrl);
	Assert.assertTrue(hangouts.getHangoutStatus().contains(DataProperties.get("status.onAir")),
		hangouts.getHangoutStatus()+"doesnt contain "+ DataProperties.get("status.onAir"));
	hangouts.takePartFreeHangout();
	waitForPageLoaded(driver);
	Assert.assertTrue(hangouts.getHangoutStatus().contains(DataProperties.get("status.join")),
		hangouts.getHangoutStatus()+"doesnt contain "+ DataProperties.get("status.join"));
    }


    @Test
    public void timezonePopup(){
	LOG.info("_______________________________Test = timezonePopup()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login3"), DataProperties.get("valid.password"));
	HangoutPage hangouts = my.recommendedTab();
	hangouts.openHangoutByNum(1);
	Assert.assertTrue(hangouts.getTimePopupText().contains(DataProperties.get("hang.timezone")));
	closePopupButt(driver);
    }




}
