package main.Driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opera.core.systems.OperaDriver;

public class WebDriverFactory {

    // Factory settings

    private static Logger LOG = LoggerFactory.getLogger(WebDriverFactory.class);
    private static String defaultHub = null; // change to  "http://myserver:4444/wd/hub" to use remote webdriver by default
    private static int restartFrequency = 50;


    public static void setDefaultHub(String newDefaultHub) {
	defaultHub = newDefaultHub;
    }


    public static void setRestartFrequency(int newRestartFrequency) {
	restartFrequency = newRestartFrequency;
    }

    // Factory

    private static String key = null;
    private static int count = 0;
    private static WebDriver driver;


    public static WebDriver getDriver(String hub, Capabilities capabilities) {
	String newKey = capabilities.toString() + ":" + hub;
	count++;
	LOG.debug("Requested driver: " + newKey + ". Driver uses count = "+count);
	// 1. WebDriver instance is not created yet
	if (driver == null) {
	    return newWebDriver(hub, capabilities);
	}

	// 2. Different flavour of WebDriver is required
	if (!newKey.equals(key)) {
	    LOG.debug("Replacing driver " + key + " with a new one " + newKey);
	    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	    dismissDriver();
	    key = newKey;
	    return newWebDriver(hub, capabilities);
	}

	// 3. Browser is dead
	try {
	    LOG.debug("getDriver: Current url: "+driver.getCurrentUrl());
	    driver.getCurrentUrl();
	} catch (Throwable t) {
	    LOG.debug("Browser is dead, starting a new one ");
	    t.printStackTrace();
	    return newWebDriver(hub, capabilities);
	}


	// 4. It's time to restart
	if (count >= restartFrequency) {
	    LOG.debug("Restarting driver after " + count + " uses");
	    dismissDriver();
	    return newWebDriver(hub, capabilities);
	}

	// 5. Just use existing WebDriver instance
	return driver;
    }


    public static WebDriver getDriver(Capabilities capabilities) {
	return getDriver(defaultHub, capabilities);
    }


    public static void dismissDriver() {
	try {
	    if (driver != null) {
		LOG.debug("Killing driver " );
		driver.quit();
	    }
	} catch (Exception e) {
	    LOG.warn("Driver killing error", e);
	} finally {
	    driver = null;
	    key = null;
	}
    }


    // Factory internals

    private static WebDriver newWebDriver(String hub, Capabilities capabilities) {
	driver = (hub == null) ? createLocalDriver(capabilities) : createRemoteDriver(hub, capabilities);
	key = capabilities.toString() + ":" + hub;
	count = 1;
	return driver;
    }


    private static WebDriver createRemoteDriver(String hub, Capabilities capabilities) {
	try {
	    return new EventFiringWebDriver(new RemoteWebDriver(new URL(hub), capabilities)).register(new MyWebDriverEventListener());
	} catch (MalformedURLException e) {
	    LOG.error("Could not start remote driver", e);
	    throw new Error("Could not connect to WebDriver hub", e);
	}
    }


    private static WebDriver createLocalDriver(Capabilities capabilities) {
	String browserType = capabilities.getBrowserName();
	try {
	    if (browserType.equals("firefox")) {
		FirefoxProfile profile = new FirefoxProfile();
		JavaScriptError.addExtension(profile);
		profile.setEnableNativeEvents(true);
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(FirefoxDriver.PROFILE, profile);
		dc.merge(capabilities);
		EventFiringWebDriver driver =  new EventFiringWebDriver(new FirefoxDriver(dc));
		driver.register(new MyWebDriverEventListener());
		return driver;
	    }
	    if (browserType.startsWith("internet explorer"))
		return new EventFiringWebDriver(new InternetExplorerDriver(capabilities)).register(new MyWebDriverEventListener());
	    if (browserType.equals("chrome"))
	    { System.setProperty("webdriver.chrome.driver", "/home/alexandra/Testing/libraries/chromedriver");
	    return new EventFiringWebDriver(new ChromeDriver(capabilities)).register(new MyWebDriverEventListener());
	    }
	    if (browserType.equals("opera"))
		return new EventFiringWebDriver(new OperaDriver(capabilities)).register(new MyWebDriverEventListener());
	} catch (Throwable e) {
	    LOG.error("Could not start local driver", e);
	    throw new Error("Could not start local driver", e);
	}
	LOG.error("Unrecognized browser type: " + browserType);
	throw new Error("Unrecognized browser type: " + browserType);
    }


    static {
	Runtime.getRuntime().addShutdownHook(new Thread() {	//before killing jvm, shutdownHook starts thread. vm will work till all the code in thread executes
	    @Override
	    public void run() {
		dismissDriver();
	    }
	});
    }
}
