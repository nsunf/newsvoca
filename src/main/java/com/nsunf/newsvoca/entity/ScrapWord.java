package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ScrapWordId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
//@Entity
public class ScrapWord extends BaseEntity {
    @EmbeddedId
    private ScrapWordId id;

    @MapsId("wordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "word_id")
    private Word word;

    @MapsId("scrapbookId")
    @ManyToOne
    @JoinColumn(nullable = false, name="scrap_word_scrapbook_id")
    private Scrapbook scrapbook;
}
