package com.kokolushkin.TaskManager.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kokolushkin.TaskManager.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{

    List<Task> findByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(int id, String username);

}
