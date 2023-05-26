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
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    private String refreshToken;
    @OneToOne(mappedBy = "refreshToken")
    private UserEntity userEntity;

}
