package main.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import test.java.LogTest;


public class SearchPage extends BaseClass{
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    private WebDriver driver;
    public SearchPage (WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================
    By userName = By.xpath("(//div[contains(@class,'plitka__author')])[1]");
    By authorLink = By.cssSelector(".paper__autor a");
    By firstHangLink = By.xpath("(//a[@class='plitka-link'])[1]");
    By firstHangName = By.xpath("(//div[@class='plitka__title'])[1]");
    By firstHangAuthor = By.xpath("(//div[@class='plitka-bottom']//span)[1]");
    By allAuthors = By.xpath("//div[@class='plitka-bottom']//span");
    By searchInput = By.id("main_search_js");
    By videoTab = By.cssSelector(".column__menu a.menu-major__item");
    By emptySearch = By.cssSelector(".empty__text.text__h3");
    By firstPaidLink = By.xpath("(//div[contains(text(),'TBM')]/ancestor::a)[1]");
    By firstFreeLink = By.xpath("//div[contains(text(),'Бесплатно')]//ancestor::a//div[@class='plitka__status-camera']//ancestor::a");
    By paidVideo = By.xpath("//div[@class='plitka__price']/ancestor::a");
    By searchPages = By.xpath("//nav[@class='pagination']/ul/*");
    //========================================================END LOCATORS======================================================================


    public UserPage goToFirstUser() {
	openFirstFoundHangout();
	//driver.findElement(authorLink).click();
	waitForPageLoaded(driver);
	return PageFactory.initElements(driver, UserPage.class);
    }


    public void videoSearch(String videoName){
	MyPage my = new MyPage(driver);
	my.getSearchResult(videoName);
	videoTab();
    }


    //find and open first free conference.Just future ones.
    public HangoutPage openFirstFreeActive() {
	driver.findElement(firstFreeLink).click();
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    //including finished conferences
    public HangoutPage openFirstFoundHangout(){
	driver.findElement(firstHangLink).click();
	return PageFactory.initElements(driver, HangoutPage.class);
    }


    public void videoTab() {
	driver.findElement(videoTab).click();
	waitForPageLoaded(driver);
    }


    public String getFirstResultName() {
	return driver.findElement(firstHangName).getText();
    }


    public String getFirstResultAuthor() {
	return driver.findElement(firstHangAuthor).getText();
    }





    public void assertAllAuthors(String name){
	List <WebElement> authors = driver.findElements(allAuthors);
	for (WebElement author : authors){
	    Assert.assertEquals(author.getText(), name);
	}
    }


    public void assertTagSearch(String tagText) {
	Assert.assertEquals(driver.findElement(searchInput).getAttribute("value"), tagText );
    }


    public String getEmptySearchMess() {
	return driver.findElement(emptySearch).getText();
    }


    public RecordPage openFirstPaidVideo() {
	List <WebElement> videos = driver.findElements(paidVideo);
	if (videos.size()!=0){
	    videos.get(0).click();
	    return PageFactory.initElements(driver, RecordPage.class);
	}
	LOG.info("No paid video was found");
	return PageFactory.initElements(driver, RecordPage.class);
    }


}

