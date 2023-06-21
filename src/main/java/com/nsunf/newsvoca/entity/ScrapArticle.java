package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ScrapArticleId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
//@Entity
public class ScrapArticle extends BaseEntity {
    @EmbeddedId
    private ScrapArticleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scrapbookId")
    @JoinColumn(name = "scrap_article_scrapbook_id", nullable = false)
    private Scrapbook scrapbook;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}