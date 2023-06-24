package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ArticleImgId;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ArticleImgId.class)
public class ArticleImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private Long contentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

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