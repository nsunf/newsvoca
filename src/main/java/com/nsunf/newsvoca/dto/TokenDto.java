package com.nsunf.newsvoca.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenDto {
    String grantType;
    String accessToken;
    Long tokenExpiresIn;
}
