package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для работы с задачами
 */
@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @PostConstruct
  public void initCache() {
    System.out.println(">>> @PostConstruct: Инициализация кэша TaskService");
    List<Task> allTasks = taskRepository.findAll();
    for (Task task : allTasks) {
      taskCache.put("task_" + task.getId(), task);
    }
    System.out.println(">>> Загружено " + taskCache.size() + " задач в кэш.");
  }

  @PreDestroy
  public void cleanUp() {
    System.out.println(">>> @PreDestroy: Очистка ресурсов TaskService. Размер кэша перед удалением: " + taskCache.size());
    taskCache.clear();
    System.out.println(">>> Кэш очищен.");
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Optional<Task> getTaskById(Long id) {
    return taskRepository.findById(id);
  }

  public Task createTask(Task task) {
    if (task.getCreatedAt() == null) {
      task.setCreatedAt(LocalDateTime.now());
    }
    return taskRepository.save(task);
  }

  public Optional<Task> updateTask(Long id, Task updatedTask) {
    return taskRepository.findById(id).map(existing -> {
      if (updatedTask.getTitle() != null) existing.setTitle(updatedTask.getTitle());
      if (updatedTask.getDescription() != null) existing.setDescription(updatedTask.getDescription());
      existing.setCompleted(updatedTask.isCompleted());
      if (updatedTask.getDueDate() != null) existing.setDueDate(updatedTask.getDueDate());
      if (updatedTask.getPriority() != null) existing.setPriority(updatedTask.getPriority());
      if (updatedTask.getTags() != null) existing.setTags(updatedTask.getTags());
      return taskRepository.save(existing);
    });
  }

  public boolean deleteTask(Long id) {
    if (taskRepository.findById(id).isPresent()) {
      taskRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public Map<String, Task> getTaskCache() {
    return taskCache;
  }
}