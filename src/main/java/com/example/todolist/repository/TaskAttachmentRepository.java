package com.example.todolist.repository;

import com.example.todolist.model.TaskAttachment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class TaskAttachmentRepository {
  private final Map<Long, TaskAttachment> store = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public TaskAttachment save(TaskAttachment attachment) {
    if (attachment.getId() == null) {
      attachment.setId(idGenerator.getAndIncrement());
    }
    store.put(attachment.getId(), attachment);
    return attachment;
  }

  public Optional<TaskAttachment> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public List<TaskAttachment> findByTaskId(Long taskId) {
    return store.values().stream()
            .filter(a -> a.getTaskId().equals(taskId))
            .collect(Collectors.toList());
  }

  public void deleteById(Long id) {
    store.remove(id);
  }
}