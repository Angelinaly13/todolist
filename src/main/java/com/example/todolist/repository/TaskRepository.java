package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByCompletedAndPriority(boolean completed, Task.Priority priority);

  @Query("""
          select t
          from Task t
          where t.dueDate between :from and :to
          """)
  List<Task> findByDueDateBetween(LocalDate from, LocalDate to);

  @EntityGraph(attributePaths = "attachments")
  @Query("select t from Task t")
  List<Task> findAllWithAttachments();
}