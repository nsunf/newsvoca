package com.nsunf.newsvoca.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMajor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_major_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String pathname;
    @Column(nullable = false)
    private int catOrder;
    @Column(nullable = false)
    private char repYN;
}
