package com.kokolushkin.TaskManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kokolushkin.TaskManager.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{

}
