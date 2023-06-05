package com.nsunf.newsvoca.entity.composite_key;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ScrapWordId implements Serializable {
    private Long wordId;
    private Long scrapbookId;
}
