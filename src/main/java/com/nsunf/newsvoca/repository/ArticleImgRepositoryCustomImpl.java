package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleImgDto;
import com.nsunf.newsvoca.dto.QArticleImgDto;
import com.nsunf.newsvoca.entity.QArticleImg;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleImgRepositoryCustomImpl implements ArticleImgRepositoryCustom {
    private final JPAQueryFactory qf;
    @Override
    public List<ArticleImgDto> getArticleImgContentsByArticleId(Long id) {
        QArticleImg articleImg = QArticleImg.articleImg;

        return qf
                .select(new QArticleImgDto(articleImg))
                .from(articleImg)
                .where(articleImg.article.id.eq(id).and(articleImg.repYN.eq("N")))
                .orderBy(articleImg.contentOrder.asc())
                .fetch();
    }
}
