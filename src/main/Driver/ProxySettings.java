package main.Driver;

import java.io.IOException;
import java.util.Random;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ProxySettings {
	
	private WebDriver driver;
	
	public ProxySettings(WebDriver driver) {
		 this.driver = driver;
	 }
	 
	 public WebDriver proxyDriver(String browser) throws IOException
	 {
		 
		 String [] arr={"142.91.207.38","108.62.246.188","23.19.213.54","46.251.231.5","31.214.185.3","78.157.195.142"}; //рандомный выбор прокси ( volodymyrbryliant DpxWAasm )
		 Random randomProx = new Random();
		 int arrNum = randomProx.nextInt(arr.length);
		 if (browser.equals("firefox"))
			 {
		     	// File file = new File(DataProperties.path("jsError.file"));
			 ProfilesIni profile = new ProfilesIni();      
			 FirefoxProfile ffprofile = profile.getProfile("default");       //берем стандартный профиль , где сохранены пароль/логин для прокси через autoauth volodymyrbryliant:DpxWAasm
			 JavaScriptError.addExtension(ffprofile);
			 ffprofile.setEnableNativeEvents(true);
			 Proxy proxy = new Proxy();
			 proxy.setHttpProxy(arr[arrNum]+":15461");
			 DesiredCapabilities capabilities = new DesiredCapabilities();
			 capabilities.setBrowserName(browser);
			 capabilities.setCapability(CapabilityType.PROXY, proxy);  
			 capabilities.setCapability(FirefoxDriver.PROFILE, ffprofile); 
			 return driver = WebDriverFactory.getDriver(capabilities);
			 }
		 else return driver = null;	 
	 }

}
