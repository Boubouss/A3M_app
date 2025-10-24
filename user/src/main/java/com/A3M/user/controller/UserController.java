package com.A3M.user.controller;

import com.A3M.user.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Resource
    private UserService userService;


}
