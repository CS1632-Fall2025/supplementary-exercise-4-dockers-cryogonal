package edu.pitt.cs;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeDriver;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {

  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;


  @Before
  public void setUp() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }


  @After
  public void tearDown() {
    driver.quit();
  }


  @Test
  public void tEST1LINKS() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    {
      WebElement element = driver.findElement(By.linkText("Reset"));
      String attribute = element.getAttribute("href");
      vars.put("resetHref", attribute);
    }
    assertTrue(vars.get("resetHref").toString().endsWith("/reset"));
  }


  @Test
  public void tEST2RESET() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=true\";document.cookie = \"2=true\";document.cookie = \"3=true\";");
    driver.findElement(By.linkText("Reset")).click();
    driver.findElement(By.linkText("Catalog")).click();
    assertThat(driver.findElement(By.id("cat-id1")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.id("cat-id2")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.id("cat-id3")).getText(), is("ID 3. Mistoffelees"));
  }


  @Test
  public void tEST3CATALOG() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Catalog")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("ol li:nth-of-type(2) img[alt=\"Old Deuteronomy\"]"));
      String attribute = element.getDomProperty("src");
      vars.put("secondImgCat2", attribute);
    }
    assertEquals(vars.get("secondImgCat2").toString(), "http://localhost:8080/images/cat2.jpg");
  }


  @Test
  public void tEST4LISTING() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Catalog")).click();
    vars.put("catCount", driver.findElements(By.xpath("//div[@id=\'listing\']/ul/li")).size());
    assertEquals(vars.get("catCount").toString(), "3");
    {
      List<WebElement> elements = driver.findElements(By.id("cat-id3"));
      assert(elements.size() > 0);
    }
    List<WebElement> cats = driver.findElements(By.xpath("//div[@id='listing']/ul/li"));
    WebElement thirdCat = cats.get(2);
    assertThat(thirdCat.getText(), is("ID 3. Mistoffelees"));  
}


  @Test
  public void tEST5RENTACAT() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[@onclick=\'rentSubmit()\']"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[@onclick=\'returnSubmit()\']"));
      assert(elements.size() > 0);
    }
  }


  @Test
  public void tEST6RENT() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Rent-A-Cat")).click();

    driver.findElement(By.id("rentID")).click();
    driver.findElement(By.id("rentID")).sendKeys("1");
    driver.findElement(By.xpath("//button[@onclick=\'rentSubmit()\']")).click();

    List<WebElement> catLines = driver.findElements(By.xpath("//div[@id='listing']/ul/li"));

    assertThat(catLines.get(0).getText(), is("Rented out"));
    assertThat(catLines.get(1).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(catLines.get(2).getText(), is("ID 3. Mistoffelees"));

    assertThat(driver.findElement(By.id("rentResult")).getText(), is("Success!"));
  }


  @Test
  public void tEST7RETURN() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=true\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Rent-A-Cat")).click();
    driver.findElement(By.id("returnID")).click();
    driver.findElement(By.id("returnID")).sendKeys("2");
    driver.findElement(By.xpath("//button[@onclick=\'returnSubmit()\']")).click();
    assertThat(driver.findElement(By.id("cat-id1")).getText(), is("ID 1. Jennyanydots"));
    assertThat(driver.findElement(By.id("cat-id2")).getText(), is("ID 2. Old Deuteronomy"));
    assertThat(driver.findElement(By.id("cat-id3")).getText(), is("ID 3. Mistoffelees"));
    assertThat(driver.findElement(By.id("returnResult")).getText(), is("Success!"));
  }


  @Test
  public void tEST8FEEDACAT() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Feed-A-Cat")).click();
    {
      List<WebElement> elements = driver.findElements(By.xpath("//button[text()=\'Feed\']"));
      assert(elements.size() > 0);
    }
  }


  @Test
  public void tEST9FEED() {
      driver.get("http://localhost:8080");
      js.executeScript("document.cookie='1=false'; document.cookie='2=false'; document.cookie='3=false';");
      driver.findElement(By.linkText("Feed-A-Cat")).click();
      driver.findElement(By.id("catnips")).sendKeys("6");
      driver.findElement(By.xpath("//button[text()='Feed']")).click();

      new WebDriverWait(driver, Duration.ofSeconds(5))
          .until(ExpectedConditions.visibilityOfElementLocated(
              By.xpath("//*[@id='feedResult' and text()='Nom, nom, nom.']")
          ));

      assertThat(
        driver.findElement(By.id("feedResult")).getText().trim(),
        is("Nom, nom, nom.")
      );
  }


  @Test
  public void tEST10GREETACAT() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.findElement(By.linkText("Greet-A-Cat")).click();
    assertThat(driver.findElement(By.id("greeting")).getText(), is("Meow!Meow!Meow!"));
  }


  @Test
  public void tEST11GREETACATWITHNAME() {
    driver.get("http://localhost:8080");
    js.executeScript("document.cookie = \"1=false\"; document.cookie = \"2=false\"; document.cookie = \"3=false\";");
    driver.get("http://localhost:8080/greet-a-cat/Jennyanydots");
    assertThat(driver.findElement(By.id("greeting")).getText(), is("Meow! from Jennyanydots."));
  }
}
