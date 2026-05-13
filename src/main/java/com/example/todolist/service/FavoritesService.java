package com.example.todolist.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FavoritesService {

  private static final String SESSION_FAVORITES_KEY = "favoriteTaskIds";

  @SuppressWarnings("unchecked")
  public Set<Long> getFavorites(HttpSession session) {
    Set<Long> favorites = (Set<Long>) session.getAttribute(SESSION_FAVORITES_KEY);
    if (favorites == null) {
      favorites = ConcurrentHashMap.newKeySet();
      session.setAttribute(SESSION_FAVORITES_KEY, favorites);
    }
    return favorites;
  }

  public void addFavorite(HttpSession session, Long taskId) {
    getFavorites(session).add(taskId);
  }

  public void removeFavorite(HttpSession session, Long taskId) {
    getFavorites(session).remove(taskId);
  }

  public boolean isFavorite(HttpSession session, Long taskId) {
    return getFavorites(session).contains(taskId);
  }
}