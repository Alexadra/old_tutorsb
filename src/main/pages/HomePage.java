package main.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.java.LogTest;
import main.Driver.DataProperties;



public class HomePage extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;
    public String errorText = null;
    String url = DataProperties.get("url");

    public HomePage (WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    By regName = By.id("register_name");
    By regEmail = By.id("register_email");
    By regPass = By.id("register_password");
    By regSubmit = By.id("register_button");
    By logEmail = By.id("login_email");
    By interestLoc1 = By.xpath("//div[@class='theme-select__row'][1]/a[@class='theme-select__item'][1]");
    By interestLoc2 = By.xpath("//div[@class='theme-select__row'][2]/a[@class='theme-select__item'][1]");
    By interestLoc3 = By.xpath("//div[@class='theme-select__row'][3]/a[@class='theme-select__item'][1]");
    By submitIntLoc = By.xpath("//div[@class='theme-select clearfix']/following::div[1]/a");
    By headerProfile = By.cssSelector(".header__profile");
    By headerSignin = By.id("js_header_signin");

    By userNameLoc = By.cssSelector(".user-big__name");

    By moneyIconLoc = By.xpath("");
    By moneyHistory = By.cssSelector(".table_payment_history");

    By imageEditLoc = By.cssSelector(".photo_edit");
    By popup = By.cssSelector(".popup__wrap");
    By userName = By.cssSelector(".user-big__name");
    //========================OTHER SERVICES==============================
    By facebookMenu = By.id("navAccountLink");
    By facebookExit = By.cssSelector("#logout_form label input");
    By facebookCheckBox = By.cssSelector("#persist_box");
    By facebookConfirm = By.xpath("//*[@name='__CONFIRM__']");
    By fbFriendsIframe = By.xpath("//iframe[@class='FB_UI_Dialog']");
    By fbCancelInv = By.xpath("//*[@type='submit']");
    By fbInviteChecks = By.cssSelector(".multiColumnCheckable.checkableListItem");
    By googleMenu = By.id("gbg4");
    By googleEmail = By.id("Email");
    By googlePass = By.id("Passwd");
    By googleSubmit = By.id("signIn");
    By googleExit = By.id("gb_71");
    By accessButG = By.id("submit_approve_access");
    By landingSingUp = By.cssSelector(".button.button_landing.button_big.js_no_link.js_body_signup");
    By linkdLoginGo = By.xpath("//p[@id='register-custom-nav']/a");
    By vkEmail = By.id("email");
    By vkPass = By.id("pass");
    By vkSubmit = By.id("login_button");
    //========================================================END LOCATORS=======================================================================


    /******************************************  HOME LINKS  ******************************************/


    public void home() {
	driver.get(url);
	waitForPageLoaded(driver);
    }


    public LoginPage loginClick() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(headerSignin));
	driver.findElement(headerSignin).click();
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(logEmail));
	return PageFactory.initElements(driver, LoginPage.class);
    }


    public RegisterPage registerClick() {
	waitForPageLoaded(driver);
	driver.findElement(By.id("js_content_signcreate")).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(regEmail));
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, RegisterPage.class);
    }


    public CreateHangoutPage unloginedCreateHang(){
	driver.findElement(By.id("js_header_singcreate")).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(regEmail));
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, CreateHangoutPage.class);
    }


    public MoneyPage goToMoney() {
	driver.findElement(moneyIconLoc).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(moneyHistory));
	return PageFactory.initElements(driver, MoneyPage.class);
    }


    public void landingConnect() {
	driver.manage().window().maximize();
	driver.findElement(landingSingUp).click();
    }


    /******************************************  OTHER SERVICES  ******************************************/


    public void vkLogin(String email, String pass){
	driver.switchTo().frame(driver.findElement(By.cssSelector("iframe")));
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(vkEmail));
	if (driver.findElements(vkEmail).size()==1){
	    driver.findElement(vkEmail).clear();
	    driver.findElement(vkEmail).sendKeys(email);
	    driver.findElement(vkPass).clear();
	    driver.findElement(vkPass).sendKeys(pass);
	    driver.findElement(vkSubmit).click();
	}
    }


    public void facebooklogin(String email, String pass) throws InterruptedException {
	if (driver.findElements(By.id("email")).size()==1 ) {
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys(email);
	    driver.findElement(By.id("pass")).clear();
	    driver.findElement(By.id("pass")).sendKeys(pass);
	    driver.findElement(By.cssSelector("#loginbutton input")).click();
	    Thread.sleep(1500);	//нельзя так делать !
	}
    }


    public void facebookloginInvite(String email, String pass) throws InterruptedException {
	if (driver.findElements(By.id("email")).size()==1 ) {
	    driver.findElement(By.id("email")).clear();
	    driver.findElement(By.id("email")).sendKeys(email);
	    driver.findElement(By.id("pass")).clear();
	    driver.findElement(By.id("pass")).sendKeys(pass);
	    write(driver.getTitle());
	    write(driver.getCurrentUrl());
	    switchToNewWindow(facebookSubmit());
	    write(driver.getTitle());
	    write(driver.getCurrentUrl());
	}
    }


    public WebElement facebookSubmit(){
	return driver.findElement(By.cssSelector("#u_0_1"));
    }


    public void facebookConfirm(){
	if (driver.getTitle().contains("acebook")) {
	    if (driver.findElements(facebookConfirm).size()==1) driver.findElement(facebookConfirm).click();
	}
    }


    public void fbCancelInvite() throws InterruptedException{
	Thread.sleep(1500);
	new WebDriverWait(driver,10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(driver.findElement(By.cssSelector(".FB_UI_Dialog")).getAttribute("name")));
	System.out.println("Will click");
	driver.findElement(fbCancelInv).click();
    }


    public void facebookExit() {
	try {
	    if (driver.findElement(facebookMenu).isDisplayed()){
		driver.findElement(facebookMenu).click();
		driver.findElement(facebookExit).click();
	    }
	} catch(StaleElementReferenceException s){}
    }


    public void googlelogin(String email, String pass) throws InterruptedException{
	if (driver.findElements(By.id("reauthEmail")).size()>0){
	    driver.manage().deleteAllCookies();
	    driver.navigate().refresh();
	}
	driver.findElement(googlePass).clear();
	driver.findElement(googlePass).sendKeys(pass);
	driver.findElement(googleEmail).clear();
	driver.findElement(googleEmail).sendKeys(email);
	driver.findElement(googleSubmit).click();
	Thread.sleep(500);	//нельзя так делать !
    }


    public void googleConfirm(){
	if (driver.getCurrentUrl().contains("google")){
	    if (driver.findElements(accessButG).size()==1){
		driver.findElement(accessButG).click();
	    }
	}
    }


    public void googleExit() {
	try {
	    if (driver.findElement(googleMenu).isDisplayed()) {
		driver.findElement(googleMenu).click();
		driver.findElement(googleExit).click();
	    }
	} catch(StaleElementReferenceException s){}
    }


    /******************************************  GENERAL METHODS  ******************************************/


    public void findTextError() throws Exception {
	//метод проверяет, содержит ли страница *** - что означает, что текстовая переменная не задана
	String errorPlace = null;
	String pageSource = driver.getPageSource();
	while (pageSource.contains("TBM")) {
	    Pattern patternError = Pattern.compile(".{25}TBM.{25}");	//find where text missing was found
	    Matcher matcherError = patternError.matcher(pageSource);
	    if (matcherError.find()) {
		errorPlace = matcherError.group();
	    }
	    LOG.error("------------------------> Ошибка на странице:\""+driver.getTitle()+"\".Текст переменной:"+errorPlace); //AssertionError

	    //return errorText = "Ошибка текстовой переменной найдена" ;
	    pageSource = pageSource.split("TBM", 2)[1];
	}
    }


    public void failIfTextError() throws AssertionError, Exception {
	// метод валит тест, если на странице ошибка (смотри метод findKeyError())
	if (findTextError() != null) {
	    throw new AssertionError(this.errorText);
	}
    }


    public void moveToByLocator(By locator) throws AWTException {
	//moves to element by its location. ROBOT . Don't use mouse.
	driver.manage().window().maximize();
	Robot robot = new Robot();
	Point coordinates = driver.findElement(locator).getLocation();
	robot.mouseMove(coordinates.getX(),coordinates.getY()+130);
    }


    public void switchToNewWindow(WebElement link) {
	final Set <String> oldWindowsSet = driver.getWindowHandles();
	write("Page title:" + driver.getTitle());
	link.click();
	final String newWindow = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<String>() {
	    @Override
	    public String apply(WebDriver driver) {
		Set<String> newWindowsSet = driver.getWindowHandles();
		newWindowsSet.removeAll(oldWindowsSet);
		return newWindowsSet.size() > 0 ? newWindowsSet.iterator().next() : null;
	    }
	});
	driver.switchTo().window(newWindow);
	new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		return driver.getWindowHandle().equals(newWindow);
	    }
	});
	write("Page title:" + driver.getTitle());
    }


    public void jsErrorExist() throws InterruptedException {
	final List <JavaScriptError> jsErrors = JavaScriptError.readErrors(driver);
	if (jsErrors.size() > 0) {
	    int i = 1;
	    for (Object jsError:jsErrors) {
		LOG.error("----------------------------------------------------------------------js error----------------------------------------------------------------------");
		LOG.error("javascript error "+i+"."+jsError.toString());
		LOG.error("----------------------------------------------------------------------js error----------------------------------------------------------------------");
		i++;
	    }
	    return;
	}
	else return;
    }






}
