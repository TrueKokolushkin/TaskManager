package com.kokolushkin.TaskManager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kokolushkin.TaskManager.dao.TaskRepository;
import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.User;
import com.kokolushkin.TaskManager.exception.TaskNotFoundException;
import com.kokolushkin.TaskManager.exception.UserNotFoundException;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<Task> getUserTasks(String username) {
        return taskRepository.findByUserUsername(username);
    }

    @Override
    public Task getUserTaskById(int id, String username) {
        return taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found or unavailable."));
    }

    @Override
    public Task createTask(Task task, String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(int id, Task taskDetail, String username) {
        return taskRepository.findByIdAndUserUsername(id, username)
                             .map(task -> {
                                 task.setTitle(taskDetail.getTitle());
                                 task.setDescription(taskDetail.getDescription());
                                 task.setPriority(taskDetail.getPriority());
                                 task.setDateTime(taskDetail.getDateTime());
                                 return taskRepository.save(task);
                             }).orElseThrow(() -> new TaskNotFoundException("Task not found."));
    }

    @Override
    public void deleteUserTask(int id, String username) {
        Task task = taskRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new TaskNotFoundException("Task not found or unavailable."));
        taskRepository.delete(task);
    }
}
