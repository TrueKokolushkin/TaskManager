package com.kokolushkin.TaskManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kokolushkin.TaskManager.entity.Task;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTaskReminder(String to, String TaskTitle, String taskDescription) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            
            messageHelper.setFrom("MyTaskManager@task");
            messageHelper.setTo(to);
            messageHelper.setSubject("Task reminder: " + TaskTitle);
            messageHelper.setText("Task description: " + taskDescription);
            
            javaMailSender.send(message);
        } catch(MailException | MessagingException e) {
            log.error("Error sending notification for task '{}' to email '{}': {}", TaskTitle, to, e.getMessage(), e);
        }
    }

    public void sendDailySummary(String to, List<Task> tasks) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            messageHelper.setFrom("MyTaskManager@task");
            messageHelper.setTo(to);
            messageHelper.setSubject("Your tasks for the coming day");

            StringBuilder message = new StringBuilder("To-do list:\n\n");
            tasks.forEach(task ->{
                message.append("- ")
                       .append(task.getTitle())
                       .append(" at ")
                       .append(task.getDateTime().toLocalTime())
                       .append("\n");
            });
            messageHelper.setText(message.toString());

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error sending notification for tasks to email '{}': {}", to, e.getMessage(), e);
        }
    }
}
