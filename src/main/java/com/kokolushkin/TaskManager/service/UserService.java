package com.kokolushkin.TaskManager.service;

import java.util.Optional;

import com.kokolushkin.TaskManager.dto.RegisterRequest;
import com.kokolushkin.TaskManager.entity.User;

public interface UserService {
    User registerUser(RegisterRequest reauest);
    Optional<User> findByEmail(String email);
    boolean checkPassword(String rawPassword, String encodedPassword);
}
