package com.kinteg.frogrammer.service.user;

import com.kinteg.frogrammer.db.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserUpdateService {

    String update(User user);

}
