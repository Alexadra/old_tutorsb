package main.pages;

import java.awt.AWTException;
import java.util.List;

import main.Driver.DataProperties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;



public class HangoutPage extends BaseClass{
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;
    String url = DataProperties.get("url");

    public HangoutPage(WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================plitka
    By allHangLoc = By.xpath("//div[@class='plitka__title']");
    By allHangPrice = By.xpath("//div[contains(@class,'plitka-top__price')]");
    By firstHangName = By.xpath("//div[@class='paper paper_hangout'][1]/div[@class='text__h2']/a");
    By firstPrice = By.xpath("(//div[@class='plitka-top__price']/span/parent::div)[1]");
    By firstPaidLink = By.xpath("(//div[@class='plitka-top__price']/span/parent::div/parent::div/parent::a)[1]");
    By firstFreeLink = By.xpath("(//div[@class='plitka-top__price '][not(span)]/ancestor::a)[1]");
    By firstPaidName = By.xpath("(//div[@class='plitka-top__price']/span/ancestor::a/div[@class='plitka__title'])[1]");
    By firstFreeName = By.xpath("(//div[@class='plitka-top__price '][not(span)]/ancestor::a/div[@class='plitka__title'])[1]");
    By firstName = By.xpath("(//div[@class='plitka__title'])[1]");
    //=================hangout
    By hangTitle = By.xpath("//div[@class='paper paper_hangout']/div[@class='text__h2']");		//contains edit, delete text
    By hangDescrip = By.xpath("//div[@class='paper paper_hangout']/div[@class='paper__text']");
    By hangTags = By.xpath("//div[@class='paper paper_hangout']/div[@class='paper__tags clearfix']");
    By statusFull = By.cssSelector(".paper__media-status");
    By deleteHang = By.cssSelector(".button.button_danger.button_small.hangout_delete_js");
    By confirmDelete = By.cssSelector(".button.button_danger.confirmed_delete_js");
    By cancelDelete = By.xpath("//div[@class='buttons_list']/a[2]");
    By takePartUnlog = By.id("original_subscribe");
    By takePartPay = By.id("original_subscribe");
    By takePartFree = By.id("original_subscribe");
    By participList = By.xpath("//div[@class='userpic userpic_size_small']//img");
    By timezoneButt = By.cssSelector(".button.button_default.button_small.popup_js");
    By timezomeSelect = By.cssSelector("#timezone_select");
    //=================start
    By startButt = By.cssSelector(".button.join_hangout_js.button_primary.button_big.button_block");
    By loaderLogo = By.cssSelector(".loader-logo");
    By headerLogo = By.cssSelector(".header__logo.header__logo_live");
    By finishButt = By.id("client-exit");
    By recordStatus = By.cssSelector(".paper__media-status__content");
    //=================comments
    By commenInput = By.id("add_comment_text");
    By commSubmit = By.cssSelector("#add_comment_form input[type='submit']");
    By commLastAvtor = By.xpath("(//*[@id='comments_list_js']//span)[1]");
    By commText = By.xpath("//*[@id='comments_list_js']/div[1]/p");
    By loginLinkComment = By.cssSelector(".alert.alert_info .js_signin_link.js_no_link");
    By unlogCommentText = By.cssSelector(".alert.alert_info");
    //=================edit
    By editButt = By.cssSelector(".buttons__edit .button.button_default.button_small");
    By imageLoc = By.xpath("//div[@class='paper__media']/img");
    //=================share
    By shareButt = By.id("share_button");
    By shareFbButt = By.xpath("//a[@data-type='facebook']");
    By shareGlButt = By.xpath("//a[@data-type='google']");
    By shareTwButt = By.xpath("//a[@data-type='twitter']");
    By shareVkButt = By.xpath("//a[@data-type='vkontakte']");
    By fbHangTitle = By.xpath("//div[@class='UIShareStage_Title']");
    By ggHangTitle = By.xpath("//div[@class='yF']/a");
    By twHangTitle = By.xpath("//textarea[@id='status']");
    By vkHangTitle = By.id("share_title");

    @FindBy(xpath = "//div[@class='paper paper_hangout']//div[@class='text__h2']")
    WebElement hangNameOpen;
    //========================================================END LOCATORS======================================================================


    /******************************************  PARTICIPATION  *****************************************/


    public void takePartFreeHangout() {
	driver.findElement(takePartFree).click();
    }


    public MoneyPage takePartPayHangout() {
	driver.findElement(takePartPay).click();
	return PageFactory.initElements(driver, MoneyPage.class);
    }


    public void takePartHangoutUnloged() {
	//click on button "take part". as I unlogged , register popup will be shown
	driver.findElement(takePartUnlog).click();
    }


    public void assertUserTakes(String name) {
	//assert that participant list includes given name
	waitForPageLoaded(driver);
	LOG.info("Assert that user "+name+" takes part in conference");
	List <WebElement> participants = driver.findElements(participList);
	for(WebElement user: participants) {
	    LOG.info("Participant name" + user.getAttribute("alt").replace(" ", ""));
	    if (user.getAttribute("alt").replace(" ", "").equals(name.replace(" ", ""))) return;
	}
	throw new NoSuchElementException("No user "+name+" was found in participants");
    }


    /******************************************  OPEN HANGOUT  ******************************************/


    public void openFirstPaid() {
	driver.findElement(firstPaidLink).click();
    }


    public void openFirstFreeActive() {
	driver.findElement(firstFreeLink).click();
    }


    public void openHangoutByName(String hangName) {
	driver.findElement(By.xpath("//span[contains(text(),'"+hangName+"')]//ancestor::a[1]")).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".comments")));
    }


    public void openHangoutByNum(int hangNum) {
	waitForPageLoaded(driver);
	driver.findElement(By.xpath("(//a[@class='plitka-link'])["+hangNum+"]")).click();
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".comments")));
    }


    /******************************************  COMMENTS  ******************************************/


    public void waitCommentInput() {		//asserts that comment input exists on page
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(commenInput));
    }


    public void addComment(String string) { //new
	driver.findElement(commenInput).clear();
	driver.findElement(commenInput).sendKeys(string);
	driver.findElement(commSubmit).click();
	waitForPageLoaded(driver);
    }


    public String getCommenterName() {
	return driver.findElement(commLastAvtor).getText();
    }


    public String getCommentText() {
	return driver.findElement(commText).getText();
    }


    public LoginPage loginLinkFromComments(){
	driver.findElement(loginLinkComment).click();
	return PageFactory.initElements(driver, LoginPage.class);
    }


    public String getCommentAlertText() {
	return driver.findElement(unlogCommentText).getText();
    }


    /******************************************  COMMENTS  ******************************************/



    public String hangPrice(String hangName) {
	write("Price for hangout " + hangName +" equals :" +
		driver.findElement(By.xpath("//span[contains(text(),'"+hangName+"')]//ancestor::div[2]//div[contains(@class,'plitka__price')]")).getText());
	return driver.findElement(By.xpath("//span[contains(text(),'"+hangName+"')]//ancestor::div[2]//div[contains(@class,'plitka__price')]")).getText();
    }


    public void deleteHangout(){
	driver.findElement(deleteHang).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.elementToBeClickable(confirmDelete));
	driver.findElement(confirmDelete).click();
	waitForPageLoaded(driver);
    }


    public String deleteHangoutStatus(String hangName) throws InterruptedException, AWTException {
	deleteHangout();
	if (driver.findElements(By.xpath("//span[contains(text(),'"+hangName+"')]/ancestor::div/a//div[@class='plitka__status-onlytext']")).size()==1){
	    return driver.findElement(By.xpath("//span[contains(text(),'"+hangName+"')]/ancestor::div/a//div[@class='plitka__status-onlytext']")).getText();
	} else throw new NullPointerException("Hangout has no calceled or over status");
    }








    public int getFirstPaidPrice(){
	String price = driver.findElement(firstPrice).getText();
	if (price.equals("Бесплатная")) throw new NoSuchElementException(" Wrong conference was found. It is a  free conference !");
	else return Integer.parseInt(price.replaceAll("\\D*", "").trim());
    }


    public String getFirstPaidName(){
	return driver.findElement(firstPaidName).getText();
    }


    public String getFirstFreeName(){
	return driver.findElement(firstFreeName).getText();
    }


    public String getFirstName(){
	return driver.findElement(firstName).getText();
    }


    public void assertHangSaved(String name) {
	waitForPageLoaded(driver);
	List <WebElement> allHangouts = driver.findElements(allHangLoc);		//create all hangout names list
	for(WebElement hangout: allHangouts) {			//for every hangout
	    if (hangout.getText().equals(name))			//assert if our hangout added to all hangouts
		return;
	    else continue;
	}
	throw new NoSuchElementException("Hangout '"+name+"' was not added");
    }








    public CreateHangoutPage editClick() {
	driver.manage().window().maximize();
	driver.findElement(editButt).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.titleContains("Редактировать"));
	return PageFactory.initElements(driver, CreateHangoutPage.class);
    }





    /******************************************  HANGOUT DATA  ******************************************/


    public String getHangName() {
	if (driver.findElements(editButt).size()>0){
	    int length = driver.findElement(hangTitle).getText().length();
	    int titleEnd = length - 22;
	    return driver.findElement(hangTitle).getText().substring(0, titleEnd);
	} else return driver.findElement(hangTitle).getText();
    }


    public String getDescript() {
	return driver.findElement(hangDescrip).getText();
    }


    public String getTag() {
	return driver.findElement(hangTags).getText();
    }

    public boolean isImageExists() {
	if (driver.findElement(imageLoc).getAttribute("src").contains("empty_picture")){
	    return false;
	} else return true;
    }


    /******************************************  SHARING  ******************************************/

    public WebElement shareFaceb() {
	driver.manage().window().maximize();
	return driver.findElement(shareFbButt);
    }


    public WebElement shareGoog() {
	driver.manage().window().maximize();
	return driver.findElement(shareGlButt);
    }


    public WebElement shareTwit() {
	driver.manage().window().maximize();
	return driver.findElement(shareTwButt);
    }


    public WebElement shareVk() {
	driver.manage().window().maximize();
	return driver.findElement(shareVkButt);
    }


    public String getHangoutNameFaceb() {
	LOG.info(driver.findElement(fbHangTitle).getText());
	return driver.findElement(fbHangTitle).getText();
    }


    public String getHangoutNameGoog() {
	LOG.info(driver.findElement(ggHangTitle).getText());
	return driver.findElement(ggHangTitle).getText();
    }


    public String getHangoutNameTwit() {
	LOG.info(driver.findElement(twHangTitle).getText());
	return driver.findElement(twHangTitle).getText();
    }


    public String getHangoutNameVk() {
	LOG.info(driver.findElement(vkHangTitle).getText());
	return driver.findElement(vkHangTitle).getText();
    }



    /******************************************  STATUS  ******************************************/


    public WebinarPage startHangout() throws InterruptedException {
	startHangout("");
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public WebinarPage startHangout(String hangUrl) throws InterruptedException {
	if (hangUrl!=""){
	    driver.get(hangUrl);
	}
	driver.findElement(startButt).click();
	new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(headerLogo));
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public String getHangoutStatus() {
	return driver.findElement(statusFull).getText();
    }


    public String getPlitkaStatus(String name){
	return driver.findElement(By.xpath("//div[contains(text(),'"+name+"')]/parent::a//div[@class='plitka__status-content']")).getText();
    }


    /*public WebinarPage joinWebinar() throws InterruptedException{
	joinWebinar("");
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public WebinarPage joinWebinar(String hangUrl) throws InterruptedException {
	if (hangUrl!=""){
	    driver.get(hangUrl);
	}
	driver.findElement(joinButt).click();
	new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(headerLogo));
	return PageFactory.initElements(driver, WebinarPage.class);
    }*/









    public String getTimePopupText() {
	driver.findElement(timezoneButt).click();
	waitForPageLoaded(driver);
	return driver.findElement(timezomeSelect).getText();
    }





}


