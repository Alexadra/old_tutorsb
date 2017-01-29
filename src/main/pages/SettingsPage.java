package main.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class SettingsPage extends BaseClass {
    private WebDriver driver;
    public SettingsPage(WebDriver driver) {
	this.driver = driver;
    }


    //========================================================LOCATORS==========================================================================
    By nameInputLoc = By.id("teacher_config_name");
    By urlInputLoc = By.id("teacher_config_short_url");
    By townInputLoc = By.id("teacher_config_city_name");
    By aboutInputLoc = By.id("teacher_config_info");
    By tagInputLoc = By.id("tags-input");
    By langInput = By.xpath("//div[@id='teacher_config_language']/a");
    By submitSettLoc = By.cssSelector("input[type='submit']");

    By accountGo = By.xpath("//ul[@class='menu-major']/li[2]/a");
    By activationGo = By.xpath("(//a[contains(@href, '/config/account')])[3]");
    By mainGo = By.xpath("//ul[@class='menu-major menu-major_main']/li[1]/a");

    By settEmail = By.id("main_config_email");
    By settPass = By.id("main_config_password");
    By settSubmit = By.xpath("//input[@type='submit']");
    By urlMess = By.id("teacher_config_short_url_error");
    By errorBadPass = By.id("main_config_password_error");
    By errorBadEmail = By.id("main_config_email_error");

    By bigName = By.cssSelector(".page-profile .userpic-user__name");
    //========================================================END LOCATORS======================================================================

    public MyPage inputSett(String name, String town, String interest, String url, String info) {
	driver.findElement(nameInputLoc).clear();
	driver.findElement(nameInputLoc).sendKeys(name);
	/*driver.findElement(townInputLoc).clear();
	driver.findElement(townInputLoc).sendKeys(town);
	if (town!=null) {
	    new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='autocomplete']/div[contains(@title,'"+town+"')]")));
	    driver.findElement(By.xpath("//div[@class='autocomplete']/div[contains(@title,'"+town+"')]")).click();
	}
	setTags(interest);
	driver.findElement(aboutInputLoc).clear();
	driver.findElement(aboutInputLoc).sendKeys(info);*/
	driver.findElement(urlInputLoc).clear();
	driver.findElement(urlInputLoc).sendKeys(url);
	submitSettings();
	return PageFactory.initElements(driver, MyPage.class);
    }

    public void setTags(String interest){
	driver.findElement(tagInputLoc).sendKeys(Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE);
	driver.findElement(tagInputLoc).sendKeys(interest);
    }


    public void submitSettings(){
	driver.findElement(submitSettLoc).click();
	waitForPageLoaded(driver);
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(bigName));
    }


    public void inputSettNoname(String name) {
	driver.findElement(nameInputLoc).clear();
	driver.findElement(nameInputLoc).sendKeys(name);
	driver.findElement(submitSettLoc).click();
    }


    public void changeLanguage(String lang){
	driver.findElement(langInput).click();
	waitForPageLoaded(driver);
	driver.findElement(By.xpath("//div[@class='dropdown-list dropdown-list_right']/a[text()='"+lang+"']")).click();		//click to choose lang
	waitForPageLoaded(driver);
    }


    public String errorName(String errorNoName) {
	new WebDriverWait(driver,20).until(ExpectedConditions.
		textToBePresentInElement(By.id("teacher_config_name_error"), errorNoName));
	return driver.findElement(By.id("teacher_config_name_error")).getText();
    }

    public void inputSettBadUrl(String name, String url) {
	driver.findElement(nameInputLoc).clear();
	driver.findElement(nameInputLoc).sendKeys(name);
	driver.findElement(urlInputLoc).clear();
	driver.findElement(urlInputLoc).sendKeys(url);
	driver.findElement(submitSettLoc).click();
    }

    public String errorUrl(String errorMess) {
	new WebDriverWait(driver,20).until(ExpectedConditions.
		textToBePresentInElement((urlMess), errorMess));
	return driver.findElement(urlMess).getText();
    }

    public void accountSett() {
	driver.findElement(accountGo).click();
	waitForPageLoaded(driver);
    }


    public void inputAccountData(String email, String pass) {
	changeEmail(email);
	changePass(pass);
	submitAccountSett();
    }


    public void changeEmail(String email){
	driver.findElement(settEmail).clear();
	driver.findElement(settEmail).sendKeys(email);
    }


    public void changePass(String pass){
	driver.findElement(settPass).clear();
	driver.findElement(settPass).sendKeys(pass);
    }


    public void submitAccountSett(){
	driver.findElement(settSubmit).click();
	waitForPageLoaded(driver);
    }


    public String errorEmail() throws InterruptedException {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(errorBadEmail));
	return driver.findElement(errorBadEmail).getText();
    }

    public String errorPass() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(errorBadPass));
	return driver.findElement(errorBadPass).getText();
    }


    public void assertSettPage() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(nameInputLoc));
    }


    public void assertAccountPage() {
	new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfElementLocated(settEmail));
    }




}
