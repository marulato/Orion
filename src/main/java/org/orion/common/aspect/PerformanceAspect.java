package org.orion.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceAspect {

    @Around("execution(protected void executeInternal(*) throws *)")
    public void timeCostTracker(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("切入成功 头部");
        joinPoint.proceed();
        System.out.println("切入尾部");
    }

}
