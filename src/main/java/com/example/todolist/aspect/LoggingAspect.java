package com.example.todolist.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

  @Around("execution(* com.example.todolist.service.*.*(..))")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();

    System.out.println(">>> AROUND START: " + methodName + " with args: " + Arrays.toString(args));

    long start = System.currentTimeMillis();
    Object result;
    try {
      result = joinPoint.proceed();
      long elapsed = System.currentTimeMillis() - start;
      System.out.println(">>> AROUND END: " + methodName + " returned: " + result + " (took " + elapsed + " ms)");
    } catch (Exception e) {
      System.out.println(">>> AROUND EXCEPTION: " + methodName + " threw " + e.getMessage());
      throw e;
    }
    return result;
  }
}