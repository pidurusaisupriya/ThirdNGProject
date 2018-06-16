package thirdNGproject;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

@SuppressWarnings("unused")
public class ThirdNGProject {
	
	public String driverPath = Utils3.DRIVER_PATH;
	public String baseUrl = Utils3.BASE_URL;
	public WebDriver driver;
	
@DataProvider(name = "Test")
public String[][] testData() throws Exception{
	
	return Utils3.getDataFromExcel(Utils3.FILE_PATH, Utils3.SHEET_NAME,
			Utils3.TABLE_NAME);
}

	
  @BeforeMethod
  public WebDriver openHomePage(){
	  
	  System.setProperty(Utils3.DRIVER_NAME, driverPath);
	  
	   driver = new ChromeDriver();
	  
	 driver.get(baseUrl);
	 driver.manage().timeouts()
		.implicitlyWait(Utils3.WAIT_TIME, TimeUnit.SECONDS);
	  return driver;
	  
	  
	  }

  
  @Test(dataProvider="Test")
public void testCase04(String username, String password) throws Exception{
	  	
	  	String actualBoxMsg;
	  	String actualTitle;
	  	
	  	
		// Enter Username
		driver.findElement(By.name("uid")).clear();
	    driver.findElement(By.name("uid")).sendKeys(username);
	   
	    // Enter Password
	    driver.findElement(By.name("password")).clear();
	    driver.findElement(By.name("password")).sendKeys(password);
	    
	    getscreenshot(username+"_"+password);
		 
	    
	    // Login 
	    driver.findElement(By.name("btnLogin")).click();
	    
	    
	 
	    
	    try{ 
		    
	       	Alert alt = driver.switchTo().alert();
			actualBoxMsg = alt.getText(); // get content of the Alter Message
			alt.accept();
			 // Compare Error Text with Expected Error Value					
			assertEquals(actualBoxMsg,Utils3.EXPECT_ERROR);
			
		}    
	    catch (NoAlertPresentException Ex){ 
	    	actualTitle = driver.getTitle();
			// On Successful login compare Actual Page Title with Expected Title
	    	assertEquals(actualTitle,Utils3.EXPECT_TITLE);
	    	String searchID = "//td[. = 'Manger Id : "+Utils3.EXPECT_ID+"']";
	    	//String actualId = driver.findElement(By.xpath("//td[. = 'Manger Id : mngr137052']")).getText();
	    	String actualId = driver.findElement(By.xpath(searchID)).getText();
	    	if(actualId.contains(Utils3.EXPECT_ID)) {
	    		System.out.println("Id is correct");
	    	}else {
	    		System.out.println("Id is incorrect");
	    	}
	    	
	    	
	    	
        } 
	  
  }
  
  
  public void getscreenshot(String name) throws Exception 
  {
          File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
       //The below method will save the screen shot in d drive with name "screenshot.png"
         FileUtils.copyFile(scrFile, new File("./screenshots/"+name+".png"));
  }

  
	  
@AfterMethod
 public void closeHomePage() {
	
	  
	  driver.quit();
  }
}
