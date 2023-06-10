package com.nsunf.newsvoca.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
public class CategoryMinor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="category_major_id", nullable = false)
    private CategoryMajor categoryMajor;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String pathname;
}
