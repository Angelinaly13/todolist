package com.example.todolist.controller;

import com.example.todolist.model.Task;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private static Long createdTaskId;

  @Test
  @Order(1)
  void testCreateTask_Positive() {
    String requestJson = """
                {
                    "title": "Тестовая задача",
                    "description": "Описание",
                    "dueDate": "2026-12-31",
                    "priority": "HIGH",
                    "tags": ["тест", "важно"]
                }
                """;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

    ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", entity, Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isNotNull();

    createdTaskId = response.getBody().getId();
  }

  @Test
  @Order(2)
  void testGetAllTasks_Positive() {
    ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotEmpty();
  }

  @Test
  @Order(3)
  void testGetTaskById_Positive() {
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/" + createdTaskId, Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getTitle()).isEqualTo("Тестовая задача");
  }

  @Test
  @Order(4)
  void testGetTaskById_Negative_NotFound() {
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/99999", Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Order(5)
  void testUpdateTask_Positive() {
    String updateJson = """
                {
                    "title": "Обновлённая задача",
                    "description": "Новое описание",
                    "completed": true,
                    "dueDate": "2026-12-31",
                    "priority": "MEDIUM",
                    "tags": ["обновлено"]
                }
                """;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(updateJson, headers);

    ResponseEntity<Task> response = restTemplate.exchange(
            "/api/tasks/" + createdTaskId,
            HttpMethod.PUT,
            entity,
            Task.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getTitle()).isEqualTo("Обновлённая задача");
  }

  @Test
  @Order(6)
  void testUpdateTask_Negative_NotFound() {
    String updateJson = """
                {
                    "title": "Неважно",
                    "description": "Неважно"
                }
                """;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(updateJson, headers);

    ResponseEntity<Task> response = restTemplate.exchange(
            "/api/tasks/99999",
            HttpMethod.PUT,
            entity,
            Task.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @Order(7)
  void testDeleteTask_Positive() {
    ResponseEntity<Void> response = restTemplate.exchange(
            "/api/tasks/" + createdTaskId,
            HttpMethod.DELETE,
            null,
            Void.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @Order(8)
  void testDeleteTask_Negative_NotFound() {
    ResponseEntity<Void> response = restTemplate.exchange(
            "/api/tasks/99999",
            HttpMethod.DELETE,
            null,
            Void.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}