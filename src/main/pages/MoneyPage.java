package main.pages;



import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import test.java.LogTest;

public class MoneyPage extends BaseClass {

    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;
    public MoneyPage(WebDriver driver) {
	this.driver = driver;
    }


    //========================================================LOCATORS==========================================================================
    //=================select pay type
    By selectPay = By.cssSelector(".pay-select");
    By cardForm = By.xpath("//div[@class='pay-select pay-select_list']/a[1]");
    By systemForm = By.xpath("//div[@class='pay-select pay-select_list']/a[2]");


    By lastPay = By.xpath("//*[@id='tm_history_table']//tr[2]/td[4]//a");
    By moneyAvail = By.xpath("//table[@class='table_payment_money']//tr[1]/td[2]/span[2]");
    By moneyAvPopup = By.xpath("//table[@class='table_pay']//tr[3]/td[2]/div");
    By hangCostPopup = By.xpath("//table[@class='table_pay']//tr[2]/td[2]/div");
    By payButton = By.xpath("//table[@class='table_pay']/following::a[1]");

    By payPopup = By.cssSelector(".table_pay");
    By addMoneyLoc = By.cssSelector(".button.button_primary.pay_emoney_js");
    By inputMoney = By.xpath("//div[@class='money-wrap']//input");
    By submitMoney = By.xpath("//div[@class='buttons_list clearfix']/a[1]");
    By moneyFormBack = By.xpath("//div[@class='buttons_list clearfix']/a[2]");
    By closeUnconfPay = By.cssSelector(".line__right.tm_decline_js.js_tooltip");
    By silverPricingUnlogged = By.xpath("(//a[@class='button button_primary buy_proacc_js js_afterlogin js_this_signup'])[1]");
    By silverPricingUnloggedSecond = By.xpath("(//a[@class='button button_primary buy_proacc_js js_afterlogin js_this_signup'])[2]");
    By goldPricingUnlogged = By.xpath("(//a[@class='button button_primary buy_proacc_js js_afterlogin js_this_signup'])[3]");
    By goldPricingUnloggedSecond = By.xpath("(//a[@class='button button_primary buy_proacc_js js_afterlogin js_this_signup'])[4]");
    By enterpPricingUnlogged = By.xpath("(//a[@id='login_chat'])[1]");
    By enterpPricingUnloggedSecond = By.xpath("(//a[@id='login_chat'])[2]");
    By freePricingUnlogged = By.xpath("(//a[@class='button button_primary js_this_signup'])[1]");
    By freePricingUnloggedSecond = By.xpath("(//a[@class='button button_primary js_this_signup'])[2]");

    //========================================================END LOCATORS======================================================================



    public String getLastPayName() {
	return driver.findElement(lastPay).getText();
    }


    public int getMoneyAvail() {
	return Integer.parseInt(driver.findElement(moneyAvail).getText().trim());
    }


    public void assertPaymentTrue(int moneyAvail, int hangPrice) {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(moneyAvPopup));
	int moneyPopup = Integer.parseInt(driver.findElement(moneyAvPopup).getText().replaceAll("\\D*", "").trim());		//get available money
	Assert.assertEquals(moneyPopup, moneyAvail);
	int pricePopup = Integer.parseInt(driver.findElement(hangCostPopup).getText().replaceAll("\\D*", "").trim());
	Assert.assertEquals(pricePopup, hangPrice);
	driver.findElement(selectPay).sendKeys(Keys.ESCAPE);
    }


    public void assertPaymentFalse(int moneyAvail, int hangPrice) {
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(moneyAvPopup));
	int moneyPopup = Integer.parseInt(driver.findElement(moneyAvPopup).getText().replaceAll("\\D*", "").trim());		//get available money
	Assert.assertEquals(moneyPopup, moneyAvail);
	int pricePopup = Integer.parseInt(driver.findElement(hangCostPopup).getText().replaceAll("\\D*", "").trim());
	Assert.assertEquals(pricePopup, hangPrice);
	driver.findElement(selectPay).sendKeys(Keys.ESCAPE);
    }


    public void addMoney() {
	driver.findElement(addMoneyLoc).click();
    }


    public void selectPayForm(String form) {
	if (form.equals("card")) {		//if we use card pay
	    new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(cardForm));
	    driver.findElement(cardForm).click();
	} else if (form.equals("system")) {		//if we use payment system
	    new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(systemForm));
	    driver.findElement(systemForm).click();
	} else if (form.equals("terminal")) {		//if we use terminal
	    new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(cardForm));
	}
    }


    public void selectPayType(String type) throws InterruptedException {
	LOG.info("________________________The type of payment system is - "+type);
	String homePage = driver.getWindowHandle();			//remember main page handle, to go to it later
	if (type.contains("card")) {			//card type has different path to link
	    //do nothing
	} else {
	    new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@*='"+type+"']"))).click();
	    Assert.assertEquals(driver.findElement(By.xpath("//a[@*='"+type+"']")).getAttribute("class"), "pay-method js_tooltip pay-method_active");
	}
	driver.findElement(inputMoney).sendKeys("");
	driver.findElement(inputMoney).sendKeys(Keys.BACK_SPACE,Keys.BACK_SPACE);		//clear money input . simple clear method doesnt work
	driver.findElement(inputMoney).sendKeys("50");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	homePage = driver.getWindowHandle();			//remember main page handle
	home.switchToNewWindow(driver.findElement(submitMoney));
	waitForPayTitle(type);
	driver.close();
	driver.switchTo().window(homePage);
    }



    public void waitForPayTitle(String type){
	switch (type) {
	case "card": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("платежной карточки")); break;
	case "WebMoney (Дол)": new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Merchant WebMoney Transfer")); break;
	case "WebMoney (РУБ)": new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Merchant WebMoney Transfer")); break;
	case "WebMoney (ЕВР)": new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Merchant WebMoney Transfer")); break;
	case  "Visa Qiwi Wallet (Россия)": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("QIWI")); break;
	case "PayPal": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("PayPal")); break;
	case "Liqpay": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("LiqPAY")); break;
	case "Деньги@Mail.Ru": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("")); break;
	case "MoneXy": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("MoneXy")); break;
	case "Веб-кошелёк ПСКБ Россия": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("PSCB"));  break;
	case "EasyPay": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("")); break;
	case "ECO": new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("ecoPayz")); break;
	}
    }


    public void backToMoneyForm() {
	driver.findElement(moneyFormBack).click();
    }

    public void assertMoneyPage(){
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(addMoneyLoc));
    }

    public void closeUnconfirmedPay(){		//close first unconfirmed payment
	driver.findElement(closeUnconfPay).click();
	waitForPageLoaded(driver);
    }


    public void assertPayPopup(){
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(payPopup));
    }

    /******************************************  PRICING  ******************************************/


    public void buyFreePricingUnlogged(){
	driver.findElement(freePricingUnlogged).click();
	waitForPageLoaded(driver);
    }


    public void buyFreePricingUnlogBottom(){
	driver.findElement(freePricingUnloggedSecond).click();
	waitForPageLoaded(driver);
    }


    public void buySilverPricingUnlogged(){
	driver.findElement(silverPricingUnlogged).click();
	waitForPageLoaded(driver);
    }


    public void buySilverPricingUnlogBottom(){
	driver.findElement(silverPricingUnloggedSecond).click();
	waitForPageLoaded(driver);
    }


    public void buyGoldPricingUnlogged(){
	driver.findElement(goldPricingUnlogged).click();
	waitForPageLoaded(driver);
    }


    public void buyGoldPricingUnlogBottom(){
	driver.findElement(goldPricingUnloggedSecond).click();
	waitForPageLoaded(driver);
    }


    public void buyEnterpPricingUnlogged(){
	driver.findElement(enterpPricingUnlogged).click();
	waitForPageLoaded(driver);
    }


    public void buyEnterpPricingUnlogBottom(){
	driver.findElement(enterpPricingUnloggedSecond).click();
	waitForPageLoaded(driver);
    }



}

