package com.example.todolist.lifecycle;

import com.example.todolist.repository.TaskRepository;
import com.example.todolist.service.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      System.out.println(">>> BeanPostProcessor BEFORE init: " + beanName + " (" + bean.getClass().getSimpleName() + ")");
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || bean instanceof TaskRepository) {
      System.out.println(">>> BeanPostProcessor AFTER init: " + beanName + " (" + bean.getClass().getSimpleName() + ")");
    }
    return bean;
  }
}