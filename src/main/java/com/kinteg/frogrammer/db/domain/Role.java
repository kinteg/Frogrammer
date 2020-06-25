package com.kinteg.frogrammer.db.domain;

import lombok.Getter;

public enum Role {

    ROLE_ADMIN("ADMIN"), ROLE_USER("USER");

    @Getter
    private final String shortName;

    Role(String shortName) {
        this.shortName = shortName;
    }

}
