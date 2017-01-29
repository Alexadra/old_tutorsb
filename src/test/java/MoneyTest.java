package test.java;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;
import main.pages.BaseClass;
import main.pages.HangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MoneyPage;
import main.pages.MyPage;
import main.pages.RecordPage;
import main.pages.SearchPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MoneyTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");


    @BeforeMethod
    public void setup() throws IOException {
	LOG.info("________________________________________________________"+this.getClass()+"________________________________________________________");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws Exception	{
	LOG.info("_________________________________________@AfterMethod_________________________________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();
	home.failIfTextError();
	MyPage my = PageFactory.initElements(driver, MyPage.class);
	my.exit();
    }



    @Test
    public void payForHangout() {
	/*finds pay hangout, clicks to take part, asserts that popup is correct and closes popup.
	After that goes to money page to assert that unconfirmed payment has appeared. Deletes that payment*/
	LOG.info("_________________________________________Test = payForHangout()_________________________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	MoneyPage money = my.goToMoney();
	int moneyAvail = money.getMoneyAvail();
	home.home();
	HangoutPage hangouts = my.recommendedTab();
	String hangName = hangouts.getFirstPaidName();
	int hangPrice = hangouts.getFirstPaidPrice();
	hangouts.openFirstPaid();
	money = hangouts.takePartPayHangout();		//click any paid hangout
	if (hangPrice <= moneyAvail) {		//assert if we have enough money
	    money.assertPaymentTrue(moneyAvail, hangPrice);		//Asserts if available money in popup, hangout cost are correct.  Esc - to close popup. Dont buys hangout!
	} else if (hangPrice > moneyAvail) {
	    money.assertPaymentFalse(moneyAvail,hangPrice);		//Asserts if available money in popup and hangout cost are correct. Esc - to close popup
	}
	driver.manage().window().maximize();
	money = my.goToMoney();
	Assert.assertEquals(money.getLastPayName(), hangName);		//Assert that request for payment was added to wallet
	money.closeUnconfirmedPay();
    }


    @Test
    public void typesOfPayment() throws Exception {
	/*Goes to money page, clicks to add funds, chooses every payment type and asserts if redirect to pay site is correct*/
	try {
	    LOG.info("_________________________________________Test = typesOfPayment()_________________________________________");
	    HomePage home = PageFactory.initElements(driver, HomePage.class);
	    LoginPage login = home.loginClick();
	    MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	    MoneyPage money = my.goToMoney();
	    List <String> payTypes = Arrays.asList("WebMoney (Дол)", "WebMoney (РУБ)", "WebMoney (ЕВР)",
		    "Visa Qiwi Wallet (Россия)",  "Деньги@Mail.Ru", "MoneXy", "Веб-кошелёк ПСКБ Россия", "EasyPay", "ECO");		//add paypal and yandex when they will work !! "Liqpay" ??
	    money.addMoney();
	    money.selectPayForm("card");		//select form for payment
	    money.selectPayType("card");	//every method asserts if type is selected, and if right pay system page was opened
	    for (String type: payTypes)	{		//do the same for every method of list "payTypes"
		money.selectPayForm("system");
		money.selectPayType(type);
	    }
	} finally {
	    closePopupButt(driver);
	}
    }


    @Test
    public void payForVideo() {
	/*finds pay hangout, clicks to take part, asserts that popup is correct and closes popup.
	After that goes to money page to assert that unconfirmed payment has appeared. Deletes that payment*/
	LOG.info("_________________________________________Test = payForVideo()_________________________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	MoneyPage money = my.goToMoney();
	int moneyAvail = money.getMoneyAvail();
	home.home();
	SearchPage search = my.getSearchResult("");
	search.videoTab();
	RecordPage video = search.openFirstPaidVideo();
	int videoPrice = Integer.parseInt(video.getPrice());
	String videoName = video.getVideoTitle();
	money = video.buyVideo();		//click any paid hangout
	if (videoPrice <= moneyAvail) {		//assert if we have enough money
	    money.assertPaymentTrue(moneyAvail, videoPrice);		//Asserts if available money in popup, hangout cost are correct.  Esc - to close popup. Dont buys hangout!
	} else if (videoPrice > moneyAvail) {
	    money.assertPaymentFalse(moneyAvail,videoPrice);		//Asserts if available money in popup and hangout cost are correct. Esc - to close popup
	}
	driver.manage().window().maximize();
	money = my.goToMoney();
	Assert.assertEquals(money.getLastPayName(), videoName);		//Assert that request for payment was added to wallet
	money.closeUnconfirmedPay();
    }


}