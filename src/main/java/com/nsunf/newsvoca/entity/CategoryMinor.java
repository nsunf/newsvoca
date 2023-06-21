package com.nsunf.newsvoca.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMinor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_minor_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="category_major_id", nullable = false)
    private CategoryMajor categoryMajor;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String pathname;
    @Column(nullable = false)
    private int catOrder;
}
