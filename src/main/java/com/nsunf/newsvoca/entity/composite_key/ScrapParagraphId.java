package com.nsunf.newsvoca.entity.composite_key;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ScrapParagraphId implements Serializable {
    private Long scrapbookId;
    private Long paragraphId;
}
