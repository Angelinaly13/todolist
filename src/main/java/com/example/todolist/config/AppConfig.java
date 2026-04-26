package com.example.todolist.config;

import com.example.todolist.repository.StubTaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public StubTaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}