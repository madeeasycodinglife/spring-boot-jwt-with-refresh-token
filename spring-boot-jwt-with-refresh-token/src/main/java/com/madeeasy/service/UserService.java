package com.madeeasy.service;


import com.madeeasy.dto.UserRequest;
import com.madeeasy.dto.UserResponse;
import com.madeeasy.entity.JwtToken;
import com.madeeasy.entity.RefreshToken;
import com.madeeasy.entity.Role;
import com.madeeasy.entity.UserEntity;
import com.madeeasy.repository.JwtTokenRepository;
import com.madeeasy.repository.RefreshTokenRepository;
import com.madeeasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public ResponseEntity<UserEntity> registerUser(UserRequest userRequest) {
        Set<Role> setOfRoles = userRequest.getRoles().stream()
                /**
                 *  this valueOf is used to convert String to enum
                 *  It is equivalent to string -> Role.valueOf(string)
                 */
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        UserEntity userEntity = UserEntity.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .roles(setOfRoles)
                .build();
        UserEntity savedUser = userRepository.save(userEntity);
        return ResponseEntity.ok(savedUser);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public Boolean existsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }

    public UserResponse generateToken(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
//        JwtToken jwtToken = userEntity.getJwtToken().stream().findFirst().orElse(null); // Set<JwtToken> tp JwtToken conversion
        if (!userEntity.equals(null)) {
            JwtToken jwtToken = userEntity.getJwtToken();
            RefreshToken refreshToken = userEntity.getRefreshToken();
            if (jwtToken != null && Objects.nonNull(refreshToken)) {
                jwtTokenRepository.delete(Objects.requireNonNull(userEntity.getJwtToken()));
                String jwtTokenGenerated = jwtUtils.generateToken(email);
                refreshTokenRepository.delete(Objects.requireNonNull(userEntity.getRefreshToken()));
                String refreshTokenGenerated = jwtUtils.generateRefreshToken();
                UserResponse userResponse = getUserResponse(email, userEntity);
                return userResponse;
            } else {
                UserResponse userResponse = getUserResponse(email, userEntity);
                return userResponse;
            }
        }
        return null;
    }

    private UserResponse getUserResponse(String email, UserEntity userEntity) {
        String jwtToken = jwtUtils.generateToken(email);
        String refreshToken = jwtUtils.generateRefreshToken();
        JwtToken jwtTokenBuilder = JwtToken.builder()
                .id(userEntity.getId())
                .jwtToken(jwtToken)
                .build();
        RefreshToken refreshTokenBuilder = RefreshToken.builder()
                .id(userEntity.getId())
                .refreshToken(refreshToken)
                .build();
        UserEntity userBuilder = UserEntity.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles())
                .jwtToken(jwtTokenBuilder)
                .refreshToken(refreshTokenBuilder)
                .build();
        UserEntity updatedUser = userRepository.save(userBuilder);
        UserResponse userResponse = UserResponse.builder()
                .email(updatedUser.getEmail())
                .jwtToken(updatedUser.getJwtToken() == null ? null : updatedUser.getJwtToken().getJwtToken())
                .refreshToken(updatedUser.getRefreshToken() == null ? null : updatedUser.getRefreshToken().getRefreshToken())
                .build();
        return userResponse;
    }

    public Boolean isJwtTokenExistsFindByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        return userEntity != null && userEntity.getJwtToken() != null && userEntity.getJwtToken().getJwtToken() != null;
    }

    public Boolean isRefreshTokenExistsFindByEmail(String email) {
        Boolean flag = userRepository.findByEmail(email)
                .map(u -> u.getRefreshToken() != null && u.getRefreshToken().getRefreshToken() != null)
                .orElse(false);
        return flag;
    }

    public void revokeJwtToken(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity != null && userEntity.getJwtToken() != null && userEntity.getJwtToken().getJwtToken() != null) {
            userEntity.setJwtToken(null);
            userRepository.save(userEntity);
        }

    }

    public void revokeRefreshToken(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        if (userEntity != null && userEntity.getRefreshToken() != null && userEntity.getRefreshToken().getRefreshToken() != null) {
            userEntity.setRefreshToken(null);
            userRepository.save(userEntity);
        }
    }

    public String findByRefreshToken(String refreshToken) {
        String token = refreshTokenRepository.findByRefreshToken(refreshToken).map(x -> x.getRefreshToken()).orElse(null);
        if (token != null) {
            UserEntity userEntity = userRepository.findByRefreshToken_RefreshToken(token).orElse(null);
            if (userEntity != null) {
                return userEntity.getEmail();
            }
        }
        return null;
    }


    public Boolean isRefreshTokenExistsFindByRefreshToken(String refreshToken) {
        String token = refreshTokenRepository.findByRefreshToken(refreshToken).map(x -> x.getRefreshToken()).orElse(null);
        if (token != null) {
            return true;
        }
        return false;
    }
}
