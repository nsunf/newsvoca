package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.dto.QArticleDto;
import com.nsunf.newsvoca.entity.QArticle;
import com.nsunf.newsvoca.entity.QArticleImg;
import com.nsunf.newsvoca.entity.QParagraph;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory qf;


    @Override
    public List<ArticleDto> getArticleDtoByCategoryMajorName(String categoryMajorName) {
        QArticle article = QArticle.article;
        QParagraph paragraph = QParagraph.paragraph;
        QArticleImg articleImg = QArticleImg.articleImg;

        List<ArticleDto> result = qf
                .select(new QArticleDto(article, paragraph.content, articleImg.url, articleImg.caption))
                .from(article)
                .join(paragraph)
                .on(paragraph.articleContent.article.eq(article))
                .join(articleImg)
                .on(articleImg.repYN.eq("Y"))
                .where(
                        article.categoryMajor.name.eq(categoryMajorName)
                                .and(
                                        paragraph.articleContent.contentOrder.eq(
                                                JPAExpressions.select(paragraph.articleContent.contentOrder.min())
                                                        .from(paragraph.articleContent)
                                                        .where(paragraph.articleContent.article.eq(article)
                                                        )
                        )
                ))
                .orderBy(article.publishTime.desc())
                .limit(20)
                .fetch();
        return null;
    }

    @Override
    public List<ArticleDto> getArticleDtoByCategoryMinorName(String categoryMinorName) {
        return null;
    }
}
