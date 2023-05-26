package com.madeeasy.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserResponse {

    private String email;
    private String jwtToken;
    private String refreshToken;
}
