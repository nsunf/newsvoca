package com.nsunf.newsvoca.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class ArticleImg extends ArticleContent {
    @Column
    private String caption;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String filename;
    @Column(nullable = false)
    @ColumnDefault("'N'")
    private String repYN;
}