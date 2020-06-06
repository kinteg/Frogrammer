package com.kinteg.frogrammer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kinteg.frogrammer.db.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String username;
    private String firstName;
    private String lastName;

    public static RegisterDto formUser(User user) {
        return RegisterDto
                .builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
