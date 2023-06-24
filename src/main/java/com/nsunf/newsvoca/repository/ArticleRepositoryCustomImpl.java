package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.dto.QArticleDto;
import com.nsunf.newsvoca.entity.QArticle;
import com.nsunf.newsvoca.entity.QArticleImg;
import com.nsunf.newsvoca.entity.QParagraph;
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

        qf
                .select(new QArticleDto(article, paragraph.content, articleImg.url, articleImg.caption))
                .from(article)
                .join(paragraph)
                .on(
                        paragraph.article.eq(article)
                                .and(paragraph.titleYN.eq("N"))
                                .and(paragraph.contentOrder.eq(
                                        qf.select(paragraph.contentOrder.min()).from(paragraph).where(paragraph.article.eq(article))
                                ))

                ).join(articleImg)
                .on(articleImg.article.eq(article).and(articleImg.repYN.eq("Y")))
                .where(article.categoryMajor.name.eq(categoryMajorName))
                .limit(20)
                .fetch()
                .forEach(System.out::println);

        return null;
    }

    @Override
    public List<ArticleDto> getArticleDtoByCategoryMinorName(String categoryMinorName) {
        return null;
    }
}
