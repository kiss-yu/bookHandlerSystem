package com.nix.cinema.aop;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Component
public interface ControllerLog {
    void before(JoinPoint joinPoint);
    void after(Object returnObject);
}
