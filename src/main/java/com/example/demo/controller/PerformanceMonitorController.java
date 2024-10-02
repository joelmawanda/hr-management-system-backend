//Performance and Admin Interface

package com.example.demo.controller;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@RestController
public class PerformanceMonitorController {

    private AtomicInteger totalRequests = new AtomicInteger(0);
    private AtomicInteger failedRequests = new AtomicInteger(0);

    @Before("execution(* com.example.api.*.*(..))")
    public void trackRequest(JoinPoint joinPoint) {
        totalRequests.incrementAndGet();
    }

    @AfterThrowing(pointcut = "execution(* com.example.api.*.*(..))", throwing = "ex")
    public void trackFailedRequest(Exception ex) {
        failedRequests.incrementAndGet();
    }

    @GetMapping("/admin/metrics")
    public ResponseEntity<?> getMetrics() {
        Map<String, Integer> metrics = new HashMap<>();
        metrics.put("Total Requests", totalRequests.get());
        metrics.put("Failed Requests", failedRequests.get());
        return ResponseEntity.ok(metrics);
    }
}

