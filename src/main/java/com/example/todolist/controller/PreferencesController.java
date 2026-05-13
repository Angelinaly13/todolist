package com.example.todolist.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

  private static final String VIEW_PREFERENCE_COOKIE = "viewPreference";

  @GetMapping("/view")
  public ResponseEntity<String> getViewPreference(@CookieValue(value = VIEW_PREFERENCE_COOKIE, defaultValue = "detailed") String viewPreference) {
    return ResponseEntity.ok(viewPreference);
  }

  @PostMapping("/view")
  public ResponseEntity<Void> setViewPreference(@RequestParam String mode, HttpServletResponse response) {
    if (!mode.equals("compact") && !mode.equals("detailed")) {
      return ResponseEntity.badRequest().build();
    }
    Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, mode);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 365); // 1 год
    response.addCookie(cookie);
    return ResponseEntity.noContent().build();
  }
}