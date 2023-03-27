/**
 * Created By shoh@n
 * Date: 3/28/2023
 */

package com.example.middleware.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeleniumUtils {
    public static void main(String[] args) {
        //SpringApplication.run(MiddlewareApplication.class, args);
        WebDriver driver = null;
        try {
            // Configure the webdriver binary
            WebDriverManager.chromedriver().setup();

            // Launch the browser
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

            // Navigate to the website
            driver.get("https://www.jtexpress.my/shipping-rates");;

            // Find the form fields and fill them up
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "$('input#international').prop('checked', true);" +
                            "$('#destination-country-div').css('display', 'flex');" +
                            "$('#receiver_postcode_div').css('display', 'none');" +
                            "$('#shipping-type').html('<option value=\"EZ\">Regular</option>');" +
                            "$('#receiver_postcode').val('12345');" +
                            "$('#div-insurance').css('display', 'block');"
            );

            WebElement sender_postcode = driver.findElement(By.name("sender_postcode"));
            sender_postcode.sendKeys("12345");

            WebElement destination_country = driver.findElement(By.name("destination_country"));
            Select dropdown = new Select(destination_country);
            dropdown.selectByVisibleText("INDONESIA");


            WebElement weight = driver.findElement(By.name("weight")); // 1 to 30
            weight.sendKeys("1");

            WebElement length = driver.findElement(By.name("length")); // 1 to 120
            length.sendKeys("1");

            WebElement width = driver.findElement(By.name("width")); // 1 to 120
            width.sendKeys("1");

            WebElement height = driver.findElement(By.name("height")); // 1 to 120
            height.sendKeys("1");

            // Click the submit button
            WebElement submitButton = driver.findElement(By.id("form-rates"));
            submitButton.submit();

            // Wait for the response to come back and find the table element
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
            Thread.sleep(1000);

            List<Map<String, String>> data = new ArrayList<>();

            // Get the tables and loop through them
            List<WebElement> tables = driver.findElements(By.cssSelector("#content table"));
            for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
                WebElement table = tables.get(tableIndex);
                // skip header row
                if (tableIndex != 0) {
                    parseTable(table, data);
                }
            }

            // collect errors
            List<String> errors = new ArrayList<>();
            List<WebElement> errorElements = driver.findElements(By.cssSelector("p.err-text"));
            for (WebElement errorElement : errorElements) {
                String errorText = errorElement.getText();
                if (!errorText.isBlank()) {
                    errors.add(errorText);
                }
            }

            driver.quit();
        }
        catch (Exception ex) {
            assert driver != null;
            driver.quit();
        }
    }

    public static void parseTable(WebElement table, List<Map<String, String>> data) {
        List<WebElement> rows = table.findElements(By.cssSelector("tr"));
        List<String> headers = null;

        for (WebElement row : rows) {
            // Find all cells in the row
            List<WebElement> cells = row.findElements(By.cssSelector("th, td"));

            if (headers == null) {
                // If this is the first row, use it to get the headers
                headers = cells.stream().map(WebElement::getText).collect(Collectors.toList());
            } else {
                // Otherwise, create a map of the row data
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    rowMap.put(headers.get(i), cells.get(i).getText().trim());
                }
                data.add(rowMap);
            }
        }
    }
}
