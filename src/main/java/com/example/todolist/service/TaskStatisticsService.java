package com.example.todolist.service;

import com.example.todolist.repository.TaskRepository;
import com.example.todolist.repository.StubTaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaskStatisticsService {

  private final TaskRepository primaryRepository;
  private final StubTaskRepository stubRepository;

  @Value("${app.name}")
  private String appName;

  @Value("${app.version}")
  private String appVersion;

  public TaskStatisticsService(
          TaskRepository primaryRepository,  // @Primary
          @Qualifier("stubTaskRepository") StubTaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  public void printStats() {
    System.out.println("=== Статистика из TaskStatisticsService ===");
    System.out.println("App: " + appName + " v" + appVersion);
    System.out.println("Основной репозиторий (InMemory) содержит задач: " + primaryRepository.findAll().size());
    System.out.println("Заглушка (Stub) содержит задач: " + stubRepository.findAll().size());
  }
}