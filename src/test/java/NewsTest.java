package test.java;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.CreateHangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.NewsPage;
import main.pages.UserPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import main.Driver.DataProperties;
import  main.Driver.WebDriverFactory;

public class NewsTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");


    @BeforeMethod
    public void setup() {
	LOG.info("______________________________________"+this.getClass()+"______________________________________");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	checkUnloginned(driver);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws AssertionError, Exception {
	LOG.info(">>>>>>>>>>@AfterMethod<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	MyPage my = PageFactory.initElements(driver, MyPage.class);
	my.exit();
    }


    @Test
    public void newsAdd() throws AWTException, InterruptedException {
	/*login by user 1, add to contact user 2 . unlogin. Login by user 2 , create hangout. Unlogin.
	 Login by user 1. Assert that created hangout was added to news tab (assert hangout title)*/
	LOG.info(">>>>>>>>>>>>>>>Test = newsAdd()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	if (url.contains("tutorsband")) driver.get(url+DataProperties.get("url.login2"));
	else driver.get(url+DataProperties.get("url.login2"));
	UserPage user = PageFactory.initElements(driver, UserPage.class);
	user.addContact();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	String hangNum = randomNumber();
	CreateHangoutPage create = my.createHangout();
	create.hangoutData ("free", DataProperties.get("freehang.name")+hangNum, DataProperties.get("freehang.text"),
		getTodayData(), timeFuture(20), DataProperties.get("sett.interest"), DataProperties.path("file.name"),"");
	create.saveHangout();
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	home.home();
	NewsPage news = my.myNews();
	Assert.assertEquals(news.newNewsName(), DataProperties.get("freehang.name")+hangNum);
    }


    @Test
    public void emptyNews(){
	LOG.info(">>>>>>>>>>>>>>>Test = emptyNews()<<<<<<<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("clean.login"), DataProperties.get("valid.password"));
	my.myNews();
	Assert.assertTrue(my.getEmptyText().contains(DataProperties.get("empty.news")),"Empty page must contain text about no news were found");
    }


    /*@Test
	public void exec() throws InterruptedException
	{

		String s;

		String[] term = new String[]{"xterm", "'java --version'"};
		try
		{
			Process proc = Runtime.getRuntime().exec(term);
			proc.waitFor();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while((s = stdInput.readLine())!= null)
			{
				System.out.println("wow"+s);
			}
			//proc.destroy();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}*/

}




