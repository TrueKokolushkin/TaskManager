package com.kokolushkin.TaskManager.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kokolushkin.TaskManager.dto.TaskDTO;
import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.Task.Status;
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
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Task.Priority priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Task.Status status,
            @RequestParam(required = false, defaultValue = "dateTime") String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        List<TaskDTO> taskDTOs = taskService.getUserTasks(userDetails.getUsername(), priority, keyword,
                                                          startDate, endDate, status, sortField, sortDirection)
                                            .stream().map(TaskDTO::new).toList();
        return ResponseEntity.ok(taskDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getUserTaskById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id){
        Task task = taskService.getUserTaskById(id, userDetails.getUsername());
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Task task){
        Task newTask = taskService.createTask(task, userDetails.getUsername());
        return ResponseEntity.ok(new TaskDTO(newTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id, @RequestBody Task task){
        Task newTask = taskService.updateTask(id, task, userDetails.getUsername());
        return ResponseEntity.ok(new TaskDTO(newTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTask(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id){
        taskService.deleteUserTask(id, userDetails.getUsername());
        return ResponseEntity.ok("Task successfully deleted.");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Integer id,
                                                    @RequestParam Status status,
                                                    @AuthenticationPrincipal UserDetails userDetails) {

        Task updatedTask = taskService.updateTaskStatus(id, userDetails.getUsername(), status);
        return ResponseEntity.ok(new TaskDTO(updatedTask));
    }
}
