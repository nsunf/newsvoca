package com.nsunf.newsvoca.entity.composite_key;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ScrapArticleId implements Serializable {
    private Long articleId;
    private Long ScrapbookId;
}
