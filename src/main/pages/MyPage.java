package main.pages;


import java.awt.AWTException;

import main.Driver.DataProperties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import test.java.LogTest;


public class MyPage extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;
    public MyPage(WebDriver driver) {
	this.driver = driver;
    }

    public String tagText;
    String url = DataProperties.get("url");

    //========================================================LOCATORS==========================================================================
    //=================info user
    By userName = By.cssSelector(".page-profile .userpic-user__name");
    By townLoc = By.cssSelector(".user-location");
    By aboutLoc = By.xpath("//div[@class='user-about']/div[@class='text__p'][1]");
    By tagLoc = By.cssSelector(".tags__section.clearfix");
    By firstUserTag = By.xpath("//*[@class='tags__section clearfix']/*[@class='tag-user'][1]");
    By firstHangTag = By.xpath("//*[@class='paper__tags clearfix']/*[@class='tag-user'][1]");
    By imageAddedLoc = By.xpath("//div[@class='photo_edit' and @style='']");
    By noImageLoc = By.xpath("//div[@class='photo_edit' and @style='display: none;']");
    By userNameMenu = By.xpath("//li[@class='menu-left-li menu-left-li_profile']//*[@class='userpic-user__name']");
    By userNameProfile = By.xpath("//li[@class='menu-left-li menu-left-li_profile']//*[@class='userpic-user__name']");

    //=================tabs
    By recomClick = By.xpath("//ul[@class='tabs__list']/li[2]/a");
    By scheduleClick = By.xpath("//ul[@class='tabs__list']/li[1]/a");
    By recomTabStatus = By.xpath("//ul[@class='tabs__list']/li[2]");
    By hangTabStatus = By.xpath("//ul[@class='tabs__list']/li[1]");
    By emptyText = By.cssSelector(".empty");

    //=================header
    By mailLoc = By.cssSelector(".header__chat");
    By headerMoney = By.cssSelector(".userpic-money.js_tooltip");
    By headerProfile = By.cssSelector(".header__profile .userpic");
    By profileSett = By.xpath("//a[@class='dropdown__item' and contains(@href,'config')]");
    By profileExit = By.id("logout");
    By profileDemo = By.xpath("//a[@class='dropdown__item js_no_link' and contains(@href,'demo')]");
    By createHang = By.cssSelector(".header__create a");
    By showNotify = By.id("notify_show");
    //=================user main buttons
    By followersLoc = By.cssSelector(".stat-grid__right .folow");
    By followingLoc = By.cssSelector(".stat-grid__left .folow");
    By newsLoc = By.xpath("//ul[@class='menu-major menu-major_main']/li[3]/a");
    By recordsLoc = By.xpath("//ul[@class='menu-major menu-major_main']/li[2]/a");

    By deleteIm = By.xpath("//div[@class='photo_edit__actions']/a[2]");
    By imageEditLoc = By.cssSelector(".photo_edit");

    //================to wait blocs
    By contactListLoc = By.xpath("//div[@class='contact-items']");
    By newsListLoc = By.id("news_block");
    By moneyAvail = By.xpath("//table[@class='table_payment_money']//tr[1]/td[2]/span[2]");
    By recordsListLoc = By.id("videos_list_block");
    //========================================================END LOCATORS=======================================================================



    /*public String getMyName(){
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(userName));
	return driver.findElement(userName).getText();
    }*/


    public String getTown() {
	return driver.findElement(townLoc).getText();
    }


    public String getInterest() {
	return driver.findElement(tagLoc).getText();
    }


    public String getInfo() {
	return driver.findElement(aboutLoc).getText();
    }


    public void noImageWait(){
    }


    public boolean isImagenotPresent(){		// assert if avatar is present. true = empty, false = present.
	return driver.findElements(noImageLoc).size()!= 0;
    }


    public void deleteImage(){
	Actions action = new Actions(driver);
	action.moveToElement(driver.findElement(imageEditLoc));
	driver.findElement(deleteIm).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.invisibilityOfElementLocated(imageEditLoc));		//wait image to be deleted
    }


    public ChatPage myMessages() {
	driver.findElement(mailLoc).click();
	return PageFactory.initElements(driver, ChatPage.class);
    }


    public ContactPage myFollowing() {
	if (driver.findElements(followingLoc).size()>0) {
	    driver.findElement(followingLoc).click();
	}
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(contactListLoc));		//wait for following page to load
	return PageFactory.initElements(driver, ContactPage.class);
    }


    public ContactPage myFollowers() {
	if (driver.findElements(followersLoc).size()>0) {
	    driver.findElement(followersLoc).click();
	}
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(contactListLoc));		//wait for following page to load
	return PageFactory.initElements(driver, ContactPage.class);
    }


    public NewsPage myNews() {
	if (driver.findElements(newsLoc).size()>0) {
	    driver.findElement(newsLoc).click();
	}
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(newsListLoc));
	return PageFactory.initElements(driver, NewsPage.class);
    }


    public RecordPage myVideos() {
	if (driver.findElements(recordsLoc).size()>0) {
	    driver.findElement(recordsLoc).click();
	}
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(recordsListLoc));		//wait for following page to load
	return PageFactory.initElements(driver, RecordPage.class);
    }


    public SearchPage  getSearchResult(String searchRequest) {
	waitForPageLoaded(driver);
	new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("main_search_js")));
	driver.findElement(By.id("main_search_js")).clear();
	driver.findElement(By.id("main_search_js")).sendKeys(searchRequest);
	driver.findElement(By.id("main_search_js")).sendKeys(Keys.ENTER);
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(By.id("search_results_block")));
	return PageFactory.initElements(driver, SearchPage.class);
    }


    public HangoutPage hangoutTab() {
	if (driver.findElement(hangTabStatus).getAttribute("class").contains("active")) {
	    return PageFactory.initElements(driver, HangoutPage.class);
	} else {
	    driver.findElement(scheduleClick).click();
	    waitForPageLoaded(driver);
	    return PageFactory.initElements(driver, HangoutPage.class);
	}
    }


    public HangoutPage recommendedTab() {
	if (driver.findElement(recomTabStatus).getAttribute("class").contains("active")) {
	    return PageFactory.initElements(driver, HangoutPage.class);
	} else {
	    avoidNotifyDisturb();
	    driver.findElement(recomClick).click();
	    waitForPageLoaded(driver);
	    new WebDriverWait(driver,10).until(ExpectedConditions.titleContains(DataProperties.get("advice.title")));
	    return PageFactory.initElements(driver, HangoutPage.class);
	}
    }


    /* public void profileMenu() {
	driver.manage().window().maximize();
	Actions builder = new Actions(driver);
	waitForPageLoaded(driver);
	builder.moveToElement(driver.findElement(headerProfile)).build().perform();
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(profileSett));
    }*/


    /* public SettingsPage openSettings() {
	profileMenu();
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(profileSett));
	driver.findElement(profileSett).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, SettingsPage.class);
    }
     */

    public WebinarPage startDemo() throws AWTException, InterruptedException {
	String hangNum = randomNumber();
	CreateHangoutPage create = createHangout();
	create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(5), "", DataProperties.get("timezone"));
	create.saveHangout();
	create.startHangout();
	return PageFactory.initElements(driver, WebinarPage.class);
    }


    public NotifyPage openNotify(){
	driver.findElement(showNotify).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, NotifyPage.class);
    }


    public void avoidNotifyDisturb(){
	openNotify();
	openNotify();
    }



    public MoneyPage goToMoney() {
	driver.findElement(headerMoney).click();
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(moneyAvail));
	return PageFactory.initElements(driver, MoneyPage.class);
    }




    public void assertMyPage(String userName){
	String title = driver.getTitle();
	String recommend = "Рекомендации";
	LOG.info("Assert that title : \""+title+"\" contains user name : "+userName);

	if (title.contains(userName)||title.contains(recommend)) Assert.assertTrue(true);
	else Assert.assertTrue(false);
    }


    public String getEmptyText() {
	LOG.info("Page has stub with text: "+driver.findElement(emptyText).getText());
	return driver.findElement(emptyText).getText();
    }


    public CreateHangoutPage createHangout() {
	driver.findElement(createHang).click();
	return  PageFactory.initElements(driver, CreateHangoutPage.class);
    }


    public SearchPage clickUserTag() {
	tagText = driver.findElement(firstUserTag).getText();
	driver.findElement(firstUserTag).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, SearchPage.class);
    }


    public SearchPage clickHangTag() {
	tagText = driver.findElement(firstHangTag).getText();
	driver.findElement(firstHangTag).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, SearchPage.class);
    }


    public Boolean userTagExist(){
	if (driver.findElements(firstUserTag).size()>0) return true;
	else return false;
    }







}
