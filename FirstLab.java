package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.decorators.WebDriverDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Objects;

public class FirstLab {

    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.microsoft.com/uk-ua/";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }

    @Test
    public void testClickOnElement() {
        WebElement productsButton = chromeDriver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/header/div/div/nav/ul/li[1]/a"));
        Assert.assertNotNull(productsButton);
        productsButton.click();
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testInputDataAndVerifyField() {
        WebElement searchButton = chromeDriver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/header/div/div/div[2]/form/button"));
        Assert.assertNotNull(searchButton);
        searchButton.click();

        WebElement searchField = chromeDriver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/header/div/div/div[2]/form/input"));
        Assert.assertNotNull(searchField);
        String searchText = "Windows 11";
        searchField.sendKeys(searchText);


        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(baseUrl)));
        Assert.assertTrue(Objects.requireNonNull(chromeDriver.getCurrentUrl()).contains("search"));
    }

    @Test
    public void testFindElementUsingIndirectXPath() {
        WebElement teamsLink = chromeDriver.findElement(By.xpath("//*[@id='l0_Teams']"));
        teamsLink.click();
        Assert.assertNotNull(teamsLink);
    }


    @Test
    public void testConditionVerification() {
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        WebElement headerArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("headerArea")));
        Assert.assertTrue(headerArea.isSelected(), "Верхня частина сайту відображається на сторінці.");
    }

}
