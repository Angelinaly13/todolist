package com.example.todolist.model;

import com.example.todolist.model.converter.TagsConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private boolean completed;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Priority priority;

  @Convert(converter = TagsConverter.class)
  @Column(columnDefinition = "TEXT")
  private Set<String> tags = new HashSet<>();

  @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<TaskAttachment> attachments = new HashSet<>();

  public enum Priority {
    LOW, MEDIUM, HIGH
  }

  public Task() {
  }

  public Task(Long id, String title, String description, boolean completed,
              LocalDateTime createdAt, LocalDate dueDate, Priority priority, Set<String> tags) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
    this.createdAt = createdAt;
    this.dueDate = dueDate;
    this.priority = priority;
    this.tags = tags != null ? tags : new HashSet<>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags != null ? tags : new HashSet<>();
  }

  public Set<TaskAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(Set<TaskAttachment> attachments) {
    this.attachments = attachments != null ? attachments : new HashSet<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task task)) return false;
    return completed == task.completed &&
            Objects.equals(id, task.id) &&
            Objects.equals(title, task.title) &&
            Objects.equals(description, task.description) &&
            Objects.equals(createdAt, task.createdAt) &&
            Objects.equals(updatedAt, task.updatedAt) &&
            Objects.equals(dueDate, task.dueDate) &&
            priority == task.priority &&
            Objects.equals(tags, task.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, completed, createdAt, updatedAt, dueDate, priority, tags);
  }

  @Override
  public String toString() {
    return "Task{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", completed=" + completed +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", dueDate=" + dueDate +
            ", priority=" + priority +
            ", tags=" + tags +
            '}';
  }
}