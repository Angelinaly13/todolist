package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import com.example.todolist.validation.OnCreate;
import com.example.todolist.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * REST контроллер для управления задачами
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Управление задачами")
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @Value("${app.version:1.0.0}")
  private String apiVersion;

  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping
  @Operation(summary = "Получить все задачи")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Успешно"),
          @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
  })
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<TaskResponseDto> tasks = taskService.getAllTasks().stream()
            .map(taskMapper::toResponseDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(tasks.size()))
            .header("X-API-Version", apiVersion)
            .body(tasks);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить задачу по ID")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Успешно"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
    Task task = taskService.getTaskById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    return ResponseEntity.ok()
            .header("X-API-Version", apiVersion)
            .body(taskMapper.toResponseDto(task));
  }

  @PostMapping
  @Operation(summary = "Создать новую задачу")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Создано"),
          @ApiResponse(responseCode = "400", description = "Неверные данные")
  })
  public ResponseEntity<TaskResponseDto> createTask(@Validated(OnCreate.class) @RequestBody TaskCreateDto createDto) {
    Task task = taskMapper.toEntity(createDto);
    task.setCreatedAt(LocalDateTime.now());
    Task saved = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED)
            .header("X-API-Version", apiVersion)
            .body(taskMapper.toResponseDto(saved));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Обновить задачу")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Обновлено"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена"),
          @ApiResponse(responseCode = "400", description = "Неверные данные")
  })
  public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id,
                                                    @Validated(OnUpdate.class) @RequestBody TaskUpdateDto updateDto) {
    Task existing = taskService.getTaskById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    taskMapper.updateEntity(updateDto, existing);
    Task updated = taskService.updateTask(id, existing).orElse(existing);
    return ResponseEntity.ok()
            .header("X-API-Version", apiVersion)
            .body(taskMapper.toResponseDto(updated));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Удалить задачу")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Удалено"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    if (taskService.deleteTask(id)) {
      return ResponseEntity.noContent()
              .header("X-API-Version", apiVersion)
              .build();
    }
    throw new TaskNotFoundException(id);
  }
}