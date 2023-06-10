package com.nsunf.newsvoca.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String title;
    private String preview;
    private String repImg;
    private String repImgDesc;
    private String url;
}
