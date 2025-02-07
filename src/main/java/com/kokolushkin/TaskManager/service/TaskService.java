package com.kokolushkin.TaskManager.service;

import java.util.List;
import java.util.Optional;

import com.kokolushkin.TaskManager.entity.Task;

public interface TaskService {

    public List<Task> getAllTasks();

    public Task getTaskById(int id);

    public Task createTask(Task task);

    public Task updateTask(int id, Task task);

    public void deleteTask(int id);
}
