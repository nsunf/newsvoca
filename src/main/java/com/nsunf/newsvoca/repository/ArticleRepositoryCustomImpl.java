package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.dto.ArticleDetailDto;
import com.nsunf.newsvoca.dto.ArticleDto;
import com.nsunf.newsvoca.dto.QArticleDetailDto;
import com.nsunf.newsvoca.dto.QArticleDto;
import com.nsunf.newsvoca.entity.QArticle;
import com.nsunf.newsvoca.entity.QArticleImg;
import com.nsunf.newsvoca.entity.QParagraph;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory qf;


    @Override
    public List<ArticleDto> getArticleDtoByCategoryMajorName(String categoryMajorName, int count) {
        QArticle article = QArticle.article;
        QParagraph paragraph = QParagraph.paragraph;
        QArticleImg articleImg = QArticleImg.articleImg;

        List<ArticleDto> result = qf
                .select(new QArticleDto(article, paragraph.content, articleImg.url, articleImg.caption))
                .from(article)
                .join(paragraph)
                .on(paragraph.previewYN.eq("Y").and(paragraph.article.eq(article)))
                .join(articleImg)
                .on(articleImg.repYN.eq("Y").and(articleImg.article.eq(article)))
                .where(article.categoryMajor.name.equalsIgnoreCase(categoryMajorName))
                .orderBy(article.publishTime.desc())
                .limit(20)
                .fetch();

        return result;
    }

    @Override
    public List<ArticleDto> getArticleDtoByCategoryMinorName(String categoryMinorName, int count) {
        QArticle article = QArticle.article;
        QParagraph paragraph = QParagraph.paragraph;
        QArticleImg articleImg = QArticleImg.articleImg;

        List<ArticleDto> result = qf
                .select(new QArticleDto(article, paragraph.content, articleImg.url, articleImg.caption))
                .from(article)
                .join(paragraph)
                .on(paragraph.previewYN.eq("Y").and(paragraph.article.eq(article)))
                .join(articleImg)
                .on(articleImg.repYN.eq("Y").and(articleImg.article.eq(article)))
                .where(article.categoryMinor.name.equalsIgnoreCase(categoryMinorName))
                .orderBy(article.publishTime.desc())
                .limit(20)
                .fetch();

        return result;
    }

    @Override
    public ArticleDetailDto getArticleDetailById(Long id) {
        QArticle article = QArticle.article;
        QParagraph paragraph = QParagraph.paragraph;
        QArticleImg articleImg = QArticleImg.articleImg;

        ArticleDetailDto result = qf
                .select(new QArticleDetailDto(article, paragraph, articleImg))
                .from(article)
                .join(paragraph)
                .on(paragraph.article.eq(article).and(paragraph.titleYN.eq("Y")))
                .join(articleImg)
                .on(articleImg.article.eq(article).and(articleImg.repYN.eq("Y")))
                .where(article.id.eq(id))
                .fetchOne();

        return result;
    }
}
