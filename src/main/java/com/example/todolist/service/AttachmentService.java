package com.example.todolist.service;

import com.example.todolist.dto.AttachmentResponseDto;
import com.example.todolist.model.TaskAttachment;
import com.example.todolist.repository.TaskAttachmentRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final Path uploadPath;

  public AttachmentService(TaskAttachmentRepository attachmentRepository) {
    this.attachmentRepository = attachmentRepository;
    this.uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");
    try {
      Files.createDirectories(uploadPath);
    } catch (IOException e) {
      throw new RuntimeException("Could not create upload directory", e);
    }
  }

  public AttachmentResponseDto storeAttachment(Long taskId, MultipartFile file) {
    try {
      String originalName = file.getOriginalFilename();
      String extension = FilenameUtils.getExtension(originalName);
      String storedName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

      Path targetPath = uploadPath.resolve(storedName);
      file.transferTo(targetPath.toFile());

      TaskAttachment attachment = new TaskAttachment();
      attachment.setTaskId(taskId);
      attachment.setFileName(originalName);
      attachment.setStoredFileName(storedName);
      attachment.setContentType(file.getContentType());
      attachment.setSize(file.getSize());
      attachment.setUploadedAt(LocalDateTime.now());

      TaskAttachment saved = attachmentRepository.save(attachment);

      return new AttachmentResponseDto(saved.getId(), saved.getFileName(), saved.getSize(), saved.getUploadedAt());
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  public List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
    return attachmentRepository.findByTask_Id(taskId).stream()
            .map(a -> new AttachmentResponseDto(a.getId(), a.getFileName(), a.getSize(), a.getUploadedAt()))
            .collect(Collectors.toList());
  }

  public Resource loadAsResource(Long attachmentId) {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found"));

    try {
      Path filePath = uploadPath.resolve(attachment.getStoredFileName());
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists() && resource.isReadable()) {
        return resource;
      }
      throw new RuntimeException("Could not read file");
    } catch (IOException e) {
      throw new RuntimeException("Failed to load file", e);
    }
  }

  public TaskAttachment getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found"));
  }

  public void deleteAttachment(Long attachmentId) {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found"));

    try {
      Path filePath = uploadPath.resolve(attachment.getStoredFileName());
      Files.deleteIfExists(filePath);
      attachmentRepository.deleteById(attachmentId);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete file", e);
    }
  }
}