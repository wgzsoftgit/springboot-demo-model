package com.demomodel.utils.fuzhu.goShopping;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

 
public class myThread3 extends Thread{
	
	private String name;
	public myThread3(String name){
		this.name = name;
	}
	public void run(){
		
		// 小米官网抢购红米k30pro     https://www.mi.com/buy/detail?product_id=12605
		//https://www.mi.com/buy/detail?product_id=10000233
		String baseurl = "https://www.mi.com/buy/detail?product_id=10000233";    
		// 小米官网登录信息
    	String baseurl2 = "https://account.xiaomi.com/pass/serviceLogin?callback=http%3A%2F%2Forder.mi.com%2Flogin%2Fcallback%3Ffollowup%3Dhttp%253A%252F%252Fitem.mi.com%252Fbuyphone%252Fnote3%26sign%3DZmYyZmU3NmE0NWNmMjZkYzk5YmU5ZWJjMzc2Zjg1YzNhMjY3NTJjMg%2C%2C&sid=mi_eshop";
    	WebElement loginElement = null;
    	WebElement element = null;
    	List<WebElement> elements = null;
    	List<WebElement> taocan = null;
    	WebElement btn = null;
 //webdriver.gecko.drive  webdriver.firefox.marionette
    	System.setProperty("webdriver.gecko.driver","C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");
//    	  DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//    	    capabilities.setCapability("marionette", true);
//    	
    	 //下面的语句用来启动带插件的火狐浏览器
        ProfilesIni allProfiles = new ProfilesIni();
        FirefoxProfile profile = allProfiles.getProfile("default");
        
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        
       		
       //创建一个叫driver的对象，启动火狐浏览器  
       WebDriver driver = new FirefoxDriver(options);
    	    
    	//WebDriver driver = new FirefoxDriver();
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // 设置隐式等待10秒钟
     //最大化浏览器窗口
     //  driver.manage().window().maximize();
		System.out.println("starting:");
    	driver.get(baseurl2);
    	
    	driver.findElement(By.id("username")).sendKeys("18837137753");   // 输入自己小米官网登录账号
    	driver.findElement(By.id("pwd")).sendKeys("w2213858206");    // 输入小米官网登录密码
    	driver.findElement(By.id("login-button")).click();
    	
    	System.out.println(driver.getCurrentUrl());
    	
    	try {
			sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	driver.navigate().to(baseurl);    
    	System.out.println("hello world!!");
    	
    	
    	
    	elements =  driver.findElements(By.xpath("//*[@class=\"option-box\"][div=\"选择产品\"]/ul/li"));
    	if(elements == null || elements.size() == 0){
			driver.close();//关闭
		}
	System.err.println(elements.size() +"==>"+1);
		elements.get(1).click();           // 选择高配版 3GB内存 + 32GB容量
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		elements = driver.findElements(By.xpath("//*[@class=\"option-box\"][div=\"选择版本\"]/ul/li"));    
		System.err.println(elements.size()+"==>"+0);
		elements.get(0).click();            // 选择双网通
		
elements = driver.findElements(By.xpath("//*[@class=\"option-box\"][div=\"选择颜色\"]/ul/li"));
    	
    	System.err.println(elements.size()+"==>"+0);
		elements.get(0).click();   // 选择 白0  极夜黑1    薄荷绿2

		
		elements = driver.findElements(By.className("sale-btn")); 
		System.out.println(elements.size()+"==>"+0);
		elements.get(0).click();                // 点击下一步抢购按钮  等待
		//driver.quit(); //退出
		
	}
}