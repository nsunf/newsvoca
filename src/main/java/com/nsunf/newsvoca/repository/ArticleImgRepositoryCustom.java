package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleImgDto;

import java.util.List;

public interface ArticleImgRepositoryCustom {
    public List<ArticleImgDto> getArticleImgContentsByArticleId(Long id);
}
