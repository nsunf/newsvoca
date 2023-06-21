package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleDto;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<ArticleDto> getArticleDtoByCategoryMajorName(String categoryMajorName);
    List<ArticleDto> getArticleDtoByCategoryMinorName(String categoryMinorName);
}
