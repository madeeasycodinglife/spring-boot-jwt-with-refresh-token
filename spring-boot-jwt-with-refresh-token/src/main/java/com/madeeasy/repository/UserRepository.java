package com.madeeasy.repository;

import com.madeeasy.entity.RefreshToken;
import com.madeeasy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String username);
    boolean existsByEmailAndPassword(String username, String password);

    /**
     * method find by refresh token id
     */
    Optional<UserEntity> findByRefreshToken_RefreshToken(String refreshToken);

}

