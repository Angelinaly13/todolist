package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Repository
@org.springframework.context.annotation.Primary
public class InMemoryTaskRepository implements TaskRepository {
  private final ConcurrentHashMap<Long, Task> store = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public Task save(Task task) {
    if (task.getId() == null) {
      task.setId(idGenerator.getAndIncrement());
    }
    store.put(task.getId(), task);
    return task;
  }

  @Override
  public void deleteById(Long id) {
    store.remove(id);
  }
}