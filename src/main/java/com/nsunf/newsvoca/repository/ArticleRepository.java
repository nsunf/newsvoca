package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    public boolean existsByOriUrl(String oriUrl);
    @Query("SELECT MAX(a.id) from Article a")
    public Optional<Long> findNextId();
}
