package com.example.softwarebackend.kafka;

import com.example.softwarebackend.dto.CodeSubmission;
import com.example.softwarebackend.service.GeminiService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer that receives submissions and invokes grading.
 * On unrecoverable failures, it forwards message to a DLQ topic.
 */
@Component
public class SubmissionConsumer {
    private  static final Logger logger = LoggerFactory.getLogger(SubmissionConsumer.class);
    private final KafkaTemplate<String, CodeSubmission> kafkaTemplate;
    private final String dlqTopic;
    private final GeminiService geminiService;

    public SubmissionConsumer(GeminiService geminiService,
                              KafkaTemplate<String, CodeSubmission> kafkaTemplate,
                              @Value("${app.kafka.dlq-topic}") String dlqTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.dlqTopic = dlqTopic;
        this.geminiService = geminiService;
    }

    @KafkaListener(
            topics = "${app.kafka.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, CodeSubmission> record,
                       Acknowledgment acknowledgment) {

        CodeSubmission submission = record.value();
        String key = record.key();

        try {
            logger.info("Received code submission: {}, Execute by thread : {}", submission,Thread.currentThread().getName());

            // Process grading
            double score = geminiService.gradeTheCode(submission);

            // ✅ commit offset manually after successful processing
            acknowledgment.acknowledge();

        } catch (Exception ex) {
            logger.error("Processing failed for key={}, forwarding to DLQ. Error: {}",
                    key, ex.getMessage(), ex);

            // ✅ Forward to DLQ
            kafkaTemplate.send(dlqTopic, key, submission);

            // Optionally DO NOT acknowledge to retry later
            // acknowledgment.nack(Duration.ofSeconds(10)); // requires Spring Kafka 2.3+
        }
    }

}
