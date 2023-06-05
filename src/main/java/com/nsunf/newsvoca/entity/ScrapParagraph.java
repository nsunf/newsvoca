package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ScrapParagraphId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ScrapParagraph extends BaseEntity {
    @EmbeddedId
    private ScrapParagraphId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scrapbookId")
    @JoinColumn(name = "scrap_paragraph_scrapbook_id", nullable = false)
    private Scrapbook scrapbook;
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "scrap_paragraph_paragraph_id", nullable = false, referencedColumnName = "id")
    @JoinColumn(name = "paragraph_order", nullable = false, referencedColumnName = "contentOrder")
    @JoinColumn(name = "paragraph_article_id", nullable = false, referencedColumnName = "article_id")
    private Paragraph paragraph;
}
