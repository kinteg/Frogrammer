package com.kinteg.frogrammer.dto;

import com.kinteg.frogrammer.db.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SimpleUserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;

    public static SimpleUserDto toSimpleUser(User user) {
        return SimpleUserDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
