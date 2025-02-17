package com.kokolushkin.TaskManager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kokolushkin.TaskManager.dao.TaskRepository;
import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.User;
import com.kokolushkin.TaskManager.exception.TaskNotFoundException;
import com.kokolushkin.TaskManager.exception.UserNotFoundException;

import jakarta.persistence.criteria.Predicate;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<Task> getUserTasks(String username, Task.Priority priority, String keyword, LocalDateTime startDate, LocalDateTime endDate, String sortField, String sortDirecString) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirecString), sortField);
        Specification<Task> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("username"), username));

            if (Objects.nonNull(priority)) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }

            if (Objects.nonNull(keyword) && !keyword.isEmpty() && !keyword.isBlank()) {
                Predicate titleContains = cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionContains = cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                predicates.add(cb.or(titleContains, descriptionContains));
            }

            if (Objects.nonNull(startDate)) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateTime"), startDate));
            }

            if (Objects.nonNull(endDate)) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateTime"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(spec, sort);
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
