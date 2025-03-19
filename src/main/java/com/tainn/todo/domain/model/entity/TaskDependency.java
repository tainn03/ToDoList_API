package com.tainn.todo.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_dependencies",
        uniqueConstraints = @UniqueConstraint(name = "uq_task_dependency", columnNames = {"task_id", "depends_on_task_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "depends_on_task_id", nullable = false)
    private Task dependsOnTask;
}
