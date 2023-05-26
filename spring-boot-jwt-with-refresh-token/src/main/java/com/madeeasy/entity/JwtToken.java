package com.madeeasy.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Entity
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue
    private Long id;
    private String jwtToken;
    @OneToOne(mappedBy = "jwtToken")
    private UserEntity userEntity;

}
