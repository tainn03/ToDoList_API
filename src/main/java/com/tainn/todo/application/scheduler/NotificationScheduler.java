package com.tainn.todo.application.scheduler;

import com.tainn.todo.domain.model.vo.TaskStatus;
import com.tainn.todo.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class NotificationScheduler {
    TaskService taskService;

    // Notification system for upcoming/overdue tasks
    @Scheduled(fixedRate = 1000 * 60)
    public void notificationUpcomingTasks() {
        log.info("NOTIFICATION UPCOMING TASKS");
        taskService.getAll().forEach(task -> {
            if (LocalDateTime.now().isAfter(task.getDueDate()) && isNotDone(task.getStatus())) {
                log.info("Task {} is overdue task", task.getId());
            } else if (LocalDateTime.now().isBefore(task.getDueDate()) && isNotDone(task.getStatus())) {
                log.info("Task {} is upcoming task", task.getId());
            }
        });
    }

    private boolean isNotDone(TaskStatus status) {
        return !status.equals(TaskStatus.COMPLETED) && !status.equals(TaskStatus.CANCELLED);
    }
}
