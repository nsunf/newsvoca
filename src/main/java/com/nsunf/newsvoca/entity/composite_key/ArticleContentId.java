package com.nsunf.newsvoca.entity.composite_key;

import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Builder
public class ArticleContentId implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Long contentOrder;
    private Long articleId;
}
