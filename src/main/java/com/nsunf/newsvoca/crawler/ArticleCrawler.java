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
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleCrawler extends Crawler {

    private final CategoryService categoryService;

    public List<String> getArticleUrls(String url) {
        List<String> result = new ArrayList<>();

        WebDriver driver = startDriver();
        loadPage(url);

        List<WebElement> cardList = driver.findElement(By.className("zone")).findElements(By.className("card"));

        for (WebElement card: cardList) {
            WebElement anchor = card.findElement(By.className("container__link"));
            if (!anchor.getAttribute("data-link-type").equals("article")) continue;

            result.add(anchor.getAttribute("href"));
        }

        quitDriver();

        return result;
    }

    public Article getArticle(String url) {
        WebDriver driver = startDriver();
        loadPage(url);
        // 기사 제목 추출
        String headline = driver.findElement(By.className("headline__text")).getText();
        // 기자 추출
        String authors = driver.findElements(By.className("byline__name")).stream()
                .map(WebElement::getText)
                .reduce((lhs, rhs) -> lhs + "|" + rhs)
                .orElseGet(() -> "");
        // 발행일 추출
        String timeStamp = driver.findElement(By.className("timestamp")).getText();
        String[] splittedTimeStamp = timeStamp.split(" ");
        String editedTimeStamp = String.join(" ", Arrays.copyOfRange(splittedTimeStamp, 1, splittedTimeStamp.length));
        Date pubDate = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a z, EEE MMM d, yyyy", Locale.US);
            pubDate = formatter.parse(editedTimeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] splittedUrl = url.split("/");
        String cat = splittedUrl[splittedUrl.length - 3];

        Slugify slg = Slugify.builder().build();
        String slugStr = slg.slugify(headline);

//        String articleUrl = String.join("/", "/article", String.valueOf());

        CategoryMajor majorCat = categoryService.getCategoryMajorByPathname(cat);
        CategoryMinor minorCat = null;

        if (majorCat == null) {
            minorCat = categoryService.getCategoryMinorByPathame(cat);
            majorCat = minorCat.getCategoryMajor();
        }

        return Article.builder()
                .categoryMajor(majorCat)
                .categoryMinor(minorCat)
                .oriUrl(url)
                .url("")
                .title(headline)
                .slug(slugStr)
//                .publishTime(pubDate)
                .author(authors)
                .view(0)
                .build();
    }
    // url생성에 generate된 id가 필요
    // Date와 LocalDateTime 타입 불일치
}
