package main.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class RegisterPage extends BaseClass{

    private WebDriver driver;
    public RegisterPage (WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================
    By regName = By.id("register_name");
    By regEmail = By.id("register_email");
    By regPass = By.id("register_password");
    By regSubmit = By.id("register_button");
    By nameErrorLoc = By.id("register_name_error");
    By emailErrorLoc = By.id("register_email_error");
    By usedEmailErr = By.id("register_email_error_used_email");
    By passErrorLoc = By.id("register_password_error");
    By interestLoc1 = By.xpath("//div[@class='theme-select__row'][1]/a[@class='theme-select__item'][1]");
    By interestLoc2 = By.xpath("//div[@class='theme-select__row'][2]/a[@class='theme-select__item'][1]");
    By interestLoc3 = By.xpath("//div[@class='theme-select__row'][3]/a[@class='theme-select__item'][1]");
    By submitIntLoc = By.id("register_tags_button");
    By userNameLoc = By.cssSelector(".user-big__name");

    By facebookBut = By.cssSelector(".button.button_loginit.button_big.button_loginit_fb.js_fb_button_register");
    By googleBut = By.cssSelector(".button.button_loginit.button_big.button_loginit_gp.js_gp_button_register");
    By inviteUsersGM = By.cssSelector(".inviteitem.clearfix");
    By submiteInviteGM = By.id("google_invite_submit");
    //========================================================END LOCATORS======================================================================


    public void userReg(String name,String email, String pass) {
	waitForPageLoaded(driver);
	driver.findElement(regName).clear();
	driver.findElement(regName).sendKeys(name);
	driver.findElement(regEmail).clear();
	driver.findElement(regEmail).sendKeys(email);
	driver.findElement(regPass).clear();
	driver.findElement(regPass).sendKeys(pass);
	driver.findElement(regSubmit).click();
	//new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(interestLoc1));
    }


    public MyPage chooseInterest() {
	driver.findElement(interestLoc1).click();
	driver.findElement(interestLoc2).click();
	driver.findElement(interestLoc3).click();
	driver.findElement(submitIntLoc).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(userNameLoc));
	return PageFactory.initElements(driver, MyPage.class);
    }


    public void assertCaptchaExists() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.id("login_captcha_img")));
    }


    public void assertInviteGM() throws InterruptedException{
	waitForPageLoaded(driver);
	Assert.assertTrue(driver.findElements(inviteUsersGM).size()>0);
	Thread.sleep(2000);
	waitForPageLoaded(driver);
    }


    public void confirmInviteGM() throws InterruptedException{
	Thread.sleep(2000);
	driver.findElement(submiteInviteGM).click();
	waitForPageLoaded(driver);
    }


    public String[] getThreeInterests() {
	String interest1 = driver.findElement(interestLoc1).getText();
	String interest2 = driver.findElement(interestLoc2).getText();
	String interest3 = driver.findElement(interestLoc3).getText();
	String[] interests = new String[]{interest1, interest2, interest3};
	return interests;
    }


    public boolean assertInterestsValid() {
	for(String interest: getThreeInterests()) {
	    int length = interest.length();
	    if (length < 2) {
		return false;
	    } else continue;
	}
	return true;
    }


    public String getNameError() {
	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(nameErrorLoc));
	return driver.findElement(nameErrorLoc).getText();
    }


    public String getMailError() {
	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(emailErrorLoc));
	return driver.findElement(emailErrorLoc).getText();
    }


    public String getUsedMailErr() {
	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(usedEmailErr));
	return driver.findElement(usedEmailErr).getText();
    }


    public String getPassError() {
	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(passErrorLoc));
	return driver.findElement(passErrorLoc).getText();
    }


    public WebElement facebookBut() {
	return driver.findElement(facebookBut);
    }


    public WebElement googleBut() {
	return driver.findElement(googleBut);
    }


}
