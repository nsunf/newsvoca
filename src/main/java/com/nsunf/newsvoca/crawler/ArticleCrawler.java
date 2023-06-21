package com.nsunf.newsvoca.crawler;

import com.github.slugify.Slugify;
import com.nsunf.newsvoca.entity.*;
import com.nsunf.newsvoca.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

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

        articleConsumer.accept(article);
        scrapArticleContents(driver, article, paragraphConsumer, articleImgConsumer);

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
        String[] splittedUrl = url.split("/");
        String cat = splittedUrl[splittedUrl.length - 3];

        // slug 생성
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
        ArticleContent titleContent = ArticleContent.builder().contentOrder(++order).article(article).build();
        Paragraph titleParagraph = Paragraph.builder().articleContent(titleContent).content(headline).titleYN("Y").build();
        paragraphList.add(titleParagraph);
        // 대표 이미지
        String repImg = null;
        String repImgDesc = null;
        WebElement repMediaSection = driver.findElement(By.className("image__lede"));
        List<WebElement> videoList = repMediaSection.findElements(By.tagName("video"));
        List<WebElement> imgList = repMediaSection.findElements(By.cssSelector(".image .image__picture img"));

        if (imgList.size() > 0) {
            repImg = imgList.get(0).getAttribute("src");
            repImgDesc = repMediaSection.findElement(By.className("image__caption")).getText();
        } else if (videoList.size() > 0) {
            repImg = videoList.get(0).getAttribute("poster");
            repImgDesc = repMediaSection.findElement(By.className("video-resource__headline")).getText();
        }

        if (repImg != null) {
            ArticleContent imgContent = ArticleContent.builder().contentOrder(++order).article(article).build();
            ArticleImg articleImg = ArticleImg.builder().articleContent(imgContent).caption(repImgDesc).url(repImg).repYN("Y").build();

            articleImgList.add(articleImg);
        }
        // 문장 및 이미지
        List<WebElement> contentList = driver.findElements(By.cssSelector(".article__content > .paragraph, .article__content > .image"));

        for (WebElement content : contentList) {
            String className = content.getAttribute("class");
            if (className.contains("paragraph")) {
                ArticleContent paragraphContent = ArticleContent.builder().contentOrder(++order).article(article).build();
                Paragraph paragraph = Paragraph.builder().articleContent(paragraphContent).content(content.getText()).titleYN("N").build();
                paragraphList.add(paragraph);
            } else {
                String imgUrl = content.findElement(By.tagName("img")).getAttribute("src");
                String imgDesc = content.findElement(By.className("image__caption")).getText();

                ArticleContent imgContent = ArticleContent.builder().contentOrder(++order).article(article).build();
                ArticleImg articleImg = ArticleImg.builder().articleContent(imgContent).caption(imgDesc).url(imgUrl).repYN("N").build();
                articleImgList.add(articleImg);
            }
        }

        paragraphConsumer.accept(paragraphList);
        articleImgConsumer.accept(articleImgList);
    }

}
