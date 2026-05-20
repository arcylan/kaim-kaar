package com.kaim.kaar.controller;

import com.kaim.kaar.DTOs.LoginResponseDTO;
import com.kaim.kaar.DTOs.RegisterResponseDTO;
import com.kaim.kaar.entity.User;
import com.kaim.kaar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody User user) {
        RegisterResponseDTO register = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(register);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody User user) {
        LoginResponseDTO login = userService.login(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(login);

    }
}
