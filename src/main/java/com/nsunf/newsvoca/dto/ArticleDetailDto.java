package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.entity.Article;
import com.nsunf.newsvoca.entity.ArticleImg;
import com.nsunf.newsvoca.entity.Paragraph;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ArticleDetailDto {
    Long id;
    String publishDate;
    String[] authorArr;
    long views;
    ParagraphDto title;
    ArticleImgDto repImg;
    List<ArticleContentDto> contents;

    @QueryProjection
    public ArticleDetailDto(Article article, Paragraph title, ArticleImg repImg) {
        this.id = article.getId();
        this.publishDate = article.getPublishTime().toString();
        this.views = article.getViews();
        this.title = new ParagraphDto(title);
        this.repImg = new ArticleImgDto(repImg);
    }
}
