package com.example.todolist.mapper;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.model.Task;
import org.mapstruct.*;

/**
 * маппер для преобразования между Task и DTO
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

  /**
   * TaskCreateDto в Task
   */
  Task toEntity(TaskCreateDto dto);

  /**
   * обновление Task из TaskUpdateDto
   * поля не перезаписывают
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

  /**
   * Task в TaskResponseDto
   */
  TaskResponseDto toResponseDto(Task task);
}