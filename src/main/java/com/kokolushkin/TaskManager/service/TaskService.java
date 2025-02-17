package com.kokolushkin.TaskManager.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kokolushkin.TaskManager.entity.Task;

public interface TaskService {

    public List<Task> getUserTasks(String username, Task.Priority priority, String keyword,
                                   LocalDateTime startDate, LocalDateTime endDate,
                                   String sortField, String sortDirecString);

    public Task getUserTaskById(int id, String username);

    public Task createTask(Task task, String username);

    public Task updateTask(int id, Task task, String username);

    public void deleteUserTask(int id, String username);
}
