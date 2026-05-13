package com.example.todolist.controller;

import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.service.FavoritesService;
import com.example.todolist.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

  private final FavoritesService favoritesService;
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public FavoritesController(FavoritesService favoritesService, TaskService taskService, TaskMapper taskMapper) {
    this.favoritesService = favoritesService;
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @PostMapping("/{taskId}")
  public ResponseEntity<Void> addFavorite(@PathVariable Long taskId, HttpSession session) {
    if (taskService.getTaskById(taskId).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    favoritesService.addFavorite(session, taskId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> removeFavorite(@PathVariable Long taskId, HttpSession session) {
    favoritesService.removeFavorite(session, taskId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public List<TaskResponseDto> getFavorites(HttpSession session) {
    return favoritesService.getFavorites(session).stream()
            .map(taskService::getTaskById)
            .filter(Optional::isPresent)
            .map(opt -> taskMapper.toResponseDto(opt.get()))
            .collect(Collectors.toList());
  }
}