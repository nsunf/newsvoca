package com.nsunf.newsvoca.crawler;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Crawler {
    public Crawler() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
//        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        System.setProperty("MOZ_REMOTE_SETTINGS_DEVTOOLS", "1");
    }

    public WebDriver startDriver() {

//        FirefoxOptions options = new FirefoxOptions();
//        options.setHeadless(true);
//
//        driver = new FirefoxDriver(options);

        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.addArguments("headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    public void loadPage(WebDriver driver, String url) {
        log.info("페이지 이동 중 : {}", url);
        long beforeTime = System.currentTimeMillis();
        driver.get(url);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        long tmp = 0L;
        int loopCnt = 0;

        while (true) {
            Object obj = js.executeScript("window.scrollBy(0, document.body.scrollHeight); return document.body.scrollHeight;");
            Long scrollHeight = (Long) obj;

            if (scrollHeight.equals(tmp)) {
                break;
            } else {
                tmp = scrollHeight;
                log.info("{} 번째 스크롤 후 로딩중", ++loopCnt);
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime);
        log.info("로딩 완료");
        log.info("소요 시간 : {}ms", secDiffTime);
        log.info("스크롤 횟수 : {}", loopCnt);
        log.info("페이지 길이 : {}px", tmp);
    }
}
