package test.java;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import main.pages.BaseClass;
import main.pages.ContactPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;
import main.pages.SearchPage;
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

//20.03.2013 All passed
public class ContactsTest extends BaseClass {
    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");


    @BeforeMethod
    public void setup() throws IOException {
	LOG.info("______________________________________"+this.getClass()+"______________________________________");
	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setBrowserName("firefox");
	driver = WebDriverFactory.getDriver(capabilities);
	if (url.contains("testun")) driver.get(DataProperties.get("urlcookie"));
	driver.get(url);
	checkUnloginned(driver);
	selectLang(DataProperties.get("language"), driver);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterMethod (alwaysRun = true)
    public void unLogin() throws Exception {
	LOG.info("_______________________________@AfterMethod_______________________________");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	MyPage my = PageFactory.initElements(driver, MyPage.class);
	my.exit();
    }


    @Test
    public void contactEditFromPage() {
	LOG.info("_______________________________Test = contactEditFromPage()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	my.avoidNotifyDisturb();
	SearchPage search = my.getSearchResult (DataProperties.get("name.toadd"));		//search any hangout of this user
	UserPage user = search.goToFirstUser();		//go to the user page
	user.addContact();		//add user to following from user page
	home.home();		//click on logo
	ContactPage contact = my.myFollowing();		//go to my following
	Assert.assertTrue(contact.getFirstName().contains(DataProperties.get("name.toadd")),"Added contact name isn't correct");		//assert that chosen user was added to following
	contact.goToFirstUser();		//go to the user page
	user.removeContact();		//remove chosen user from following
	home.home();		//click on logo
	my.myFollowing();		//go to my following
	Assert.assertFalse(contact.assertAllUsers(DataProperties.get("name.toadd")));		//assert that user was deleted from my followings
    }


    @Test
    public void contactEditFromContacts() throws InterruptedException {
	LOG.info("_______________________________Test = contactEditFromContacts()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password")); //вход на сайт , выбор языка , логин, пароль
	my.avoidNotifyDisturb();
	ContactPage contact = my.myFollowing();
	String userLink = contact.getFirstUserLink();		//save first user link, to go to user page
	String userName = contact.getFirstName();		//save user name, to assert that user was deleted
	contact.removeFirstUser();		//delete user from followings
	driver.navigate().refresh();
	Assert.assertFalse(contact.assertAllUsers(userName));  //assert that user was deleted from following
	driver.get(userLink);		//go to user page to add this user back
	UserPage user = PageFactory.initElements(driver, UserPage.class);
	user.addContact();		//add user
	home.home();		//click on logo
	my.myFollowing();		//go to my following
	Assert.assertEquals(contact.getFirstName(), userName);		//assert that chosen user was added to following
    }


    @Test
    public void editingMeFromContactPage() throws AWTException {
	LOG.info("_______________________________Test = editingMeFromContactPage()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	String myName = my.getMyName();
	my.avoidNotifyDisturb();
	driver.get(url+DataProperties.get("url.login2"));
	UserPage user = PageFactory.initElements(driver, UserPage.class);
	user.addContact();		//add user to following from user page
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	home.home();		//click on logo
	ContactPage contact = my.myFollowers();		//go to my following
	Assert.assertEquals(contact.getFirstName(), myName);		//assert that chosen user was added to following
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login1"), DataProperties.get("valid.password"));
	driver.get(url+DataProperties.get("url.login2"));
	user.removeContact();		//remove chosen user from following
	my.exit();
	home.loginClick();
	login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	my.myFollowers();
	Assert.assertFalse(contact.assertAllUsers(myName));		//assert that user was deleted from my followings
    }


    @Test
    public void emptyFollowing(){
	LOG.info("_______________________________Test = emptyFollowing()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("clean.login"), DataProperties.get("valid.password"));
	my.myFollowing();
	Assert.assertTrue(my.getEmptyText().contains(DataProperties.get("empty.following")),"Empty page must contain text about no contacts were found");
    }


    @Test
    public void emptyFollowers(){
	LOG.info("_______________________________Test = emptyFollowers()_______________________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick(); 		//login form go
	MyPage my = login.userLogin(DataProperties.get("clean.login"), DataProperties.get("valid.password"));
	my.myFollowers();
	Assert.assertTrue(my.getEmptyText().contains(DataProperties.get("empty.followers")),"Empty page must contain text about no contacts were found");
    }


}
