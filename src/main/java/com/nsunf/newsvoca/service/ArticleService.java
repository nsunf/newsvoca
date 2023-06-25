package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.ArticleCrawler;
import com.nsunf.newsvoca.dto.*;
import com.nsunf.newsvoca.entity.*;
import com.nsunf.newsvoca.repository.ArticleImgRepository;
import com.nsunf.newsvoca.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
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


    public List<ArticleDto> getArticleDtoList(String majorCatName, String minorCatName) {
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
        if (minorCatName != null)
            result = articleRepository.getArticleDtoByCategoryMinorName(minorCatName, 20);
        else if (majorCatName != null)
            result = articleRepository.getArticleDtoByCategoryMajorName(majorCatName, 20);
        else
            result = articleRepository.getArticleDtoByCategoryMajorName("world", 20);

        return result;
    }

    public ArticleDetailDto getArticleDetailDto(Long articleId) {
        ArticleDetailDto articleDetailDto = articleRepository.getArticleDetailById(articleId);
        List<ParagraphDto> paragraphContents = paragraphService.getParagraphContents(articleId);
        List<ArticleImgDto> articleImgContents = articleImgRepository.getArticleImgContentsByArticleId(articleId);

        List<ArticleContentDto> contents = new ArrayList<>();
        contents.addAll(paragraphContents);
        contents.addAll(articleImgContents);

        contents.sort((lhs, rhs) -> lhs.getContentOrder() < rhs.getContentOrder() ? -1 : 1);

        articleDetailDto.setContents(contents);

        return articleDetailDto;
    }

    public void saveArticles(List<String> urlList) {
        List<String> newArticleUrls = urlList.stream().filter(url -> !articleRepository.existsByOriUrl(url)).collect(Collectors.toList());
        log.info("탐색된 url : {}, 저장할 url : {}", urlList.size(), newArticleUrls.size());
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
                    log.error(e.getMessage());
                }
            });
        }

        executors.shutdown();

        try {
            executors.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

        articleRepository.saveAllAndFlush(articleList);
        paragraphService.saveParagraphs(paragraphList);
        articleImgRepository.saveAll(articleImgList);

        log.info("{} 개의 기사 저장됨", articleList.size());
        log.info("{} 개의 문장 저장됨", paragraphList.size());
        log.info("{} 개의 이미지 저장됨", articleImgList.size());
    }

}