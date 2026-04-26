package com.example.todolist.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeScopedBean {
  private final String uniqueId;

  public PrototypeScopedBean() {
    this.uniqueId = UUID.randomUUID().toString();
    System.out.println(">>> Создан новый PrototypeScopedBean: " + uniqueId);
  }

  public String getUniqueId() {
    return uniqueId;
  }
}