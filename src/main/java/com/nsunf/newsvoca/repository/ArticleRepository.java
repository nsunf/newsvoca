package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
