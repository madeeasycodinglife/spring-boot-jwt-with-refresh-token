package com.madeeasy.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

public class RefreshTokenRequest {
    private String token;
}
