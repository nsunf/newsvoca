package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ArticleContentId;
import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ArticleContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id")
    private Long id;

    @Column(name = "content_order", nullable = false)
    private Long contentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    protected Article article;
}
