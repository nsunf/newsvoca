package com.nsunf.newsvoca.dto;

import lombok.Builder;

@Builder
public class MemberImgDto {
    private Long id;
    private Long memberId;
    private String url;
    private String filename;
    private String oriImgName;
}
