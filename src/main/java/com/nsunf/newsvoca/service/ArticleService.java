package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.ArticleCrawler;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.entity.Article;
import com.nsunf.newsvoca.entity.CategoryMajor;
import com.nsunf.newsvoca.entity.CategoryMinor;
import com.nsunf.newsvoca.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleCrawler articleCrawler;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

    public Long getNextId() {
        return articleRepository.findNextId().orElse(0L) + 1L;
    }

    public List<ArticleDto> getArticles(String majorCatName, String minorCatName) {
        // CNN URL 찾기
        String cnnUrl = "https://edition.cnn.com";
        if (majorCatName != null) {
            CategoryMajor catMaj = categoryService.getCategoryMajorByName(majorCatName);
            if (catMaj != null)
                cnnUrl += "/" + catMaj.getName().replaceAll(" ", "-").toLowerCase();

            if (minorCatName != null) {
                CategoryMinor catMin = categoryService.getCategoryMinorByName(minorCatName);
                if (catMin != null)
                    cnnUrl += "/" + catMin.getName().replaceAll(" ", "-").toLowerCase();
            }
        }


        List<String> newArticleUrls = articleCrawler.getArticleUrls(cnnUrl);
        System.out.println("---> article 개수 " + newArticleUrls.size());
        long nextId = getNextId();

        ExecutorService executors = Executors.newCachedThreadPool();

        Vector<Article> articleList = new Vector<>();

        for (int i = 0; i < newArticleUrls.size(); i++) {
            final int index = i;
            final String url = newArticleUrls.get(index);
            executors.submit(() -> {
                try {
                    Article article = articleCrawler.getArticle(url, nextId + index);
                    articleList.add(article);
                } catch (Exception e) {
                    System.err.println("url ---> " + url);
                    e.printStackTrace();
                }
            });
        }

        executors.shutdown();

        try {
            executors.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        articleList.forEach(System.out::println);
        System.out.println("--end--");

        // 멀티 스레드를 이용하여 구현
        // 성능차 고려

        return null;
    }

    public void saveArticles(List<String> urlList) {
        List<String> newArticleUrls = urlList.stream().filter(articleRepository::existsByOriUrl).collect(Collectors.toList());



    }

}