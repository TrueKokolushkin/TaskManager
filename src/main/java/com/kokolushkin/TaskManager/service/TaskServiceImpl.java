package com.kokolushkin.TaskManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kokolushkin.TaskManager.dao.TaskRepository;
import com.kokolushkin.TaskManager.entity.Task;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with ID " + id + " not found"));
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(int id, Task taskDetail) {
        return taskRepository.findById(id)
                             .map(task -> {
                                 task.setTitle(taskDetail.getTitle());
                                 task.setDescription(taskDetail.getDescription());
                                 task.setPriority(taskDetail.getPriority());
                                 task.setDateTime(taskDetail.getDateTime());
                                 return taskRepository.save(task);
                             }).orElseThrow(() -> new RuntimeException("Task not found."));
    }

    @Override
    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }
}
