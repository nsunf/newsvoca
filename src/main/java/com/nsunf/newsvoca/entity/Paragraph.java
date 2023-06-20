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
    @Column(name = "paragraph_id")
    private long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "paragraph_id")
    private ArticleContent articleContent;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("")
    private String translation;

    @Column(nullable = false)
    @ColumnDefault("'N'")
    private String titleYN;
}
