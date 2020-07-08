package com.kinteg.frogrammer.service.user.impl;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.dto.auth.AuthenticationRequestDto;
import com.kinteg.frogrammer.dto.auth.AuthenticationResponseDto;
import com.kinteg.frogrammer.security.jwt.JwtTokenProvider;
import com.kinteg.frogrammer.security.jwt.JwtUser;
import com.kinteg.frogrammer.service.user.UserLoginService;
import org.aspectj.weaver.BCException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserLoginServiceImpl(
            @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserRepo userRepo,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getAuthUser() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepo.findById(jwtUser.getId()).orElseThrow(() -> new AuthorizationServiceException(""));
    }

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto auth) {

        String username = auth.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found."));

        authenticate(auth);
        user.setPassword(passwordEncoder.encode(auth.getPassword()));
        userRepo.save(user);

        return new AuthenticationResponseDto(username, createToken(user));
    }

    private void authenticate(AuthenticationRequestDto auth) {
        try {
            var user = new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword());
            authenticationManager.authenticate(user);
        } catch (AuthenticationException ex) {
            throw new BCException("Invalid username or password.");
        }
    }

    private String createToken(User user) {
        return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
    }


}
