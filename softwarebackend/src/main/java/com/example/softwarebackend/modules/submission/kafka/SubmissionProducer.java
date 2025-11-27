package com.example.softwarebackend.modules.submission.kafka;

import com.example.softwarebackend.modules.submission.dto.SubmissionPendingRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Responsible for publishing CodeSubmission messages to Kafka.
 * Uses async send and logs success/failure. In production, attach monitoring/metrics here.
 */
@Component
public class SubmissionProducer {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionProducer.class);
    private final KafkaTemplate<String, SubmissionPendingRequestDTO> kafkaTemplate;
    private final String topic;

    public SubmissionProducer(KafkaTemplate<String, SubmissionPendingRequestDTO> kafkaTemplate,
                              @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * Publish submission asynchronously. Key is studentId (so messages for one student go to same partition).
     */
    public void publish(SubmissionPendingRequestDTO submission) {
        String key = submission.getSubmissionId() != null ? submission.getSubmissionId() : "unknown";
        kafkaTemplate.send(topic, key, submission)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        // log success, monitor offsets
                        logger.info("Published submission for student={}, partition={}, offset={}",
                                key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        // publish failed — in production consider retrying or writing to external store
                        logger.error("Failed to publish submission for student={}: {}", key, ex.getMessage(), ex);
                    }
                });
    }
}
