package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecoverPage {

    private WebDriver driver;
    public RecoverPage(WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================recover
    By inputRecover = By.id("recover_email");
    By recoverSubmit = By.xpath("//form[@id='recover_form']//input[@type='submit']");
    By recoverMess = By.id("recover_message");
    By errorMess = By.id("recover_email_error");
    By recovTitle = By.cssSelector("#js_recover_popup .popup__title");
    By recovExpiredEmail = By.xpath("//input[@id='recover_email']");
    By captchaRecov = By.id("recover_captcha_img");
    //=================new password
    By passInput = By.id("new_pass_password");
    By submitPass = By.id("new_pass_button");
    //========================================================END LOCATORS=======================================================================

    public void resetPassword(String mail) {
	driver.findElement(inputRecover).clear();
	driver.findElement(inputRecover).sendKeys(mail);
	driver.findElement(recoverSubmit).click();
    }


    public String getResetMess(String errorMessage){
	new WebDriverWait(driver,10).until(ExpectedConditions.
		textToBePresentInElement(recoverMess, errorMessage));
	return  driver.findElement(recoverMess).getText();
    }


    public String getErrorReset(String errorMessage){
	new WebDriverWait(driver,10).until(ExpectedConditions.
		textToBePresentInElement(errorMess, errorMessage));
	return  driver.findElement(errorMess).getText();
    }


    public String getErrorNoReset(String errorMessage){
	new WebDriverWait(driver,15).until(ExpectedConditions.
		textToBePresentInElement(errorMess, errorMessage));
	return  driver.findElement(errorMess).getText();
    }


    public void newPassword(String pass) {
	new WebDriverWait(driver,10).until(ExpectedConditions.titleIs("Новый пароль")); /////////
	driver.findElement(passInput).clear();
	driver.findElement(passInput).sendKeys(pass);
	driver.findElement(submitPass).click();
    }


    public String getRecovTitle() {
	return driver.findElement(recovTitle).getText();
    }


    public String getExpiredEmail() {
	return driver.findElement(recovExpiredEmail).getAttribute("value");
    }


    public void assertCaptchaExists() {
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(captchaRecov));
	if (driver.findElements(captchaRecov).size()>0) return;
	else throw new NoSuchElementException("Captcha was not found");
    }


}
