package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.ArticleCrawler;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.entity.*;
import com.nsunf.newsvoca.repository.ArticleImgRepository;
import com.nsunf.newsvoca.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final CategoryService categoryService;
    private final ParagraphService paragraphService;

    private final ArticleRepository articleRepository;
    private final ArticleImgRepository articleImgRepository;

    public Long getNextId() {
        return articleRepository.findNextId().orElse(0L) + 1L;
    }


    public List<ArticleDto> getArticles(String majorCatName, String minorCatName) {
        // crawler를 이용한 데이터 갱신
        String cnnUrl = "https://edition.cnn.com";
        if (minorCatName != null)
            cnnUrl += "/" + categoryService.getCategoryMinorByName(minorCatName).getName().replaceAll(" ", "-").toLowerCase();
        else if (majorCatName != null)
            cnnUrl += "/" + categoryService.getCategoryMajorByName(majorCatName).getName().replaceAll(" ", "-").toLowerCase();
        else
            cnnUrl += "/world";

        List<String> articleUrlList = articleCrawler.getArticleUrls(cnnUrl);
        saveArticles(articleUrlList);
        // db에서 데이터 조회 후 dto 반환

        List<ArticleDto> result = new ArrayList<>();

//        result = articleRepository.getArticleDtoByCategoryMajorName(majorCatName);

        return result;
    }

    public void saveArticles(List<String> urlList) {
        List<String> newArticleUrls = urlList.stream().filter(url -> !articleRepository.existsByOriUrl(url)).collect(Collectors.toList());
        System.out.println("탐색된 url : " + urlList.size() + ", 저장할 url : " + newArticleUrls.size());
        long nextId = getNextId();

        ExecutorService executors = Executors.newFixedThreadPool(4);

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

        articleRepository.saveAllAndFlush(articleList);

        paragraphService.saveParagraphs(paragraphList);
        articleImgRepository.saveAll(articleImgList);

        System.out.println(articleList.size() + " articles saved");
        System.out.println(paragraphList.size() + " paragraphs saved");
        System.out.println(articleImgList.size() + " article imgs saved");

        System.out.println("--end--");
    }

}