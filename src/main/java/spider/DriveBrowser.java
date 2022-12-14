package spider;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
public class DriveBrowser {
    public static void main(String[] args) {

        System.setProperty("webdriver.edge.driver","c:/drivers/msedgedriver.exe");

        WebDriver driver= new EdgeDriver();

        driver.get("https://www.baidu.com/");

        driver.manage().window().maximize();

//        driver.quit();
    }
}
