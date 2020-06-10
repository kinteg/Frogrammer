package com.kinteg.frogrammer.service.user;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.dto.AuthenticationRequestDto;
import com.kinteg.frogrammer.dto.AuthenticationResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface UserLoginService {

    User getAuthUser();

    AuthenticationResponseDto login(AuthenticationRequestDto auth);

}
