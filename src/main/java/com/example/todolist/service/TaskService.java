package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @PostConstruct
  public void initCache() {
    List<Task> allTasks = taskRepository.findAll();
    for (Task task : allTasks) {
      taskCache.put("task_" + task.getId(), task);
    }
  }

  @PreDestroy
  public void cleanUp() {
    taskCache.clear();
  }

  @Transactional(readOnly = true)
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<Task> getTaskById(Long id) {
    return taskRepository.findById(id);
  }

  @Transactional
  public Task createTask(Task task) {
    if (task.getCreatedAt() == null) {
      task.setCreatedAt(LocalDateTime.now());
    }
    if (task.getUpdatedAt() == null) {
      task.setUpdatedAt(LocalDateTime.now());
    }
    Task saved = taskRepository.save(task);
    taskCache.put("task_" + saved.getId(), saved);
    return saved;
  }

  @Transactional
  public Optional<Task> updateTask(Long id, Task updatedTask) {
    return taskRepository.findById(id).map(existing -> {
      if (updatedTask.getTitle() != null) {
        existing.setTitle(updatedTask.getTitle());
      }
      if (updatedTask.getDescription() != null) {
        existing.setDescription(updatedTask.getDescription());
      }
      existing.setCompleted(updatedTask.isCompleted());
      if (updatedTask.getDueDate() != null) {
        existing.setDueDate(updatedTask.getDueDate());
      }
      if (updatedTask.getPriority() != null) {
        existing.setPriority(updatedTask.getPriority());
      }
      if (updatedTask.getTags() != null) {
        existing.setTags(updatedTask.getTags());
      }
      existing.setUpdatedAt(LocalDateTime.now());

      Task saved = taskRepository.save(existing);
      taskCache.put("task_" + saved.getId(), saved);
      return saved;
    });
  }

  @Transactional
  public boolean deleteTask(Long id) {
    if (taskRepository.existsById(id)) {
      taskRepository.deleteById(id);
      taskCache.remove("task_" + id);
      return true;
    }
    return false;
  }

  @Transactional(readOnly = true)
  public List<Task> getTasksByCompletedAndPriority(boolean completed, Task.Priority priority) {
    return taskRepository.findByCompletedAndPriority(completed, priority);
  }

  @Transactional(readOnly = true)
  public List<Task> getTasksByDueDateBetween(LocalDate from, LocalDate to) {
    return taskRepository.findByDueDateBetween(from, to);
  }

  @Transactional(readOnly = true)
  public List<Task> getAllTasksWithAttachments() {
    return taskRepository.findAllWithAttachments();
  }

  public Map<String, Task> getTaskCache() {
    return taskCache;
  }
}