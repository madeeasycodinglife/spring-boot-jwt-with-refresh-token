package com.madeeasy.controller;


import com.madeeasy.dto.AuthenticationRequest;
import com.madeeasy.dto.RefreshTokenRequest;
import com.madeeasy.dto.UserRequest;
import com.madeeasy.dto.UserResponse;
import com.madeeasy.entity.UserEntity;
import com.madeeasy.service.JwtUtils;
import com.madeeasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome to madeeasy.com from demo project";
    }

    @GetMapping("/")
    public String home() {
        return "welcome";
    }

    @PostMapping("/api/authentication")
    public ResponseEntity<?> getAuthenticated(@RequestBody AuthenticationRequest authenticationRequest) {
        Boolean flag = userService.existsByEmailAndPassword(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        UserEntity user = userService.findUserByEmail(authenticationRequest.getEmail());
        Boolean isJwtTokenExists = userService.isJwtTokenExistsFindByEmail(authenticationRequest.getEmail());
        Boolean isRefreshTokenExists = userService.isRefreshTokenExistsFindByEmail(authenticationRequest.getEmail());
        if (flag && user != null &&  !isJwtTokenExists && !isRefreshTokenExists) {
            UserResponse userResponse = userService.generateToken(user.getEmail());
              return ResponseEntity.ok(new UserResponse(user.getEmail(), userResponse.getJwtToken(), userResponse.getRefreshToken()));
        }else {
            userService.revokeJwtToken(authenticationRequest.getEmail());
            userService.revokeRefreshToken(authenticationRequest.getEmail());
            Boolean isJwtTokenExistsAfterRevoking =  userService.isJwtTokenExistsFindByEmail(authenticationRequest.getEmail());
            Boolean isRefreshTokenExistsAfterRevoking =  userService.isRefreshTokenExistsFindByEmail(authenticationRequest.getEmail());
            if (!isJwtTokenExistsAfterRevoking && !isRefreshTokenExistsAfterRevoking) {
                UserResponse userResponse = userService.generateToken(user.getEmail());
                return ResponseEntity.ok(new UserResponse(user.getEmail(), userResponse.getJwtToken(), userResponse.getRefreshToken()));
            }
        }
        return ResponseEntity.ok(new UserResponse());
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        ResponseEntity<UserEntity> userEntityResponseEntity = userService.registerUser(userRequest);
        return ResponseEntity.ok(userEntityResponseEntity);
    }
    /**
     * refresh token endpoint
     *
     * @return JWT Token
     */
    @PostMapping("/api/refresh")
    public UserResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        Boolean isRefreshTokenExists = userService.isRefreshTokenExistsFindByRefreshToken(request.getToken());
        String email = userService.findByRefreshToken(request.getToken());
        if (isRefreshTokenExists && email!= null){
            userService.revokeJwtToken(email);
            userService.revokeRefreshToken(email);
            return userService.generateToken(email);
        }
        return new UserResponse();
    }
}
