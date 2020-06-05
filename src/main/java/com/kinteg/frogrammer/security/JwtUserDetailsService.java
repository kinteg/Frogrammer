package com.kinteg.frogrammer.security;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.security.jwt.JwtUser;
import com.kinteg.frogrammer.security.jwt.JwtUserFactory;
import com.kinteg.frogrammer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User with username: " + username + "not found");
        }

        return JwtUserFactory.create(user);
    }
}
