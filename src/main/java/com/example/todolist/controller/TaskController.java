package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контроллер для управления задачами
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @Autowired
  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping
  public List<TaskResponseDto> getAllTasks() {
    return taskService.getAllTasks().stream()
            .map(taskMapper::toResponseDto)
            .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
    return taskService.getTaskById(id)
            .map(taskMapper::toResponseDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskResponseDto createTask(@RequestBody TaskCreateDto createDto) {
    Task task = taskMapper.toEntity(createDto);
    task.setCreatedAt(LocalDateTime.now()); // устанавливаем дату создания
    Task saved = taskService.createTask(task);
    return taskMapper.toResponseDto(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id,
                                                    @RequestBody TaskUpdateDto updateDto) {
    return taskService.getTaskById(id).map(existing -> {
      // Частичное обновление: копируем только не-null поля из DTO
      taskMapper.updateEntity(updateDto, existing);
      Task updated = taskService.updateTask(id, existing).orElse(existing);
      return ResponseEntity.ok(taskMapper.toResponseDto(updated));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    if (taskService.deleteTask(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}