package com.roadmap.expense_tracker_api.controller;

import com.roadmap.expense_tracker_api.dto.UserDto;
import com.roadmap.expense_tracker_api.entity.UserEntity;
import com.roadmap.expense_tracker_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @PostMapping("save-users")
    public UserEntity save(@RequestBody UserDto dto) {
        return userService.createUser(dto);
    };
}
