package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LoginPage extends BaseClass {

    private WebDriver driver;
    public LoginPage(WebDriver driver) {
	this.driver = driver;
    }




    //========================================================LOCATORS==========================================================================
    //=================login popup
    By logEmail = By.id("login_email");
    By logPass = By.id("login_password");
    By logSubmit = By.id("login_button");
    By facebookBut = By.cssSelector(".button.button_loginit.button_big.button_loginit_fb");
    By googleBut = By.cssSelector(".button.button_loginit.button_big.button_loginit_gp");
    By signinPopup = By.id("js_signin_popup");
    By signupPopup = By.id("js_signup_popup");
    //=================error text
    By logNoEmail = By.id("login_email_error");
    By logBadPass = By.id("login_password_error");
    //=================links
    By regToLogin = By.cssSelector(".popup__footer .js_signin_link.js_no_link") ;
    By logToReg = By.cssSelector(".js_signup_link.js_no_link") ;
    By recoverLink = By.cssSelector(".js_recover_link.js_no_link.loginit__forgot");
    By createHeader = By.xpath("//div[@class='header__create']/a");

    //========================================================END LOCATORS======================================================================


    /******************************************  CLASSIC LOGIN  ******************************************/


    public MyPage userLogin(String email, String pass) {
	new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(logEmail));
	driver.findElement(logEmail).clear();
	driver.findElement(logEmail).sendKeys(email);
	driver.findElement(logPass).clear();
	driver.findElement(logPass).sendKeys(pass);
	driver.findElement(logSubmit).click();
	waitForCreateButtPresence();
	checkLangLogged(driver);
	return PageFactory.initElements(driver, MyPage.class);
    }


    public void userFailLogin(String email, String pass) {
	driver.findElement(logEmail).clear();
	driver.findElement(logEmail).sendKeys(email);
	driver.findElement(logPass).clear();
	driver.findElement(logPass).sendKeys(pass);
	driver.findElement(logSubmit).click();
    }


    public void waitForCreateButtPresence(){
	new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(createHeader));
    }


    /******************************************  FB/GM LOGIN  ******************************************/


    public MyPage loginWithFB() {
	driver.findElement(facebookBut).click();
	waitForCreateButtPresence();
	checkLangLogged(driver);
	return PageFactory.initElements(driver, MyPage.class);
    }


    public WebElement facebookBut() {
	return driver.findElement(facebookBut);
    }


    public WebElement googleBut() {
	return driver.findElement(googleBut);
    }


    public MyPage loginWithGM() {
	driver.findElement(googleBut).click();
	waitForCreateButtPresence();
	checkLangLogged(driver);
	return PageFactory.initElements(driver, MyPage.class);
    }


    public RegisterPage toRegistLink() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(logToReg));
	driver.findElement(logToReg).click();
	return PageFactory.initElements(driver, RegisterPage.class);
    }


    public LoginPage toLoginLink() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(regToLogin));
	driver.findElement(regToLogin).click();
	return PageFactory.initElements(driver, LoginPage.class);
    }




    public void userErrLogin(String email, String pass) {
	driver.findElement(By.id("login_error_email")).clear();
	driver.findElement(By.id("login_error_email")).sendKeys(email);
	driver.findElement(By.id("login_error_password")).clear();
	driver.findElement(By.id("login_error_password")).sendKeys(pass);
	driver.findElement(By.xpath("//input[@id='login_error_button']")).click();
    }


    public String getEmailError() {
	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(logNoEmail));
	return driver.findElement(logNoEmail).getText();
    }

    public String getPassError() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(logBadPass));
	return driver.findElement(logBadPass).getText();
    }

    public String getBadPass() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(logBadPass));
	return driver.findElement(logBadPass).getText();
    }


    public RecoverPage resetPass() {
	waitForPageLoaded(driver);
	driver.findElement(recoverLink).click();
	return PageFactory.initElements(driver, RecoverPage.class);
    }


    public boolean singinPopupOpened() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(signinPopup));
	return driver.findElement(signinPopup).getAttribute("class").contains("popup_open");
    }


    public boolean singupPopupOpened() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(signupPopup));
	return driver.findElement(signupPopup).getAttribute("class").contains("popup_open");
    }


    public void assertCaptchaExists() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.id("login_captcha_img")));
    }





}