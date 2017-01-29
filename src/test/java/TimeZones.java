package test.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

import main.Driver.DataProperties;
import main.Driver.WebDriverFactory;
import main.pages.BaseClass;
import main.pages.CreateHangoutPage;
import main.pages.HomePage;
import main.pages.LoginPage;
import main.pages.MyPage;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeZones extends BaseClass {

    private static Logger LOG = LoggerFactory.getLogger(LogTest.class);
    WebDriver driver;
    String url = DataProperties.get("url");
    public int counter;


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
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }


    /* @AfterMethod (alwaysRun = true)
    public void unLogin() throws AssertionError, Exception {
	LOG.info(">>>>>>>>>>@AfterMethod<<<<<<<<<");
	HomePage home = PageFactory.initElements(driver,HomePage.class);
	home.jsErrorExist();
	home.findTextError();		//check *** doesn't exists
	home.failIfTextError();
	MyPage my = PageFactory.initElements(driver, MyPage.class);
	my.exit();
    }*/


    private String getRequest(String url) throws Exception {
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");
	if (con.getResponseCode() != 200 && con.getResponseCode() != 201 ) return "";

	//add request header
	//con.setRequestProperty("User-Agent", USER_AGENT);

	BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
	    response.append(inputLine);
	}
	in.close();

	//print result
	return response.toString();
    }


    public String getLocation(String url, String city) throws Exception {
	String response = getRequest(url + city);
	System.out.println("chance " + counter);
	try{
	    if (response == "") return "";
	    String location = response.toString().split("location", 2)[1].split("}",2)[0];

	    String lat = location.split(":",4)[2].split(",",2)[0].trim();
	    String lng = location.split(":",4)[3].trim();
	    return lat + "," + lng;
	} catch(ArrayIndexOutOfBoundsException e){
	    System.out.println(e);
	    Thread.sleep(500);
	    counter++;
	    if (counter < 2) return getLocation(url, city);
	    return "";
	}
    }


    public String getTime(String url, String timestamp) throws Exception{
	String response = getRequest(url);
	System.out.println(response);
	long dstOffset = Long.parseLong(response.split("dstOffset\" : ",2)[1].split(",",2)[0].trim());
	long rawOffset = Long.parseLong(response.split("rawOffset\" : ",2)[1].split(",",2)[0].trim());
	long newe = Long.parseLong(timestamp) +dstOffset +rawOffset;
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(newe*1000);
	SimpleDateFormat sdf = new SimpleDateFormat();
	sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
	sdf.applyPattern("HH:mm");
	System.out.println(sdf.format(calendar.getTime()));
	return sdf.format(calendar.getTime());
    }

    public String getCurrentTimestamp(){
	DateTime time = DateTime.now().plusMinutes(5);
	String s = String.valueOf(time.getMillis());
	System.out.println(time.getMillis() + "="+ s.substring(0,10));
	return s.substring(0,10);
    }


    //return cities list, gotten from create page
    public List<String> getCitiesList(){
	List <String> citiesText = new ArrayList<String>();
	List <WebElement> zones = driver.findElements(By.xpath("//a[@attr-timezone]"));
	for (WebElement zone:zones){
	    String zoneText = zone.getAttribute("text").split("\\)", 2)[1];
	    zoneText = zoneText.split("\\(", 2)[0];
	    int size = zoneText.split(",").length;
	    for (int i=0; i<size; i++){
		citiesText.add(zoneText.split(",")[i].trim());
	    }
	}
	return citiesText;
    }




    @Test
    public void timezoneTest() throws Exception{
	LOG.info("_______________________Test = freeHangoutCreation()_______________________");
	HomePage home = PageFactory.initElements(driver, HomePage.class);
	LoginPage login = home.loginClick();
	MyPage my = login.userLogin(DataProperties.get("valid.login2"), DataProperties.get("valid.password"));
	CreateHangoutPage create = my.createHangout();
	List <String> errorTz = new ArrayList<String>();
	for(String city:getCitiesList() ){
	    counter = 0;
	    String location = getLocation("http://maps.googleapis.com/maps/api/geocode/json?address=", city.replace(" ", ""));
	    if (location == "") continue;
	    String currTimestamp = getCurrentTimestamp();
	    String tzid = getTime("https://maps.googleapis.com/maps/api/timezone/json?location=" + location + "&timestamp="+currTimestamp+"&key=AIzaSyDrqH5a1w6dwyX-5j7THaWEuWn6G9LU3uI", currTimestamp);
	    System.out.println("Google time for " + city + " = "+ tzid);
	    create.setTimezone(city);
	    driver.findElement(By.id("hangout_time")).click();
	    String timeCurrent = driver.findElement(By.xpath("//ul[@id='hangout_hours']//li[1]")).getText().trim();
	    System.out.println("Tut time for " + city + " = "+ timeCurrent);
	    if(!tzid.equals(timeCurrent)) {
		errorTz.add(city + " Expected: " +tzid +" Found: " + timeCurrent);
		System.out.println("ERROR " + city);
	    }

	}
	if (!errorTz.isEmpty()){
	    throw new Error("Some cities have wrong timezone : " + errorTz);
	}




    }

}
