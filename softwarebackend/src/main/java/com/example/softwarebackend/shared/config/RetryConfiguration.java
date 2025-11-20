package com.example.softwarebackend.shared.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class RetryConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RetryConfiguration.class);

    @Bean
    public RetryRegistry retryRegistry() {
        // Create custom retry configuration with 10-second delay
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3) // Maximum 3 attempts (1 initial + 2 retries)
                .waitDuration(Duration.ofSeconds(10)) // 10-second delay between attempts
                .retryOnException(throwable -> {
                    // Retry on these specific exceptions
                    return throwable instanceof RuntimeException ||
                           throwable instanceof TimeoutException ||
                           throwable instanceof java.io.IOException ||
                           throwable instanceof java.net.UnknownHostException ||
                           throwable.getClass().getName().contains("GenAiIOException");
                })
                .ignoreExceptions(IllegalArgumentException.class) // Don't retry on validation errors
                .build();

        // Create registry with custom config
        RetryRegistry registry = RetryRegistry.of(retryConfig);

        // Configure specific retry instance for Gemini service
        configureGeminiServiceRetry(registry);

        return registry;
    }

    @Bean
    public Retry geminiServiceRetry(RetryRegistry retryRegistry) {
        return retryRegistry.retry("geminiService");
    }

    private void configureGeminiServiceRetry(RetryRegistry registry) {
        Retry geminiRetry = registry.retry("geminiService");

        // Add comprehensive event listeners
        geminiRetry.getEventPublisher()
                .onRetry(event -> {
                    logger.warn("🔄 Retry attempt {} for Gemini service operation '{}' - Last exception: {}",
                            event.getNumberOfRetryAttempts(),
                            event.getName(),
                            event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : "Unknown error");

                    logger.info("⏳ Waiting 10 seconds before next retry attempt...");
                })
                .onSuccess(event -> {
                    if (event.getNumberOfRetryAttempts() > 0) {
                        logger.info("✅ Gemini service operation '{}' succeeded after {} retry attempts",
                                event.getName(), event.getNumberOfRetryAttempts());
                    }
                })
                .onError(event -> {
                    logger.error("❌ Gemini service operation '{}' failed permanently after {} attempts. Final exception: {}",
                            event.getName(),
                            event.getNumberOfRetryAttempts(),
                            event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : "Unknown error");
                })
                .onIgnoredError(event -> {
                    logger.info("⚠️ Gemini service operation '{}' failed with ignored exception (no retry): {}",
                            event.getName(),
                            event.getLastThrowable() != null ? event.getLastThrowable().getMessage() : "Unknown error");
                });
    }
}
