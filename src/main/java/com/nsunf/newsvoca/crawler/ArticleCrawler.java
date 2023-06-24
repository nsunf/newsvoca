package com.nsunf.newsvoca.crawler;

import com.github.slugify.Slugify;
import com.nsunf.newsvoca.entity.*;
import com.nsunf.newsvoca.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
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

    public void getArticle(String url, long nextId, Consumer<Article> articleConsumer, Consumer<List<Paragraph>> paragraphConsumer, Consumer<List<ArticleImg>> articleImgConsumer) {
        WebDriver driver = startDriver();
        driver.get(url);

        Article article = scrapArticle(driver, nextId);
        if (article != null) {
            articleConsumer.accept(article);
            scrapArticleContents(driver, article, paragraphConsumer, articleImgConsumer);
        }

        driver.quit();
    }

    public Article scrapArticle(WebDriver driver, long nextId) {
        String url = driver.getCurrentUrl();
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

        // pathname 생성
        String cat = url.split("/")[6];

        // slug 생성
        Slugify slg = Slugify.builder().build();
        String slugStr = slg.slugify(headline);

        CategoryMajor majorCat = categoryService.getCategoryMajorByPathname(cat);
        CategoryMinor minorCat = null;

        if (majorCat == null) {
            minorCat = categoryService.getCategoryMinorByPathame(cat);
            if (minorCat != null) majorCat = minorCat.getCategoryMajor();
        }

        if (majorCat == null && minorCat == null) {
            log.warn("확인 되지 않은 카테고리 이름 : {}", cat);
            return null;
        }

        String pathname = String.join("/",
                "/article",
                String.valueOf(nextId),
                minorCat == null ? majorCat.getName() : minorCat.getName(),
                slugStr
        );

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
                .views(0)
                .build();
    }

    public void scrapArticleContents(WebDriver driver, Article article, Consumer<List<Paragraph>> paragraphConsumer, Consumer<List<ArticleImg>> articleImgConsumer) {
        long order = 0L;
        List<Paragraph> paragraphList = new ArrayList<>();
        List<ArticleImg> articleImgList = new ArrayList<>();
        // 기사 제목
        String headline = driver.findElement(By.className("headline__text")).getText();
        Paragraph titleParagraph = Paragraph.builder().contentOrder(++order).article(article).content(headline).titleYN("Y").build();
        paragraphList.add(titleParagraph);
        // 대표 이미지
        String repImg = null;
        String repImgDesc = null;
        WebElement repMediaSection = driver.findElement(By.className("image__lede"));
        List<WebElement> videoList = repMediaSection.findElements(By.tagName("video"));
        List<WebElement> imgList = repMediaSection.findElements(By.cssSelector(".image .image__picture img"));

        if (imgList.size() > 0) {
            repImg = imgList.get(0).getAttribute("src");
            try {
                repImgDesc = repMediaSection.findElement(By.className("image__caption")).getText();
            } catch (NoSuchElementException e) {
                log.warn("대표 이미지의 캡션을 찾을 수 없음 : url({})", driver.getCurrentUrl());
            }
        } else if (videoList.size() > 0) {
            repImg = videoList.get(0).getAttribute("poster");
            try {
                repImgDesc = repMediaSection.findElement(By.className("video-resource__headline")).getText();
            } catch (NoSuchElementException e) {
                log.warn("대표 비디오의 캡션을 찾을 수 없음 : url({})", driver.getCurrentUrl());
            }
        }

        if (repImg != null && !repImg.isBlank()) {
            ArticleImg articleImg = ArticleImg.builder().contentOrder(++order).article(article).caption(repImgDesc).url(repImg).repYN("Y").build();
            articleImgList.add(articleImg);
        }
        // 문장 및 이미지
        List<WebElement> contentList = driver.findElements(By.cssSelector(".article__content > .paragraph, .article__content > .image"));

        for (WebElement content : contentList) {
            String className = content.getAttribute("class");
            // 문장
            if (className.contains("paragraph") || className.contains("subheader")) {
                Paragraph paragraph = Paragraph.builder().contentOrder(++order).article(article).content(content.getText()).titleYN("N").build();
                paragraphList.add(paragraph);
            // 이미지
            } else if (className.contains("image")) {
                String imgUrl = content.findElement(By.tagName("img")).getAttribute("src");
                String imgDesc = null;
                try {
                    imgDesc = content.findElement(By.className("image__caption")).getText();
                } catch (NoSuchElementException e) {
                    log.warn("해당 이미지의 캡션을 찾을 수 없음 : url({}), imgUrl({})", driver.getCurrentUrl(), imgUrl);
                }

                if (imgUrl != null && !imgUrl.isBlank()) {
                    ArticleImg articleImg = ArticleImg.builder().contentOrder(++order).article(article).caption(imgDesc).url(imgUrl).repYN("N").build();
                    articleImgList.add(articleImg);
                }
            // 비디오 이미지
            } else if (className.contains("video-resource")) {
                String thumbnailUrl = content.findElement(By.tagName("video")).getAttribute("poster");
                String thumbnailCaption = content.findElement(By.className("video-resource__headline")).getText();
                ArticleImg articleImg = ArticleImg.builder().contentOrder(++order).article(article).caption(thumbnailCaption).url(thumbnailUrl).repYN("N").build();
                articleImgList.add(articleImg);
            } else {
                log.warn("식별 되지 않은 콘텐츠 타입 : url({}), className({})", driver.getCurrentUrl(), className);
            }
        }

        paragraphConsumer.accept(paragraphList);
        articleImgConsumer.accept(articleImgList);
    }

}
