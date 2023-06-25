package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ParagraphDto;
import com.nsunf.newsvoca.dto.QParagraphDto;
import com.nsunf.newsvoca.entity.QParagraph;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParagraphRepositoryCustomImpl implements ParagraphRepositoryCustom {
    private final JPAQueryFactory qf;
    @Override
    public List<ParagraphDto> getParagraphContentsByArticleId(Long id) {
        QParagraph paragraph = QParagraph.paragraph;

        return qf
                .select(new QParagraphDto(paragraph))
                .from(paragraph)
                .where(paragraph.article.id.eq(id).and(paragraph.titleYN.eq("N")))
                .orderBy(paragraph.contentOrder.asc())
                .fetch();

    }
}
