package Cotizacion;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import com.borland.silktest.jtf.Desktop;
import com.borland.silktest.jtf.xbrowser.BrowserApplication;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class PruebaExtent {
	private Desktop desktop = new Desktop();

static ExtentTest test;
static ExtentReports report;
@BeforeClass
public static void startTest()


{
report = new ExtentReports(System.getProperty("user.dir\\CotizaAutoGNP\\CotizaAutoGNP-report\\")+"ExtentReportResults.html");

//C:\Users\QAIT-0069\eclipse-workspace\CotizaAutoGNP\CotizaAutoGNP-report
test = report.startTest("ExtentDemo");
}
@Test
public void extentReportsDemo()
{
//System.setProperty("webdriver.chrome.driver", "D:SubmittalExchange_TFSQAAutomation3rdpartychromechromedriver.exe");
WebDriver driver = desktop.<BrowserApplication> find("//BrowserApplication").getWebDriver();

driver.get("https://www.google.co.in");
if(driver.getTitle().equals("Google"))
{
test.log(LogStatus.PASS, "Navigated to the specified URL");
}
else
{
test.log(LogStatus.FAIL, "Test Failed");
}
}
@AfterClass
public static void endTest()
{
report.endTest(test);
report.flush();
}
}