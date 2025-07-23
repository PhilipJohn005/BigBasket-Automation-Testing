package project;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class Bigbasket {
    public static WebDriver driver;

    public static void main(String[] args) {
        try {
            setupDriver();
            driver.get("https://www.bigbasket.com/");
            System.out.println("Site launched successfully: " + driver.getTitle());

            waitForPageLoad();
            handleLoginSignUp();
            performSignUpWithManualOTP();
            addItemToCartAndCaptureScreenshot();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void setupDriver() {
        try {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("Driver setup complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void waitForPageLoad() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                webDriver -> ((String) ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("return document.readyState")).equals("complete"));
            System.out.println("Page loaded completely.");
        } catch (Exception e) {
            System.err.println("Page did not load completely.");
        }
    }

    private static void handleLoginSignUp() {
        try {
            clickButton(By.cssSelector("button[color='darkOnyx'][pattern='filled']"));
            enterText(By.id("multiform"), "8197742770");
            System.out.println("Entered phone/email.");
            clickButton(By.cssSelector("button[type='submit']"));
            System.out.println("Continue button clicked.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void performSignUpWithManualOTP() {
        try {
            System.out.println("Enter the OTP manually:");
            Scanner scanner = new Scanner(System.in);
            String otp = scanner.nextLine();
            enterText(By.cssSelector("input[type='number']"), otp);
            System.out.println("Entered OTP.");
            clickButton(By.xpath("//button[text()='Verify & Continue']"));
            System.out.println("Verify & Continue button clicked.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addItemToCartAndCaptureScreenshot() {
        try {
            WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[color='rossoCorsa'][pattern='outline']")));

            scrollIntoView(addToCartButton);
            addToCartButton.click();
            System.out.println("Item added to cart.");

            scrollToTop();

            clickCartIcon();

            Thread.sleep(2000); 

           
            System.out.println("Cart is fully loaded. Taking screenshot...");

            captureScreenshot("cart_screenshot.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void clickCartIcon() {
        try {
            WebElement svgContainer = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("g[mask='url(#basket_svg__a)']"))
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", svgContainer);
            System.out.println("Cart icon clicked successfully.");
        } catch (Exception e) {
            System.err.println("Failed to click the cart icon.");
            e.printStackTrace();
        }
    }

    private static void enterText(By locator, String text) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.sendKeys(text);
        } catch (Exception e) {
            System.err.println("Error entering text: " + text);
            e.printStackTrace();
        }
    }

    private static void clickButton(By locator) {
        try {
            WebElement button = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(locator));
            scrollIntoView(button);
            button.click();
            System.out.println("Button clicked successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scrollIntoView(WebElement element) {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", element);
            Thread.sleep(500);
            System.out.println("Scrolled into the element's view.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scrollToTop() {
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            System.out.println("Scrolled back to the top.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void captureScreenshot(String fileName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/" + fileName);
            destFile.getParentFile().mkdirs();
            Files.copy(srcFile, destFile);
            System.out.println("Screenshot saved to: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanup() {
        if (driver != null) {
            driver.quit();
            System.out.println("Driver closed.");
        }
    }
}
