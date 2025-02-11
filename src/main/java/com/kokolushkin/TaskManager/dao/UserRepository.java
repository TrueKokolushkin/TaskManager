package com.kokolushkin.TaskManager.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kokolushkin.TaskManager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
