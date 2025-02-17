package com.kokolushkin.TaskManager.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kokolushkin.TaskManager.dto.TaskDTO;
import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getUserTasks(
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "dateTime") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        String email = getCurrentUserEmail();
        List<TaskDTO> taskDTOs = taskService.getUserTasks(email, priority, keyword,
                                                          startDate, endDate, sortField,
                                                          sortDirection)
                                            .stream().map(TaskDTO::new).toList();
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getUserTaskById(@PathVariable Integer id){
        Task task = taskService.getUserTaskById(id, getCurrentUserEmail());
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody Task task){
        Task newTask = taskService.createTask(task, getCurrentUserEmail());
        return ResponseEntity.ok(new TaskDTO(newTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Integer id, @RequestBody Task task){
        Task newTask = taskService.updateTask(id, task, getCurrentUserEmail());
        return ResponseEntity.ok(new TaskDTO(newTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTask(@PathVariable Integer id){
        taskService.deleteUserTask(id, getCurrentUserEmail());
        return ResponseEntity.ok("Task successfully deleted.");
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
