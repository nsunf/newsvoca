package com.nsunf.newsvoca.repository;

import com.nsunf.newsvoca.entity.ArticleImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImgRepository extends JpaRepository<ArticleImg, Long>, ArticleImgRepositoryCustom {
}
