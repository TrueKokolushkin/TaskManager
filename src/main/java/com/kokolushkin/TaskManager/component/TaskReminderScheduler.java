package com.kokolushkin.TaskManager.component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kokolushkin.TaskManager.dao.TaskRepository;
import com.kokolushkin.TaskManager.entity.Task;
import com.kokolushkin.TaskManager.entity.User;
import com.kokolushkin.TaskManager.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskReminderScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void sendTaskReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMin = now.plusMinutes(1);

        List<Task> tasks = taskRepository.findByDateTimeBetween(now, nextMin);
        for (Task task : tasks) {
            try {
                emailService.sendTaskReminder(task.getUser().getEmail(), task.getTitle(), task.getDescription());
            } catch (MessagingException e) {
                log.error("Error sending notification for task '{}': {} ", task.getTitle(), e.getMessage(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyTaskSummary() {
        LocalDateTime startOfDay = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        try {
            List<Task> tasks = taskRepository.findByDateTimeBetween(startOfDay, endOfDay);
            Map<User, List<Task>> taskByUser = tasks.stream().collect(Collectors.groupingBy(Task::getUser));

            for (var entry : taskByUser.entrySet()) {
                User user = entry.getKey();
                List<Task> userTasks = entry.getValue();

                emailService.sendDailySummary(user.getEmail(), userTasks);
            }
        } catch (Exception e) {
            log.error("Error sending daily reminders");
        }
    }
}
