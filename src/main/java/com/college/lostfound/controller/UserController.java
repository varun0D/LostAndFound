package com.college.lostfound.controller;

import com.college.lostfound.model.User;
import com.college.lostfound.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return service.login(user.getEmail(), user.getPassword());
    }
}
