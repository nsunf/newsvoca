package com.nsunf.newsvoca.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Word extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "world")
    private Long id;
    @Column(nullable = false, unique = true)
    private String text;
    @Column(nullable = false)
    private String definition;
    @Column(nullable = false)
    @ColumnDefault("0")
    private int savedCnt;
}
