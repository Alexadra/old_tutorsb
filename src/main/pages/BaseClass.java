package main.pages;


import java.util.Calendar;
import java.util.Formatter;
import java.util.Random;

import main.Driver.DataProperties;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;

public class BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    String lang = DataProperties.get("language");
    String url = DataProperties.get("url");


    //========================================================LOCATORS==========================================================================
    By selectedLangLink = By.cssSelector(".button.js_main_lang");
    By popupClose = By.xpath("//div[contains(@class,'popup_open')]//a[contains(@class,'popup__close')]");
    By headerProfile = By.cssSelector(".header__profile");
    By selectedLangFooter = By.xpath("//div[@class='select-lang']//li[@class='selected']/a");
    By userNameMenu = By.xpath("//li[@class='menu-left-li menu-left-li_profile']//*[@class='userpic-user__name']");
    By menuIcon = By.cssSelector("#js__menuTrigger");
    By threeDots = By.cssSelector(".button-dropdown.button-dropdown_type_dots a");
    By exitLink = By.xpath("//span[@data-logout]");

    By settingsTab = By.cssSelector(".menu-left-li.menu-left-li_settings a");
    //========================================================END LOCATORS==========================================================================


    /******************************************  MENU AND EXIT  ******************************************/

    public void openMenu(WebDriver driver){
	if (!isMenuOpen(driver))
	    driver.findElement(menuIcon).click();
    }


    public void closeMenu(WebDriver driver){
	if (isMenuOpen(driver))
	    driver.findElement(menuIcon).click();
    }


    public Boolean isMenuOpen(WebDriver driver){
	return driver.findElement(userNameMenu).isDisplayed();
    }


    public String getMyNameMenu(WebDriver driver){
	openMenu(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(userNameMenu));
	return driver.findElement(userNameMenu).getText();
    }


    public void exit(WebDriver driver){
	openMenu(driver);
	openThreeDots(driver);
	driver.findElement(exitLink).click();
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Tutorsband"));
    }


    public void openThreeDots(WebDriver driver){
	driver.findElement(threeDots).click();
    }

    /******************************************  MENU TABS  ******************************************/

    public SettingsPage openSettings(WebDriver driver) {
	openMenu(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(settingsTab));
	driver.findElement(settingsTab).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, SettingsPage.class);
    }


    /******************************************  ERROR MESSAGES  ******************************************/

    public void write(String anything){
	System.out.println(anything);		//im too lazy too write this every time
    }


    public void write(){
	write("");
    }


    public String randomEmail() {
	String [] arr = {"a", "b", "c", "d", "e", "f","h","g","j","k","l","m","o"};
	Random randomStr = new Random();
	int arrNum = randomStr.nextInt(arr.length);
	String emailNum = 100 + randomNumber();
	write(arr[arrNum] + emailNum + "@u.u");
	return arr[arrNum] + emailNum + "@u.u";
    }


    public String randomNumber() {
	Random randomNum = new Random();
	return "a"+100+randomNum.nextInt(1000);
    }


    public String timeFuture(int i) {
	String timePlus = LocalTime.now().plusMinutes(i).toString();
	return timePlus.substring(0,5);
    }


    public String timePast() {
	String timePlus = LocalTime.now().minusMinutes(5).toString();
	return timePlus.substring(0,5);
    }


    public String getTodayData(){ //получаем сегодняшнюю дату в формате */*/*
	Formatter f = new Formatter();
	Calendar cal = Calendar.getInstance();
	f.format("%td/%tm/%tY", cal,cal,cal);
	String date = f.toString();
	f.close();
	return date;
    }


    public String getYesterday() {
	String dateMinus = LocalDate.now().minusDays(1).toString("dd/MM/yyyy");
	return dateMinus;
    }


    public void waitForPageLoaded(WebDriver driver) {
	ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		LOG.info("------------------------waitForPageLoaded "+
			((JavascriptExecutor) driver).executeScript("if (window.jQuery.active>0||window.jQuery==undefined) return false; else return true;"));
		return (Boolean) ((JavascriptExecutor) driver).executeScript("if (window.jQuery.active>0||window.jQuery==undefined) return false; else return true;"); //function codeAddress() { alert('ok');  } window.onload = codeAddress; codeAddress();
	    }
	};
	Wait <WebDriver> wait = new WebDriverWait(driver,5);
	try {
	    wait.until(expectation);
	} catch (Throwable error) {
	    LOG.info( error.getMessage() );
	}
    }


    public void selectLang(String lang, WebDriver driver ) {
	if(!driver.findElement(selectedLangLink).getText().equals(lang)){
	    write(driver.findElement(selectedLangLink).getText());
	    write(lang);
	    new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(selectedLangLink));
	    driver.findElement(selectedLangLink).click();		//click to open language list
	    waitForPageLoaded(driver);
	    driver.findElement(By.xpath("//div[@class='dropdown-list dropdown-list_right']/a[text()='"+lang+"']")).click();		//click to choose lang
	    waitForPageLoaded(driver);
	}
    }


    public void checkLangLogged(WebDriver driver) {
	Cookie cookie = driver.manage().getCookieNamed("lang");
	String cookieLang;
	switch(lang){
	case "Русский": cookieLang = "ru"; break;
	case "Українська": cookieLang = "uk"; break;
	case "Français": cookieLang = "fr"; break;
	default: cookieLang = "en";
	}
	if(!cookie.getValue().equals(cookieLang)){
	    LOG.info("User had wrong lang. Change to " + lang);
	    SettingsPage sett = openSettings(driver);		//click to open language list
	    waitForPageLoaded(driver);
	    sett.changeLanguage(lang);			//click to choose lang
	    waitForPageLoaded(driver);
	    driver.navigate().back();
	}
    }


    public void checkUnloginned(WebDriver driver){
	if (driver.findElements(headerProfile).size()!=0){
	    exit(driver);
	}
    }


    public void fileInputHidden(By locator, String file, WebDriver driver) {
	String js = " arguments[0].style.visibility='visible';";		//make input visible
	((JavascriptExecutor) driver).executeScript(js, driver.findElement(locator));		// execute javascript
	driver.findElement(locator).sendKeys(file);	//insert file into input
	String js2 = "arguments[0].style.visibility='hidden';";		//make input hidden back (is it need?)
	((JavascriptExecutor) driver).executeScript(js2, driver.findElement(locator));		//execute javascript
    }


    public void closePopupButt(WebDriver driver){
	waitForPageLoaded(driver);
	driver.findElement(popupClose).click();
	waitForPageLoaded(driver);
    }














}
