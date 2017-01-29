package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.java.LogTest;

public class NewsPage {
    private WebDriver driver;
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);

    public NewsPage (WebDriver driver) {
	this.driver = driver;
    }

    //========================================================LOCATORS==========================================================================
    //=================
    By newsBlock = By.id("news_block");
    By firstNewsTitle = By.xpath("(//a[@class='notification__item-title'])[1]");
    //========================================================END LOCATORS======================================================================

    public String newNewsName() {
	assertNewsPage();
	return driver.findElement(firstNewsTitle).getText();
    }

    public void assertNewsPage(){
	new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfElementLocated(newsBlock));
    }


}
