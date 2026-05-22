package com.example.todolist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_attachments")
@EntityListeners(AuditingEntityListener.class)
public class TaskAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "stored_file_name", nullable = false)
  private String storedFileName;

  @Column(name = "content_type")
  private String contentType;

  @Column(name = "size_bytes", nullable = false)
  private long size;

  @CreatedDate
  @Column(name = "uploaded_at", nullable = false, updatable = false)
  private LocalDateTime uploadedAt;

  public TaskAttachment() {
  }

  public TaskAttachment(Long id, Long taskId, String fileName, String storedFileName,
                        String contentType, long size, LocalDateTime uploadedAt) {
    this.id = id;
    setTaskId(taskId);
    this.fileName = fileName;
    this.storedFileName = storedFileName;
    this.contentType = contentType;
    this.size = size;
    this.uploadedAt = uploadedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public Long getTaskId() {
    return task != null ? task.getId() : null;
  }

  public void setTaskId(Long taskId) {
    if (taskId == null) {
      this.task = null;
      return;
    }

    Task task = new Task();
    task.setId(taskId);
    this.task = task;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getStoredFileName() {
    return storedFileName;
  }

  public void setStoredFileName(String storedFileName) {
    this.storedFileName = storedFileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }
}