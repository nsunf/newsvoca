package com.nsunf.newsvoca.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paragraph {
    @Id
    @Column(name = "article_content_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "article_content_id", nullable = false)
    @MapsId
    private ArticleContent articleContent;

    @Column(nullable = false, columnDefinition = "VARCHAR2(1024)")
    private String content;

    @Column
    private String translation;

    @Column(name = "title_yn", nullable = false)
    @ColumnDefault("'N'")
    private String titleYN;
}
