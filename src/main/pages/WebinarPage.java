package main.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import main.Driver.DataProperties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import test.java.LogTest;

public class WebinarPage extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;

    public WebinarPage(WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================
    By flashSettings = By.xpath("//object[contains(@style,'width')]");
    By finishCabinet = By.id("header__exit_btn");
    By confirmExit = By.id("yes-exit");
    By uploadFileCenter = By.id("upload-document-second");
    By uploadFile = By.id("upload-document");
    By presentationLoc = By.id("presentationContainer");
    By presentSlide = By.id("svgImage");
    By paramsSlide = By.id("svgGroup");
    By videoTab = By.xpath("//a[@data-type='video']");
    By presentationTab = By.xpath("//a[@data-type='presentation']");
    By uploadButtCenter = By.cssSelector(".client-add__content .button.button_primary.button_upload");
    By uploadButtUp = By.cssSelector(".button.button_primary.button_upload");
    By uploadErrorMess = By.cssSelector(".popup__content .text__p.text_red");
    By closeErrorPop = By.cssSelector(".button.button_inverse.button_medium.jsClosePopup");
    By swfLoc = By.xpath("//object[contains(@style,'width')]");
    By dropdownLink = By.id("jsPresentationsDropdownLink");
    By dropdownButt = By.id("jsPresentationsDropdownButton");
    By nextSlide = By.cssSelector(".js_tooltip.slider__control.slider__control_right");
    By prevSlide = By.cssSelector(".js_tooltip.slider__control.slider__control_left");
    By scaleButt = By.xpath("//div[@id='jsPresentationScaleButton']//a");
    By scaleControll = By.id("jsPresentationScaleControl");

    //========================================================END LOCATORS======================================================================


    public void allowFlashPermiss() throws AWTException, InterruptedException{
	Point swfPosition = driver.findElement(flashSettings).getLocation();
	Robot robot = new Robot();
	//move to flash permissions (112 px offset for browser header):
	robot.mouseMove(swfPosition.getX()+13, swfPosition.getY()+81+112);
	Thread.sleep(1000);
	robot.mousePress(InputEvent.BUTTON1_MASK);
	robot.mouseRelease(InputEvent.BUTTON1_MASK);
	Thread.sleep(1000);
	//move to cancel button :
	robot.mouseMove(swfPosition.getX()+174, swfPosition.getY()+120+112);
	robot.mousePress(InputEvent.BUTTON1_MASK);
	robot.mouseRelease(InputEvent.BUTTON1_MASK);
	Thread.sleep(1000);
    }


    public void waitCabinetLoader(){
	new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(swfLoc));
    }


    public void hideSwf(){
	waitCabinetLoader();
	((JavascriptExecutor) driver).executeScript("clientFlash.hideSWF();");
    }


    public String getPresenterId(){
	String js = "return clientState.modules.userslist.presenter;";
	Object presenter = ((JavascriptExecutor) driver).executeScript(js);
	return presenter.toString();
    }


    public void goToVideoTab(){
	driver.findElement(videoTab).click();
	waitForPageLoaded(driver);
    }


    public void goToPresentationTab(){
	driver.findElement(presentationTab).click();
	waitForPageLoaded(driver);
    }


    public String getActiveCamera(){
	String js = "return clientFlash.getActiveCamera();";
	Object presenter = ((JavascriptExecutor) driver).executeScript(js);
	return presenter.toString();
    }


    public RecordPage finishHangout(){
	new WebDriverWait(driver,10).until(ExpectedConditions.elementToBeClickable(finishCabinet));
	waitForPageLoaded(driver);
	driver.findElement(finishCabinet).click();
	driver.findElement(confirmExit).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, RecordPage.class);
    }


    /******************************************  PRESENTATION  ******************************************/
    public void waitForCenterButtPresent() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(uploadButtCenter));
    }


    public void uploadFirstFile(String file){
	waitForCenterButtPresent();
	fileInputHidden(uploadFileCenter, DataProperties.path(file), driver);
    }


    public void assertFirstFileLoaded(String file){
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='"+DataProperties.get(file)+"']")));
	Assert.assertEquals(getPresentationName(), DataProperties.get(file));
    }


    public void chooseFile(String file){
	if (!isDropdownOpen()) driver.findElement(dropdownLink).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='"+DataProperties.get(file)+"']/ancestor::div[1]")));
	driver.findElement(By.xpath("//span[text()='"+DataProperties.get(file)+"']/ancestor::div[1]")).click();
    }


    public void deleteFile(String file) {
	if (!isDropdownOpen()) driver.findElement(dropdownLink).click();
	Actions action = new Actions(driver);
	action.moveToElement(driver.findElement(By.xpath("//span[text()='"+DataProperties.get(file)+"']/ancestor::div[1]")));
	new WebDriverWait(driver,10).until(ExpectedConditions.
		presenceOfElementLocated(By.xpath("//span[text()='"+DataProperties.get(file)+"']/ancestor::div[1]/span[@class='jsDeletePresentation dropdown__item-clear js_tooltip']")));
	driver.findElement(By.xpath("//span[text()='"+DataProperties.get(file)+"']/ancestor::div[1]/span[@class='jsDeletePresentation dropdown__item-clear js_tooltip']")).click();
    }


    public void uploadFile(String file){
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(uploadButtUp));
	fileInputHidden(uploadFile, DataProperties.path(file), driver);
    }


    public void clickSlidePreview(int page){
	driver.findElement(By.xpath("//div[@id='jsSliderPreviews']//div["+page+"]")).click();
    }


    public void clickNextSlide(){
	driver.findElement(nextSlide).click();
    }


    public void clickPrevSlide(){
	driver.findElement(prevSlide).click();
    }





    public int getActiveSlide(){
	write(driver.findElement(presentSlide).getAttribute("xlink:href"));
	String preslide[] = driver.findElement(presentSlide).getAttribute("xlink:href").split("slide", 2);
	write(preslide[0]);
	write(preslide[1]);
	String slide[] = preslide[1].split("\\.", 2);
	write(slide[0]);
	write(slide[1]);
	return Integer.parseInt(slide[0])+1;
    }


    public Boolean errorIconExists(String file){
	if (driver.findElements(By.xpath("//*[text()='"+DataProperties.get(file)+"']/preceding-sibling::i[@class='icon-notify']")).size()!=0){
	    return true;
	}
	else return false;
    }


    public String getPresentationName(){
	waitForPageLoaded(driver);
	return driver.findElement(dropdownLink).getText();
    }


    public void scalePresentationWheel() throws AWTException {
	Point slide = driver.findElement(presentSlide).getLocation();
	String params1[] = driver.findElement(paramsSlide).getAttribute("transform").split("scale", 2);
	String scale1 = params1[1];
	Robot robot = new Robot();
	robot.mouseMove(slide.getX(), slide.getY()+200);
	robot.mouseWheel(-5);
	String params2[] = driver.findElement(paramsSlide).getAttribute("transform").split("scale", 2);
	String scale2 = params2[1];
	write(scale1+scale2);
	Assert.assertTrue(!scale1.equals(scale2),"Presentation scale hasn't changed");
    }


    public void scalePresentation(){
	String params1[] = driver.findElement(paramsSlide).getAttribute("transform").split("scale", 2);
	String scale1 = params1[1];
	driver.findElement(scaleButt).click();
	Actions action = new Actions(driver);
	action.clickAndHold(driver.findElement(scaleControll)).moveByOffset(50, 0).release().perform();
	String params2[] = driver.findElement(paramsSlide).getAttribute("transform").split("scale", 2);
	String scale2 = params2[1];
	write(scale1+scale2);
	Assert.assertTrue(!scale1.equals(scale2),"Presentation scale hasn't changed");
    }


    public void waitForFileinList(final String file) throws Exception{
	if (!isDropdownOpen()) driver.findElement(dropdownLink).click();
	List <WebElement> element = driver.findElements(By.cssSelector(".uploader-name"));
	final List <String> fileList = new ArrayList<String>();
	int i = 0;
	for (WebElement elem:element){
	    LOG.info(elem.getText()+i );
	    fileList.add(elem.getText());
	    i++;
	}
	ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
	    @Override
	    public Boolean apply(WebDriver driver) {
		return fileList.contains(DataProperties.get(file));
	    }
	};
	Wait <WebDriver> wait = new WebDriverWait(driver,10);
	try {
	    wait.until(expectation);
	} catch (Throwable error) {
	    LOG.error( "There are no files with name" + DataProperties.get(file) + "in the list" );
	    // throw error;
	}
    }


    public Boolean isDropdownOpen(){
	System.out.println(driver.findElement(dropdownButt).getAttribute("class"));
	if (driver.findElement(dropdownButt).getAttribute("class").contains("open")){
	    return true;
	} else return false;
    }





}
