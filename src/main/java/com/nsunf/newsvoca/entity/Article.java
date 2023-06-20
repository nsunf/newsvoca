package com.nsunf.newsvoca.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends BaseEntity {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_major_id", nullable = false)
    private CategoryMajor categoryMajor;
    @ManyToOne
    @JoinColumn(name = "category_minor_id")
    private CategoryMinor categoryMinor;
    @Column(nullable = false)
    private String oriUrl;
    @Column(nullable = false)
    private String pathname;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String slug;
    @Column
    private LocalDateTime publishTime;
    @Column
    private String author;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int view;
}
