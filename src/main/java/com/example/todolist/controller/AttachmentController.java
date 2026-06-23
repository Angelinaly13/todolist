package com.example.todolist.controller;

import com.example.todolist.dto.AttachmentResponseDto;
import com.example.todolist.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttachmentController {

  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @PostMapping("/tasks/{taskId}/attachments")
  @ResponseStatus(HttpStatus.CREATED)
  public AttachmentResponseDto uploadAttachment(@PathVariable Long taskId,
                                                @RequestParam("file") MultipartFile file) {
    return attachmentService.storeAttachment(taskId, file);
  }

  @GetMapping("/tasks/{taskId}/attachments")
  public List<AttachmentResponseDto> getAttachments(@PathVariable Long taskId) {
    return attachmentService.getAttachmentsByTaskId(taskId);
  }

  @GetMapping("/attachments/{attachmentId}")
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
    Resource resource = attachmentService.loadAsResource(attachmentId);
    String fileName = attachmentService.getAttachment(attachmentId).getFileName();
    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
            .body(resource);
  }

  @DeleteMapping("/attachments/{attachmentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAttachment(@PathVariable Long attachmentId) {
    attachmentService.deleteAttachment(attachmentId);
  }
}