/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.providers;

import com.example.middleware.enums.CountryCode;
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
import java.util.*;
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

            boolean internationShipping = !Objects.equals(
                    CountryCode.getByCode(request.destination_country()).getAlpha2(),
                    "MY"
            );

            // Configure the webdriver binary
            log.info("{} setup chrome driver", Provider.JT_EXPRESS);
            WebDriverManager.chromedriver().setup();

            // Launch the browser
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

            // Navigate to the website
            driver.get("https://www.jtexpress.my/shipping-rates");
            log.info("{} navigate to the website and get content", Provider.JT_EXPRESS);

            // Find the form fields and fill them up
            log.info("{} started to fill up the form", Provider.JT_EXPRESS);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            if (internationShipping) {
                js.executeScript(
                        "$('input#international').prop('checked', true);" +
                                "$('#destination-country-div').css('display', 'flex');" +
                                "$('#receiver_postcode_div').css('display', 'none');" +
                                "$('#shipping-type').html('<option value=\"EZ\">Regular</option>');" +
                                "$('#receiver_postcode').val('12345');" +
                                "$('#div-insurance').css('display', 'block');"
                );
                WebElement destination_country = driver.findElement(By.name("destination_country"));
                Select dropdown = new Select(destination_country);
                String value = CountryCode.getByCode(request.destination_country()).getAlpha3();
                dropdown.selectByValue(value);
            }
            else {
                WebElement receiver_postcode = driver.findElement(By.name("receiver_postcode"));
                receiver_postcode.sendKeys(request.destination_postCode());

                WebElement shipping_type = driver.findElement(By.name("shipping_type"));
                Select dropdown = new Select(shipping_type);
                dropdown.selectByValue(request.expressDelivery() ? "EX" : "EZ");
            }

            WebElement sender_postcode = driver.findElement(By.name("sender_postcode"));
            sender_postcode.sendKeys(request.origin_post_code());

            if (request.weight() != null) {
                WebElement weight = driver.findElement(By.name("weight"));
                weight.sendKeys(request.weight().toString());
            }
            if (request.length() != null) {
                WebElement length = driver.findElement(By.name("length"));
                length.sendKeys(request.length().toString());
            }
            if (request.width() != null) {
                WebElement width = driver.findElement(By.name("width"));
                width.sendKeys(request.width().toString());
            }
            if (request.height() != null) {
                WebElement height = driver.findElement(By.name("height"));
                height.sendKeys(request.height().toString());
            }
            if (request.insurance_item_value() != null && request.insurance_item_value() > 0) {
                driver.findElement(By.id("insurance")).click();
                WebElement item_value = driver.findElement(By.name("item_value"));
                item_value.sendKeys(request.insurance_item_value().toString());
            }

            // Click the submit button
            WebElement submitButton = driver.findElement(By.id("form-rates"));
            submitButton.submit();
            log.info("{} submitted the form", Provider.JT_EXPRESS);

            // Wait for the response to come back and find the table element
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#content table")));

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
            log.info("{}, get table size {}", Provider.JT_EXPRESS, tables.size());
            for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
                WebElement table = tables.get(tableIndex);
                // skip header row
                if (tableIndex != 0) {
                    parseTable(table, tableData);
                }
            }

            if (tableData.size() > 1) {
                log.info("{} preparing shippingRateData", Provider.JT_EXPRESS);
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
        String originCountryAlfa2Code = CountryCode.getByCode(request.origin_country()).getAlpha2();
        String destinationCountryAlfa2Code = CountryCode.getByCode(request.destination_country()).getAlpha2();
        boolean domesticShipping = originCountryAlfa2Code.equals(destinationCountryAlfa2Code);
        if (!domesticShipping) {
            if (!destinationCountryMap.containsKey(destinationCountryAlfa2Code)) {
                errors.add(request.destination_country() + " is not supported destination_country for this provider");
            }
        }
        if (domesticShipping && request.destination_postCode() == null) {
            errors.add("destination_postCode is required");
        }
        if (request.length() != null && request.length() > 80) {
            errors.add("length can not be greater than 80");
        }
        if (request.width() != null && request.width() > 80) {
            errors.add("width can not be greater than 80");
        }
        if (request.height() != null && request.height() > 80) {
            errors.add("height can not be greater than 80");
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
