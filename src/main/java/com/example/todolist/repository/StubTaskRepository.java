package com.example.todolist.repository;

import com.example.todolist.model.Task;
import java.util.*;

public class StubTaskRepository implements TaskRepository {
  private final Map<Long, Task> stubData = new HashMap<>();

  public StubTaskRepository() {
    stubData.put(100L, new Task(100L, "Stub Task", "This is a stub", false));
    stubData.put(101L, new Task(101L, "Another Stub", "Fixed data", true));
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