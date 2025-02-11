package com.kokolushkin.TaskManager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kokolushkin.TaskManager.dao.UserRepository;
import com.kokolushkin.TaskManager.dto.RegisterRequest;
import com.kokolushkin.TaskManager.entity.User;
import com.kokolushkin.TaskManager.exception.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest reauest) {
        if (userRepository.findByUsername(reauest.getUsername()).isPresent()) {
            throw new RuntimeException("A user with this name already exists.");
        }

        User user = new User();
        user.setUsername(reauest.getUsername());
        user.setPassword(passwordEncoder.encode(reauest.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        UserDetails userDetails = org.springframework.security.core.userdetails
                                     .User.builder()
                                     .username(user.getUsername())
                                     .password(user.getPassword())
                                     .build();
        return userDetails;
    }
}
