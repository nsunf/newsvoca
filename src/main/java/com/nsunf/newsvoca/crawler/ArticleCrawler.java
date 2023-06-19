package com.nsunf.newsvoca.crawler;

import com.github.slugify.Slugify;
import com.nsunf.newsvoca.entity.Article;
import com.nsunf.newsvoca.entity.CategoryMajor;
import com.nsunf.newsvoca.entity.CategoryMinor;
import com.nsunf.newsvoca.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleCrawler extends Crawler {

    private final CategoryService categoryService;

    public List<String> getArticleUrls(String url) {
        List<String> result = new ArrayList<>();

        WebDriver driver = startDriver();
        loadPage(driver, url);

        List<WebElement> cardList = driver.findElement(By.className("zone")).findElements(By.className("card"));

        for (WebElement card: cardList) {
            WebElement anchor = card.findElement(By.className("container__link"));
            if (!anchor.getAttribute("data-link-type").equals("article")) continue;

            result.add(anchor.getAttribute("href"));
        }

        driver.quit();

        return result;
    }

    public Article getArticle(String url, long nextId) {
        WebDriver driver = startDriver();
        driver.get(url);


        // 기사 제목 추출
        String headline = driver.findElement(By.className("headline__text")).getText();
        // 기자 추출
        String authors = driver.findElements(By.className("byline__name")).stream()
                .map(WebElement::getText)
                .reduce((lhs, rhs) -> lhs + "|" + rhs)
                .orElseGet(() -> "");
        // 발행일 추출
        String timeStamp = driver.findElement(By.className("timestamp")).getText();
        String editedTimeStamp = timeStamp.substring(timeStamp.indexOf(" ") + 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a z, EEE MMMM d, yyyy", Locale.US);
        LocalDateTime pubDate = LocalDateTime.parse(editedTimeStamp, formatter);

        String[] splittedUrl = url.split("/");
        String cat = splittedUrl[splittedUrl.length - 3];

        Slugify slg = Slugify.builder().build();
        String slugStr = slg.slugify(headline);


        CategoryMajor majorCat = categoryService.getCategoryMajorByPathname(cat);
        CategoryMinor minorCat = null;

        if (majorCat == null) {
            minorCat = categoryService.getCategoryMinorByPathame(cat);
            majorCat = minorCat.getCategoryMajor();
        }

        String pathname = String.join("/",
                "/article",
                String.valueOf(nextId),
                minorCat == null ? majorCat.getName() : minorCat.getName(),
                slugStr
        );

        driver.quit();

        return Article.builder()
                .id(nextId)
                .categoryMajor(majorCat)
                .categoryMinor(minorCat)
                .oriUrl(url)
                .pathname(pathname)
                .title(headline)
                .slug(slugStr)
                .publishTime(pubDate)
                .author(authors)
                .view(0)
                .build();
    }
}
