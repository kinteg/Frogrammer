package com.kinteg.frogrammer.controller;

import com.kinteg.frogrammer.db.domain.User;
import com.kinteg.frogrammer.dto.UserDto;
import com.kinteg.frogrammer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User result = userService.findById(id);

        if (Objects.isNull(result)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(UserDto.formUser(result), HttpStatus.OK);
    }
}
