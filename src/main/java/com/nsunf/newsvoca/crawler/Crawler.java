package com.nsunf.newsvoca.crawler;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Crawler {
    private static WebDriver driver;

    static {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
    }

    public WebDriver startDriver() {

        if (driver != null) quitDriver();

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);

        driver = new FirefoxDriver(options);

        return driver;
    }

    public void quitDriver() {
        driver.quit();
        driver = null;
    }

    public void loadPage(String url) {
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
                Thread.sleep(500);
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
