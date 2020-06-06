package com.kinteg.frogrammer.security.jwt;

import com.kinteg.frogrammer.db.domain.RoleEntity;
import com.kinteg.frogrammer.db.domain.Status;
import com.kinteg.frogrammer.db.domain.User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return JwtUser
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(mapToGrantedAuthority(user.getRole()))
                .enabled(user.getStatus().equals(Status.ACTIVE))
                .lastPasswordResetDate(user.getUpdated())
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(List<RoleEntity> userRoleEntities) {
        return userRoleEntities.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
