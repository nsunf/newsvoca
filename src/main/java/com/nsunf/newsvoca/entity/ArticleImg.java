package com.nsunf.newsvoca.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ArticleImg {
    @Id
    @Column(name = "article_content_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "article_content_id", nullable = false)
    @MapsId
    private ArticleContent articleContent;

    @Column(columnDefinition = "VARCHAR2(1024)")
    private String caption;

    @Column(nullable = false)
    private String url;

//    @Column(nullable = false)
//    private String filename;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private String repYN;
}