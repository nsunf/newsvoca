package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.entity.ArticleImg;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticleImgDto extends ArticleContentDto {
    String url;
    String caption;

    @QueryProjection
    public ArticleImgDto(ArticleImg articleImg) {
        this.id = articleImg.getId();
        this.contentOrder = articleImg.getContentOrder();
        this.url = articleImg.getUrl();
        this.caption = articleImg.getCaption();
    }
}
