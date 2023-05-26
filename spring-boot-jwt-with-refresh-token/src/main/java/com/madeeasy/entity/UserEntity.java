package com.madeeasy.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Entity
@Table(name = "user_tbl")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    /**
     * this @ElementCollection is mandatory to specify that the field
     * represents a collection of elements(not entities).
     * example :
     * @Embeddable
     * public class Roles {
     *     private String name;
     *
     *     // Constructors, getters, setters
     * }
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "refreshtoken_id")
    private RefreshToken refreshToken;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "jwttoken_id")
    private JwtToken jwtToken;

}
