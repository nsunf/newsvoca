package com.nsunf.newsvoca.crawler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.springframework.stereotype.Component;

@Component
public class Crawler {
    private static WebDriver wd;

    static {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
    }

    public FluentWebDriver startDriver(String url) {

        if (wd != null) return new FluentWebDriver(wd);

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);

        wd = new FirefoxDriver(options);
        wd.get(url);
        return new FluentWebDriver(wd);
    }

    public void quitDriver() {
        wd.quit();
    }
}
