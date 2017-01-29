package main.Driver;



import java.util.Collection;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyWebDriverEventListener implements WebDriverEventListener { //
 
    private static Logger LOG = LoggerFactory.getLogger(MyWebDriverEventListener.class); 
    private By lastFindBy;
    private String originalValue;

    
    public void beforeNavigateTo(String url, WebDriver selenium){
	LOG.info("WebDriver navigating to:'"+url+"'");
    }
    
    
    public void beforeChangeValueOf(WebElement element, WebDriver selenium){
        originalValue = elementDescription(element);
    }
 
    
    public void afterChangeValueOf(WebElement element, WebDriver selenium){
    	LOG.debug("WebDriver changing value in element found "+lastFindBy+" from '"+originalValue);
    }
 
    
    public void beforeFindBy(By by, WebElement element, WebDriver selenium) {
    	lastFindBy = by;
    }
    
    
    public void beforeClickOn(WebElement element, WebDriver selenium) {
	originalValue = elementDescription(element);
    	LOG.debug("Webdriver will click on element " + elementDescription(element) );
    }
    
    
    public void afterClickOn(WebElement element, WebDriver selenium) {
	LOG.debug("hope Webdriver clicked on element " + originalValue);
    }
 
	 
    public void onException(Throwable error, WebDriver driver) {
	if (error.getClass().equals(NoSuchElementException.class)) {
	    LOG.error("________________________________________________________________________________________________________________");
	    LOG.error("WebDriver error: Element not found "+lastFindBy);
	    LOG.error("________________________________________________________________________________________________________________");
        } else {
            LOG.error("________________________________________________________________________________________________________________");
            LOG.error("WebDriver error:", error);
            LOG.error("________________________________________________________________________________________________________________");
            @SuppressWarnings("unchecked")
            final Collection <Object> errors = (Collection<Object>) ((JavascriptExecutor) driver).executeScript("return window.JSErrorCollector_errors.pump()");
            if (errors.size()>0) {
        	int i = 1;
        	for (Object jsError:errors) {
        	    LOG.error("------------------------js error--------------------");
        	    LOG.error("javascript error "+i+"."+jsError.toString());
        	    LOG.error("________________________________________________________________________________________________________________");
        	    i++;
		}
            }
        }
    }
    
   
    
    private String elementDescription(WebElement element) {
	String description = "tag:" + element.getTagName();
        if (element.getAttribute("id") != null) {
            description += " |id: " + element.getAttribute("id");
        } else if (element.getAttribute("class") != null) {
            description += " |class: " + element.getAttribute("class");
        } else if (element.getAttribute("value") != null){
            description += " |value: " + element.getAttribute("value");
        }
        description += " |text: ('" + element.getText() + "')";
        return description;
    }
    
    
    @SuppressWarnings("unchecked")
    public void jsErrors(WebDriver driver) throws InterruptedException {
	Thread.sleep(3000);
	final Collection <Object> errors = (Collection<Object>) ((JavascriptExecutor) driver).executeScript("return window.JSErrorCollector_errors.pump()");
	if (errors.size()>0) {
	    int i = 1;
	    for (Object jsError:errors) {
		LOG.error("------------------------------------------------------------js error------------------------------------------------------------");
		LOG.error("javascript error "+i+"."+jsError.toString());
		LOG.error("------------------------------------------------------------js error------------------------------------------------------------");
		i++;
	    }
	    return;
	}
	else return;
    }
 
    
    public void beforeNavigateBack(WebDriver selenium){}
    public void beforeNavigateForward(WebDriver selenium){}
    
    public void beforeScript(String script, WebDriver selenium){}
    
    public void afterFindBy(By by, WebElement element, WebDriver selenium){}
    public void afterNavigateBack(WebDriver selenium){}
    public void afterNavigateForward(WebDriver selenium){}
    public void afterNavigateTo(String url, WebDriver selenium){}
    public void afterScript(String script, WebDriver selenium){}
 
}