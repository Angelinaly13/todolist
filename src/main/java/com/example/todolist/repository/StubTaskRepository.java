package com.example.todolist.repository;

import com.example.todolist.model.Task;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * заглушка с фикс данными
 */
public class StubTaskRepository implements TaskRepository {
  private final Map<Long, Task> stubData = new HashMap<>();

  public StubTaskRepository() {
    stubData.put(100L, new Task(100L, "Stub Task", "This is a stub", false,
            LocalDateTime.now().minusDays(1), LocalDate.now().plusDays(1),
            Task.Priority.MEDIUM, new HashSet<>(Arrays.asList("stub", "example"))));

    stubData.put(101L, new Task(101L, "Another Stub", "Fixed data", true,
            LocalDateTime.now().minusDays(2), LocalDate.now().plusDays(2),
            Task.Priority.HIGH, new HashSet<>(Arrays.asList("test", "demo"))));
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(stubData.values());
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(stubData.get(id));
  }

  @Override
  public Task save(Task task) {
    stubData.put(task.getId(), task);
    return task;
  }

  @Override
  public void deleteById(Long id) {
    stubData.remove(id);
  }
}