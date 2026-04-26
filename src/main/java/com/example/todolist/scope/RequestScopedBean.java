package com.example.todolist.scope;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequestScope
public class RequestScopedBean {
  private final String requestId;
  private final LocalDateTime startTime;

  public RequestScopedBean() {
    this.requestId = UUID.randomUUID().toString();
    this.startTime = LocalDateTime.now();
    System.out.println(">>> Создан новый RequestScopedBean: " + requestId);
  }

  public String getRequestId() { return requestId; }
  public LocalDateTime getStartTime() { return startTime; }
}