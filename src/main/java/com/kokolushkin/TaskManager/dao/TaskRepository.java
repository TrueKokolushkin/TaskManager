package com.kokolushkin.TaskManager.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.kokolushkin.TaskManager.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    List<Task> findByUserUsername(String username, Sort sort);
    Optional<Task> findByIdAndUserUsername(int id, String username);
    List<Task> findByUserUsernameAndPriority(String username, Task.Priority priority, Sort sort);
    List<Task> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
