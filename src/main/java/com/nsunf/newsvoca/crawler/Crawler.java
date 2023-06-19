package com.nsunf.newsvoca.crawler;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

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
        System.out.println("-- Crawler --------------------");
        System.out.println(url + " 접속중");
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
                System.out.println(++loopCnt + " 번째 스크롤 후 로딩중");
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime);
        System.out.println("로딩 완료");
        System.out.println("소요 시간 : " + secDiffTime + "ms");
        System.out.println("스크롤 횟수 : " + loopCnt);
        System.out.println("페이지 길이 : " + tmp + "px");
    }
}
