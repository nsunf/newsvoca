package com.nsunf.newsvoca.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Paragraph extends ArticleContent {
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("")
    private String translation;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private String titleYN;
}
