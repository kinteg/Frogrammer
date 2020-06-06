package com.kinteg.frogrammer.security;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.db.repository.UserRepo;
import com.kinteg.frogrammer.security.jwt.JwtUserFactory;
import com.kinteg.frogrammer.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

        return JwtUserFactory.create(user);
    }
}
