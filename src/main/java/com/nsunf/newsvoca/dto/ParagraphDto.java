package com.nsunf.newsvoca.dto;

import com.nsunf.newsvoca.entity.Paragraph;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class ParagraphDto extends ArticleContentDto {
    String content;
    String translation;

    @QueryProjection
    public ParagraphDto(Paragraph paragraph) {
        this.id = paragraph.getId();
        this.contentOrder = paragraph.getContentOrder();
        this.content = paragraph.getContent();
        this.translation = paragraph.getTranslation();
    }
}
