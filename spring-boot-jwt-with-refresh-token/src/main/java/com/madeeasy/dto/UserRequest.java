package com.madeeasy.dto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserRequest {
    private String email;
    private String password;
    private Set<String> roles;
}
