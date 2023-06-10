package com.nsunf.newsvoca.service;

import com.nsunf.newsvoca.crawler.Crawler;
import com.nsunf.newsvoca.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final Crawler crawler;

    public List<ArticleDto> getArticles(String majorCat, String minorCat) {
        String cnnUrl = "https://edition.cnn.com";
        return null;
    }

}
