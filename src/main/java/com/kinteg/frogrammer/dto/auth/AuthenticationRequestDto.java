package com.kinteg.frogrammer.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthenticationRequestDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
