package com.nsunf.newsvoca.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryMinorDto {
    private Long id;
    private String name;
    private String pathname;
    private CategoryMajorDto categoryMajor;
}
