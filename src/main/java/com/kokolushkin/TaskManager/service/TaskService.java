package com.kokolushkin.TaskManager.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.Task.Status;

public interface TaskService {

    public List<Task> getUserTasks(String username, Task.Priority priority, String keyword,
                                   LocalDateTime startDate, LocalDateTime endDate, Task.Status status,
                                   String sortField, String sortDirecString);

    public Task getUserTaskById(int id, String email);

    public Task createTask(Task task, String username);

    public Task updateTask(int id, Task task, String email);

    public void deleteUserTask(int id, String email);

    public Task updateTaskStatus(int id, String email, Status newStatus);
}
