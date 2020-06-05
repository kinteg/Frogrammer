package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.dto.AuthenticationRequestDto;
import com.kinteg.frogrammer.dto.AuthenticationResponseDto;
import com.kinteg.frogrammer.security.jwt.JwtTokenProvider;
import com.kinteg.frogrammer.service.UserService;
import org.aspectj.weaver.BCException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto auth) {
        try {
            String username = auth.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, auth.getPassword()));
            User user = userService.findByUsername(username);

            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("User with username " + username + " not found.");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            AuthenticationResponseDto responseDto = new AuthenticationResponseDto(username, token);

            return ResponseEntity.ok(responseDto);

        } catch (AuthenticationException ex) {
            throw new BCException("Invalid username or password.");
        }
    }

}
