package com.example.softwarebackend.config;

import com.example.softwarebackend.dto.CodeSubmission;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration: creates topics and configures Json (de)serializers.
 * Using Spring's default ProducerFactory/ConsumerFactory with JSON support.
 */
@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${app.kafka.topic}")
    private String submissionsTopic;

    @Value("${app.kafka.dlq-topic}")
    private String dlqTopic;

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic submissionsTopic() {
        // create topic with partitions and replication factor suited for production
        logger.info("Creating Kafka topic: {}", submissionsTopic);
        return new NewTopic(submissionsTopic, 3, (short)1);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic dlqTopic() {
        logger.info("Creating Kafka DLQ topic: {}", dlqTopic);
        return new NewTopic(dlqTopic, 3, (short)1);
    }

    // ProducerFactory + KafkaTemplate using JsonSerializer for values
    @Bean
    public ProducerFactory<String, CodeSubmission> producerFactory(org.springframework.core.env.Environment env) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        // production suggested: acks=all and retries
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, 3);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, 10);
        // Add timeout configurations for better error handling
        props.put(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 15000);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, CodeSubmission> kafkaTemplate(ProducerFactory<String, CodeSubmission> pf) {
        return new KafkaTemplate<>(pf);
    }

    // ConsumerFactory for CodeSubmission
    @Bean
    public ConsumerFactory<String, CodeSubmission> consumerFactory(Environment env) {
        JsonDeserializer<CodeSubmission> deserializer = new JsonDeserializer<>(CodeSubmission.class);
        deserializer.addTrustedPackages("com.example.softwarebackend.dto");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("spring.kafka.consumer.group-id"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    // Configure listener container factory for concurrency
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CodeSubmission> kafkaListenerContainerFactory(
            ConsumerFactory<String, CodeSubmission> cf) {
        ConcurrentKafkaListenerContainerFactory<String, CodeSubmission> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        // concurrency tuned from application.yml but can override programmatically
        factory.setConcurrency(6);
        // if you want at-least-once semantics, disable batch and use record Acks (default)
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }
}

