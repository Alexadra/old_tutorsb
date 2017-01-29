package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NotifyPage {
    
    private WebDriver driver;
    
    public NotifyPage(WebDriver driver) {
	this.driver = driver;
    }
    
    
    By firstNotific = By.xpath("//div[@class='dropdown__notification'][1]");
    
    
    public String getFirstNotific(){
	return driver.findElement(firstNotific).getText();
    }

}
