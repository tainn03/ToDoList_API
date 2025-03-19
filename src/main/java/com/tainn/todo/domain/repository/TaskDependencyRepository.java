package com.tainn.todo.domain.repository;

import com.tainn.todo.domain.model.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    @Query("SELECT td FROM TaskDependency td WHERE td.task.id = ?1 AND td.dependsOnTask.id = ?2")
    Optional<TaskDependency> findByTaskIdAndDependsOnTaskId(Long taskId, Long dependencyId);
}
