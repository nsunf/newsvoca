package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.ArticleCrawler;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.entity.*;
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
        // crawler를 이용한 데이터 갱신
        List<String> articleUrlList = articleCrawler.getArticleUrls(majorCatName, minorCatName);
        saveArticles(articleUrlList);
        // db에서 데이터 조회 후 dto 반환

        return null;
    }

    public void saveArticles(List<String> urlList) {
        List<String> newArticleUrls = urlList.stream().filter(url -> !articleRepository.existsByOriUrl(url)).collect(Collectors.toList());
        System.out.println("탐색된 url : " + urlList.size() + ", 저장할 url : " + newArticleUrls.size());
        long nextId = getNextId();

        ExecutorService executors = Executors.newCachedThreadPool();

        Vector<Article> articleList = new Vector<>();
        Vector<Paragraph> paragraphList = new Vector<>();
        Vector<ArticleImg> articleImgList = new Vector<>();

        for (int i = 0; i < newArticleUrls.size(); i++) {
            final int index = i;
            final String url = newArticleUrls.get(index);
            executors.submit(() -> {
                try {
                    articleCrawler.getArticle(
                            url,
                            nextId + index,
                            articleList::add,
                            paragraphList::addAll,
                            articleImgList::addAll);
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
        paragraphList.forEach(System.out::println);
        articleImgList.forEach(System.out::println);

        System.out.println("--end--");


    }

}