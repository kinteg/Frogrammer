package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.dto.AuthenticationRequestDto;
import com.kinteg.frogrammer.dto.AuthenticationResponseDto;
import com.kinteg.frogrammer.dto.RegisterDto;
import com.kinteg.frogrammer.service.user.UserLoginService;
import com.kinteg.frogrammer.service.user.UserRegisterService;
import com.kinteg.frogrammer.service.user.UserUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserLoginService userLoginService;
    private final UserRegisterService userRegisterService;
    private final UserUpdateService userUpdateService;

    public AuthController(
            UserLoginService userLoginService, UserRegisterService userRegisterService,
            UserUpdateService userUpdateService) {
        this.userLoginService = userLoginService;
        this.userRegisterService = userRegisterService;
        this.userUpdateService = userUpdateService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto auth) {
        return ResponseEntity.ok(userLoginService.login(auth));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(RegisterDto.formUser(userRegisterService.register(user)));
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userUpdateService.update(user));
    }

}
