package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.entity.Article;
import com.nsunf.newsvoca.entity.ArticleImg;
import com.nsunf.newsvoca.entity.Paragraph;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String preview;
    private String repImg;
    private String repImgDesc;
    private String pathname;

    @QueryProjection
    public ArticleDto(Article article, String preview, String repImg, String repImgDesc) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.pathname = article.getPathname();
        this.preview = preview;
        this.repImg = repImg;
        this.repImgDesc = repImgDesc;
    }
}
