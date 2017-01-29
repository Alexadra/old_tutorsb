package main.pages;

import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;




public class CreateHangoutPage extends BaseClass{
    private WebDriver driver;
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    public CreateHangoutPage(WebDriver driver) {
	this.driver = driver;
    }
    //========================================================LOCATORS==========================================================================
    //=================input
    By dateInput = By.id("hangout_date");
    By timeInput = By.id("hangout_time");
    By hangTitle = By.id("hangout_title");
    By hangDescrip = By.id("hangout_text");
    By hangTag = By.id("tags-input");
    By modeDropdown = By.xpath("//div[@class='form-create__switcher']//a[@class='button button_medium button_input js_tooltip']");
    By hangFreeMode = By.xpath("//div[@id='type_select']/a[@attr-hgtype='1']");
    By hangPrivMode = By.xpath("//div[@id='type_select']/a[@attr-hgtype='3']");
    By hangPayMode = By.xpath("//div[@id='type_select']/a[@attr-hgtype='2']");
    By privMode = By.id("access_closed");
    By inviteInput = By.id("hangout_invite");
    By inviteButt = By.id("hangout_invite_button");
    By inviteListFirst = By.xpath("//ul[@id='hangout_invite_suggestion']/li[1]");
    By hangMoney = By.id("hangout_tmoney");
    By hangSubmit = By.id("create_hangout");
    By hangoutImage = By.id("hangout_image");
    By timezoneButt = By.xpath("//div[@id='teacher_config_timezone']/a");
    By timeSamoa = By.xpath("//a[@class='dropdown__item'][@attr-timezone='2']");
    By timeKiev = By.xpath("//a[@attr-timezone='16']");
    By previewImage = By.id("preview_upload");
    By editImageButt = By.id("create_preview");
    By deleteImageButt = By.id("image_remove");
    By photoEdit = By.cssSelector("#hangout_image_container");
    By hangSaveImage = By.id("preview_create");
    By errorPriceMess = By.id("tm_invalid");
    By messPrice = By.id("tm_valid");
    By closePop = By.cssSelector("js_close_popup.popup__close.js_tooltip");
    //================errors

    By titleErrorInput = By.xpath("//div[@class='inputbox inputbox_error']/input[@id='hangout_title']");
    By timeErrorText = By.xpath("//div[@id='hangout_time_error']/div");
    By imageErrorText = By.xpath("//div[@id='image_error']/div");
    //================saved hangout
    By hangArea = By.cssSelector(".broadcast-body");
    By swfLoc = By.xpath("//object[contains(@style,'width')]");
    By headerLogo = By.cssSelector(".header__logo.header__logo_live");
    By startButton = By.xpath("//a[@class='join_hangout_js button button_primary button_big']");
    //========================================================END LOCATORS======================================================================


    public HangoutPage hangoutData(String mode, String hangoutName, String hangDescription, String data, String time, String price, String timezone) throws AWTException, InterruptedException {
	setTitle(hangoutName);
	setTimezone(timezone);
	setTime(data, time);
	setMode(mode, price); //free, pay
	setDescription(hangDescription);
	LOG.info("Hangout name is "+hangoutName);
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    public HangoutPage hangoutInvalidData(String mode, String hangoutName, String hangDescription, String data, String time, String tags, String image, String price) throws AWTException, InterruptedException {
	setTitle(hangoutName);
	setTime(data, time);
	setMode(mode, price);		//free, pay
	setDescription(hangDescription);
	setImage(image);
	//setTag(tags);
	//setTimezone();
	driver.findElement(hangSubmit).click();
	LOG.info("Hangout name is "+hangoutName);
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    public HangoutPage hangoutEditData(String hangoutName, String hangDescription, String tags, String image) {
	setTitle(hangoutName);
	setDescription(hangDescription);
	deleteImageClick();
	setTag(tags);
	saveHangout();
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    public void privateMode(){
	driver.findElement(privMode).click();
    }


    public void invitePrivUser(String name) throws InterruptedException{
	driver.findElement(inviteInput).clear();
	driver.findElement(inviteInput).sendKeys(name);
	Actions action = new Actions(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(inviteListFirst));
	action.moveToElement(driver.findElement(inviteListFirst)).click().perform();
	waitForPageLoaded(driver);
    }


    public void invitePrivEmail(String email){
	driver.findElement(inviteInput).clear();
	driver.findElement(inviteInput).sendKeys(email);
	driver.findElement(inviteButt).click();
	waitForPageLoaded(driver);
    }


    public HangoutPage saveHangout(){
	driver.findElement(hangSubmit).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(hangArea));
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    public WebinarPage saveStartHangout(){
	driver.findElement(hangSubmit).click();
	new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(headerLogo));
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public WebinarPage startHangout(){
	driver.findElement(startButton).click();
	new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(headerLogo));
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public void setMode(String mode, String price){
	if (mode.equals("free")){
	    driver.findElement(modeDropdown).click();
	    new WebDriverWait(driver,5).until(ExpectedConditions.presenceOfElementLocated(hangFreeMode));
	    driver.findElement(hangFreeMode).click();		//free type
	} else if (mode.equals("pay")) {
	    setPayMode(price);
	} else {
	    driver.findElement(modeDropdown).click();
	    new WebDriverWait(driver,5).until(ExpectedConditions.presenceOfElementLocated(hangPrivMode));
	    driver.findElement(hangPrivMode).click();
	}
    }


    public void setPayMode(String price) {
	driver.findElement(modeDropdown).click();
	new WebDriverWait(driver,5).until(ExpectedConditions.presenceOfElementLocated(hangPayMode));
	write("pay");
	driver.findElement(hangPayMode).click();		//free type
	driver.findElement(hangMoney).clear();
	driver.findElement(hangMoney).sendKeys(price);
    }


    public String getPrice(){
	if (valid.equals("valid"))
	    return new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(messPrice)).getText();
	else return new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(errorPriceMess)).getText();
    }


    public void setTitle(String hangoutName){
	driver.findElement(hangTitle).clear();
	driver.findElement(hangTitle).sendKeys(hangoutName);
    }


    public void setDescription(String hangDescription){
	driver.findElement(hangDescrip).clear();
	driver.findElement(hangDescrip).sendKeys(hangDescription);
    }


    public void setTime(String data, String time){
	if (data.equals("")) return;
	driver.findElement(dateInput).clear();
	write(data);
	driver.findElement(dateInput).sendKeys(data);
	driver.findElement(timeInput).clear();
	write(time);
	driver.findElement(timeInput).sendKeys(time);
    }


    public void setTimezone(String zone) throws AWTException, InterruptedException{
	driver.findElement(By.xpath("//div[@id='teacher_config_timezone']/a")).click();
	Point coord = driver.findElement(By.xpath("//*[@id='teacher_config_timezone']//div[@class='jspContainer']")).getLocation();
	Robot robot = new Robot();
	for(int i=0;i<=50;i++){
	    robot.mouseMove(coord.getX()+20,coord.getY()+150);
	    robot.mouseWheel(i);
	    System.out.println("scroll" + i);
	    Thread.sleep(200);
	    try{
		if(driver.findElement(By.xpath("//a[text()[contains(.,'"+ zone +"')]]")).isDisplayed()){
		    System.out.println("Displayed");
		    driver.findElement(By.xpath("//a[text()[contains(.,'"+ zone +"')]]")).click();
		    System.out.println("2");
		    return;
		}
	    } catch(MoveTargetOutOfBoundsException e){
		robot.mouseWheel(2);
		System.out.println(e);
	    }
	}


	/*new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(timeSamoa));
	WebElement samoa = driver.findElement(timeSamoa);
	Point coord = samoa.getLocation();
	Robot robot = new Robot();
	robot.mouseMove(coord.getX()+20,coord.getY()+150);
	Thread.sleep(1000);
	robot.mouseWheel(12);
	driver.findElement(timeKiev).click();*/
    }


    public void setImage(String image){
	fileInputHidden(hangoutImage, image, driver );		//image input
	if (!image.contains("wrong")){
	    driver.findElement(hangSaveImage).click();
	}
    }


    public void setPreviewImage(String image){
	fileInputHidden(previewImage, image, driver );		//image input
	if (!image.contains("wrong")){
	    driver.findElement(hangSaveImage).click();
	}
    }


    public void editImageClick(){
	Actions action = new Actions(driver);
	action.moveToElement(driver.findElement(photoEdit));
	driver.findElement(editImageButt).click();
	waitForPageLoaded(driver);
    }


    public void deleteImageClick(){
	Actions action = new Actions(driver);
	action.moveToElement(driver.findElement(photoEdit),0,100).build().perform();
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(deleteImageButt));
	driver.findElement(deleteImageButt).click();
    }


    public void closePopup(){
	driver.findElement(closePop).click();
    }


    public void setTag(String tags){
	driver.findElement(hangTag).sendKeys(Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE);
	driver.findElement(hangTag).sendKeys(tags);
    }


    public void assertTitleInputError() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(titleErrorInput));
    }


    public String getTimeError() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(timeErrorText));
	return driver.findElement(timeErrorText).getText();
    }


    public String getImageError() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(imageErrorText));
	return driver.findElement(imageErrorText).getText();
    }


    public String getErrorText(String inputId) {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='"+inputId+"']/following::div[@class='form-error__text'][1]")));
	return driver.findElement(By.xpath("//*[@id='"+inputId+"']/following::div[@class='form-error__text'][1]")).getText();
    }


    public String getErrorOther(String errorId) {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='"+errorId+"_error']")));
	return driver.findElement(By.xpath("//*[@id='"+errorId+"_error']/div")).getText();
    }


    public String getErrorPrice(){
	return new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(errorPriceMess)).getText();
    }


    public void assertCreatePage(){
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(dateInput));
    }


}

