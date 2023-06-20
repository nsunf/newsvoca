package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ArticleContentId;
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
    @Column(name = "article_img_id")
    private long id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_img_id")
    private ArticleContent articleContent;
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