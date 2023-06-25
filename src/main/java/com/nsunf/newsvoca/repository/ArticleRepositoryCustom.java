package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleDetailDto;
import com.nsunf.newsvoca.dto.ArticleDto;

import java.util.List;
import java.util.Optional;

public interface ArticleRepositoryCustom {
    List<ArticleDto> getArticleDtoByCategoryMajorName(String categoryMajorName, int count);
    List<ArticleDto> getArticleDtoByCategoryMinorName(String categoryMinorName, int count);
    ArticleDetailDto getArticleDetailById(Long id);
}
