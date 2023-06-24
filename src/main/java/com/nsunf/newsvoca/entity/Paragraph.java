package com.nsunf.newsvoca.entity;

import com.nsunf.newsvoca.entity.composite_key.ParagraphId;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ParagraphId.class)
public class Paragraph extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Id
    private Long contentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(nullable = false, columnDefinition = "VARCHAR2(1024)")
    private String content;

    @Column
    private String translation;

    @Column(name = "title_yn", nullable = false)
    @ColumnDefault("'N'")
    private String titleYN;
}
