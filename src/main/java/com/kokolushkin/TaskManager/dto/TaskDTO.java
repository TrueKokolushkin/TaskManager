package com.kokolushkin.TaskManager.dto;

import java.time.LocalDateTime;

import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.Task.Priority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime dateTime;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.dateTime = task.getDateTime();
    }
}
