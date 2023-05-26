package com.madeeasy.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationRequest {
    private String email;
    private String password;
}
