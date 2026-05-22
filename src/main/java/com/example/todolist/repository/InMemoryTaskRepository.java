package com.example.todolist.repository;

import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskRepository {

  private final ConcurrentHashMap<Long, Task> store = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public List<Task> findAll() {
    return new ArrayList<>(store.values());
  }

  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public Task save(Task task) {
    if (task.getId() == null) {
      task.setId(idGenerator.getAndIncrement());
    }
    store.put(task.getId(), task);
    return task;
  }

  public void deleteById(Long id) {
    store.remove(id);
  }
}