package com.example.softwarebackend.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfig {

    private static final Logger logger = LoggerFactory.getLogger(RetryConfig.class);

    @Bean
    public RetryRegistry retryRegistry() {
        RetryRegistry registry = RetryRegistry.ofDefaults();

        // Get your retry instance
        Retry retry = registry.retry("geminiService");

        // Add event listener
        retry.getEventPublisher()
                .onRetry(event -> logger.warn("Retry attempt {} for operation '{}', last exception: {}",
                        event.getNumberOfRetryAttempts(),
                        event.getName(),
                        event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : "none"))
                .onError(event -> logger.error("Retry failed with exception: {}", event.getName()));

        return registry;
    }
}
