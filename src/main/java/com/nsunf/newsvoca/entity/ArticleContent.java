package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ArticleContentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public class ArticleContent extends BaseEntity{
    @EmbeddedId ArticleContentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "article_id", nullable = false, referencedColumnName = "id")
    protected Article article;
}
