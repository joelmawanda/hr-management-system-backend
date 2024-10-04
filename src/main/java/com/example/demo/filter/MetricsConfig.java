//package com.example.demo.filter;
//
//import io.micrometer.core.instrument.config.MeterFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MetricsConfig {
//    @Bean
//    public MeterFilter meterFilter() {
//        return MeterFilter.denyUnless(id -> {
//            // Only track metrics for the "/api/staff" URI
//            return id.getTag("uri") != null && id.getTag("uri").startsWith("/api/staff");
//        });
//    }
//}
