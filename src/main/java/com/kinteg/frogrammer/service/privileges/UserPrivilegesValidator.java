package com.kinteg.frogrammer.service.privileges;

import com.kinteg.frogrammer.db.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserPrivilegesValidator {

    User getUser();

    boolean checkAuthPrivileges();

    boolean checkUserPrivileges(Long id);

    boolean checkModeratorPrivileges();

    boolean checkAdministratorPrivileges();

}
