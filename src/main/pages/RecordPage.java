package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RecordPage extends BaseClass{
    private WebDriver driver;
    public String hangoutName;
    public int hangPrice;

    public RecordPage(WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================opened record
    By recordStatus = By.cssSelector(".paper__media-status__content");
    By publishButt = By.id("publish_video");
    By editVideo = By.cssSelector(".button.button_default.button_small");
    By overlayPlay = By.cssSelector(".mejs-overlay-button");
    By mainPlay = By.xpath("//button[@play]");
    By player = By.id("vodOverlay");
    By titleText = By.xpath("//div[@class='text__h2'][not(a)]");
    By descrText = By.cssSelector(".text__p.share_text_js");
    By priceText = By.cssSelector(".paper__price-new");
    By sharePresent = By.cssSelector(".list-horiz.list-horiz_share.list-horiz_share-sidebar.clearfix");
    By buyRecord = By.cssSelector(".js_no_link.js_this_signup.button.button_primary");
    By commenInput = By.id("add_comment_text");
    //=================create record
    By titleInput = By.id("hangout_title");
    By descrInput = By.id("hangout_text");
    By freeTab = By.id("hangout_money_free");
    By payTab = By.id("hangout_money_closed");
    By moneyInput = By.id("hangout_tmoney");
    By saveButt = By.id("not_publish");

    //=================plitka records
    By frstPlitkaStatus = By.xpath("(//div[@class='plitka__status-content'])[1]");
    By frstReadyVideo = By.xpath("(//div[@class='plitka__status plitka__status_overlay'])[1]");
    By frstPublishedVideo = By.xpath("(//div[@class='plitka__status-play'])[1]");
    By frstLockedVideo = By.xpath("(//div[@class='plitka__status-lock'])[1]");
    //========================================================END LOCATORS======================================================================


    public String getStatus(){
	return driver.findElement(recordStatus).getText();
    }


    public String getFirstPlitkaStatus(){
	return driver.findElement(frstPlitkaStatus).getText();
    }


    public void publishRecord(){
	waitRecordProccessed();
	driver.findElement(publishButt).click();
	waitForPageLoaded(driver);
    }


    public void waitRecordProccessed(){
	for (int i = 0;driver.findElements(recordStatus).size()==1;i++){
	    driver.navigate().refresh();
	    if (i>15) return;
	}
    }


    public void openReadyVideo() {
	driver.findElement(frstReadyVideo).click();
	waitForPageLoaded(driver);
    }


    public void openLockedVideo(){
	driver.findElement(frstLockedVideo).click();
	waitForPageLoaded(driver);
    }


    public void openPublishedVideo(){
	driver.findElement(frstPublishedVideo).click();
	waitForPageLoaded(driver);
    }


    public void editVideoButt(){
	driver.findElement(editVideo).click();
	waitForPageLoaded(driver);
    }


    public void playOverlayButt(){
	driver.findElement(overlayPlay).click();
	waitForPageLoaded(driver);
    }

    public void playPauseOverlay(){
	driver.findElement(player).click();
    }


    public void playPauseMain(){
	driver.findElement(mainPlay).click();
    }


    public String getPlayVisibility() {
	return driver.findElement(overlayPlay).getCssValue("visibility");
    }


    public void editVideoData(String title, String descr, String mode, int price) {
	setTitle(title);
	setDescription(descr);
	setPayMode(mode, price);
	saveVideo();
    }


    public void setTitle(String title){
	driver.findElement(titleInput).clear();
	driver.findElement(titleInput).sendKeys(title);
    }


    public void setDescription(String descr){
	driver.findElement(descrInput).clear();
	driver.findElement(descrInput).sendKeys(descr);
    }


    public void setPayMode(String mode,int price){
	if (mode.equals("pay")){
	    driver.findElement(payTab).click();
	    driver.findElement(moneyInput).sendKeys(Keys.BACK_SPACE,Keys.BACK_SPACE);
	    driver.findElement(moneyInput).sendKeys(price+"");
	} else driver.findElement(freeTab).click();
    }


    public void saveVideo(){
	driver.findElement(saveButt).click();
    }


    public String getVideoTitle(){
	//avoid "edit, delete text"
	if (driver.findElements(editVideo).size()>0){
	    int length = driver.findElement(titleText).getText().length();
	    int titleEnd = length - 22;
	    return driver.findElement(titleText).getText().substring(0, titleEnd);
	} else return driver.findElement(titleText).getText();
    }


    public String getDescription(){
	return driver.findElement(descrText).getText();
    }


    public String getPrice(){
	return driver.findElement(priceText).getText().replace("TBM", "").trim();
    }


    public boolean isSharePresent() {
	if (driver.findElements(sharePresent).size()>0) return true;
	else return false;
    }


    public MoneyPage buyVideo(){
	driver.findElement(buyRecord).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, MoneyPage.class);
    }


    public void waitCommentInput(){
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(commenInput));
    }



}
