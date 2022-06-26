import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class project2 {



    public static void main(String[] args) throws InterruptedException, IOException {


        System.setProperty("webdriver.chrome.driver", "/Users/afaggadimova/Desktop/untitled folder/driver/chromedriver");


        WebDriver driver = new ChromeDriver();


        driver.navigate().to("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");

        driver.findElement(By.name("ctl00$MainContent$username")).sendKeys("Tester");
        driver.findElement(By.name("ctl00$MainContent$password")).sendKeys("test");
        driver.findElement(By.id("ctl00_MainContent_login_button")).click();

        driver.findElement(By.xpath("//a[@href='Process.aspx']")).click();

        List<String[]> content = readFromCSV("MOCK_DATA.csv");

        String[] user = content.get(randNumber(0, content.size()));

        int quantity = randNumber(1, 100);

        driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity")).sendKeys("" + quantity);
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        double total = Double.parseDouble(driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtTotal")).getAttribute("value"));

        if (quantity >= 10) {
            Assert.assertTrue(total == (quantity * 100 * 0.92));
        } else {
            Assert.assertTrue(total == (quantity * 100));
        }

        driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtName")).sendKeys(user[1] + " " + user[2]);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox2")).sendKeys(user[3]);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox3")).sendKeys(user[4]);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox4")).sendKeys(user[5]);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox5")).sendKeys(user[6]);

        List<WebElement> cards = driver.findElements(By.xpath("//input[@name='ctl00$MainContent$fmwOrder$cardList']"));
        int cardChoiceRand = randNumber(0, cards.size());
        WebElement chosenCard = cards.get(cardChoiceRand);
        chosenCard.click();

        String currentCard = chosenCard.getAttribute("value");

        if (currentCard.equals("Visa")) {
            driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys("4" + (long) (Math.random() * 999_999_999_999_999L));
        } else if (currentCard.equals("MasterCard")) {
            driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys("5" + (long) (Math.random() * 999_999_999_999_999L));
        } else {
            driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys("3" + (long) (Math.random() * 99_999_999_999_999L));
        }

        int expirationMonth = (int) (1 + Math.random() * 9);

        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox1")).sendKeys("0" + expirationMonth + "/23");
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();

        Thread.sleep(250);

        String actualPageSource = driver.getPageSource();
        String expectedPageSourceContent = "New order has been successfully added.";
        Assert.assertTrue(actualPageSource.contains(expectedPageSourceContent));
        driver.quit();


    }

    public static int randNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static List<String[]> readFromCSV(String filePath) throws IOException {

        List<String[]> fileContent = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(filePath)));

            String line = br.readLine();// skip the first line

            while ((line = br.readLine()) != null) {
                fileContent.add(line.split(","));
            }

            return fileContent;
        } catch (FileNotFoundException e) {
            System.out.println("The given file does not exist.");
        } finally {
            br.close();
        }

        return fileContent;

    }
}

