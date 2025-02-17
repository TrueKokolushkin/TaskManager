package com.kokolushkin.TaskManager.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kokolushkin.TaskManager.component.JwtUtil;
import com.kokolushkin.TaskManager.dto.RegisterRequest;
import com.kokolushkin.TaskManager.entity.User;
import com.kokolushkin.TaskManager.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegisterRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());

        if (user.isPresent() && userService.checkPassword(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok(jwtUtil.generateToken(user.get().getEmail()));
        }

        return ResponseEntity.status(401).body("Incorrect login or password.");
    }
}
