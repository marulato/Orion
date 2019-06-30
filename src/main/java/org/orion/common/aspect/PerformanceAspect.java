package org.orion.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.orion.common.miscutil.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceAspect {

    private final Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(protected void executeInternal(*) throws *)")
    public void timeCostTracker(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        logger.info("计划任务性能监控-启动: " + joinPoint.getTarget().getClass().getName() + " - " + DateUtil.getLogDate(DateUtil.getDate(startTime)));
        joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("结束: " + joinPoint.getTarget().getClass().getName() + " - " + DateUtil.getLogDate(DateUtil.getDate(endTime)));
        logger.info("耗时: " + joinPoint.getTarget().getClass().getName() + " - " + (endTime - startTime) + " ms");
    }

}
