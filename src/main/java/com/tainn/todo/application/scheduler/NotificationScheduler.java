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
        taskService.getAll().forEach(task -> {
            if (isNotDone(task.getStatus())) {
                if (LocalDateTime.now().isAfter(task.getDueDate())) {
                    log.info("TASK {} IS OVERDUE", task.getId());
                } else if (LocalDateTime.now().isAfter(task.getDueDate().plusHours(6))) {
                    log.info("TASK {} IS UPCOMING", task.getId());
                }
            }
        });
    }

    private boolean isNotDone(TaskStatus status) {
        return !status.equals(TaskStatus.COMPLETED) && !status.equals(TaskStatus.CANCELLED);
    }
}
