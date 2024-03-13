package com.emzio.todo_api.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component

public class LogicAspect {
    private final Timer projectCreateGroupTimer;
    private final Logger logger = LoggerFactory.getLogger(LogicAspect.class);

    public LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Pointcut("execution(* com.emzio.todo_api.logic.ProjectService.createGroup(..))")
    void projectServiceCreateGroup(){
    }

    @Before("projectServiceCreateGroup()")
    void logMethodCall(JoinPoint joinPoint){
        logger.info("Before {} with {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @Around("projectServiceCreateGroup()")
    Object aroundGroupCreate(ProceedingJoinPoint jp){
        return projectCreateGroupTimer.record(() -> {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}
