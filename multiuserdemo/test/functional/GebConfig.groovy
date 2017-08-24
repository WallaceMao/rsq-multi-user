/*
	This is the Geb configuration file.

	See: http://www.gebish.org/manual/current/#configuration
*/


import com.rishiqing.test.functional.ConfigUtil
import org.openqa.selenium.chrome.ChromeDriver

def path = "${new File('.').getAbsolutePath()}${File.separator}test${File.separator}functional${File.separator}chromedriver.exe"
println "-----$path"

driver = {
    System.setProperty("webdriver.chrome.driver",
            path)
    new ChromeDriver()
}

waiting {
    timeout = 20
    retryInterval = 0.5
}

//  不缓存driver
//cacheDriver = false

//  自动清除cookie
autoClearCookies = true

baseNavigatorWaiting = true
atCheckWaiting = true

baseUrl = "${ConfigUtil.config.baseUrl}${ConfigUtil.config.path}"