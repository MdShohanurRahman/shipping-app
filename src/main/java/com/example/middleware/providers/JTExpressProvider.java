/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.GoodsType;
import com.example.middleware.enums.Provider;
import com.example.middleware.model.ShippingRateData;
import com.example.middleware.model.ShippingRateRequest;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JTExpressProvider implements ShippingProvider {

    private static final Map<String, String> destinationCountryMap = Map.of(
            "BN", "BWN",
            "HK", "HKG",
            "SG", "SIN",
            "VN", "VNM",
            "CN1", "VNM",
            "TH", "THA",
            "ID", "IDN",
            "PH", "OHL"

    );

    @Override
    public ShippingRateData getShippingRateData(ShippingRateRequest request) {
        ShippingRateData rateData = new ShippingRateData(Provider.JT_EXPRESS);
        WebDriver driver = null;
        try {
            // sanitize data
            List<String> errors = checkErrors(request);
            if (errors.size() > 0) {
                rateData.setMessages(errors);
                return rateData;
            }

            boolean isDomesticShipping = request.destination_country().equals("MY");

            // Configure the webdriver binary
            WebDriverManager.chromedriver().setup();

            // Launch the browser
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

            // Navigate to the website
            driver.get("https://www.jtexpress.my/shipping-rates");

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
            sender_postcode.sendKeys(request.origin_post_code());

            WebElement destination_country = driver.findElement(By.name("destination_country"));
            Select dropdown = new Select(destination_country);
            dropdown.selectByValue(destinationCountryMap.get(request.destination_country()));

            WebElement weight = driver.findElement(By.name("weight"));
            weight.sendKeys(request.weight().toString());

            WebElement length = driver.findElement(By.name("length"));
            length.sendKeys(request.length().toString());

            WebElement width = driver.findElement(By.name("width"));
            width.sendKeys(request.width().toString());

            WebElement height = driver.findElement(By.name("height"));
            height.sendKeys(request.height().toString());

            // Click the submit button
            WebElement submitButton = driver.findElement(By.id("form-rates"));
            submitButton.submit();

            // Wait for the response to come back and find the table element
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
            Thread.sleep(1000);

            // collect form errors
            List<WebElement> errorElements = driver.findElements(By.cssSelector("p.err-text"));
            for (WebElement errorElement : errorElements) {
                String errorText = errorElement.getText();
                if (!errorText.isBlank()) {
                    errors.add(errorText);
                }
            }

            if (errors.size() > 0) {
                rateData.setMessages(errors);
                return rateData;
            }

            List<Map<String, String>> tableData = new ArrayList<>();

            // Get the tables and loop through them
            List<WebElement> tables = driver.findElements(By.cssSelector("#content table"));
            for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
                WebElement table = tables.get(tableIndex);
                // skip header row
                if (tableIndex != 0) {
                    parseTable(table, tableData);
                }
            }

            if (tableData.size() > 1) {
                var parcelData = tableData.get(0);
                var documentData = tableData.get(1);
                if (request.goods_type().equals(GoodsType.PARCEL.name())) {
                    rateData.setRate(parcelData.getOrDefault("Total (incl. Tax)", "0"));
                } else {
                    rateData.setRate(documentData.getOrDefault("Total (incl. Tax)", "0"));
                }
            }

            return rateData;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("can't get data from {}", Provider.JT_EXPRESS);
            rateData.setMessages(List.of(ex.getMessage()));
            return rateData;
        } finally {
            if (driver != null) driver.quit();
        }
    }

    private List<String> checkErrors(ShippingRateRequest request) {
        List<String> errors = new ArrayList<>();
        if (!destinationCountryMap.containsKey(request.destination_country())) {
            errors.add(request.destination_country() + " is not supported destination_country for this provider");
        }
        if (request.length() > 80 || request.width() > 80 || request.height() > 80) {
            errors.add("length, width, height can not be greater than 80");
        }
        return errors;
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
