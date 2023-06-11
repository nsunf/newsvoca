package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.ArticleCrawler;
import com.nsunf.newsvoca.crawler.Crawler;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.dto.CategoryMajorDto;
import com.nsunf.newsvoca.entity.CategoryMajor;
import com.nsunf.newsvoca.entity.CategoryMinor;
import com.nsunf.newsvoca.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleCrawler articleCrawler;
    private final ArticleRepository articleRepository;
    private final CategoryService categoryService;

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

        return null;
    }

    public void saveArticles(List<String> urlList) {
        List<String> newArticleUrls = urlList.stream().filter(articleRepository::existsByOriUrl).collect(Collectors.toList());



    }

}
