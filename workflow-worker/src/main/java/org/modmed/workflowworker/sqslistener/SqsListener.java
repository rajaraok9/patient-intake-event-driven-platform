package org.modmed.workflowworker.sqslistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.modmed.workflowworker.event.PatientIntakeSubmittedEvent;
import org.modmed.workflowworker.service.WorkflowOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Component


public class SqsListener {
   Logger log = LoggerFactory.getLogger(SqsListener.class);
    private final SqsClient sqsClient;
    private final WorkflowOrchestrator orchestrator;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public SqsListener(SqsClient sqsClient, WorkflowOrchestrator orchestrator) {
        this.sqsClient = sqsClient;
        this.orchestrator = orchestrator;
    }

    @PostConstruct
    public void start() {
        new Thread(this::poll).start();
    }

    private void poll() {
        while (true) {
            log.info("Waiting for messages to be delivered...");

            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .waitTimeSeconds(10)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(request).messages();

            for (Message message : messages) {

                try {

                    PatientIntakeSubmittedEvent event =
                            objectMapper.readValue(message.body(), PatientIntakeSubmittedEvent.class);

                    String patientId = event.getPatientId();

                    log.info("Received intake event for patientId: {}", patientId);

                    orchestrator.process(patientId);

                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build());

                    log.info("Message processed and deleted");

                } catch (Exception e) {

                    log.error("Failed to process message", e);

                }
            }
        }
    }

    private String extractRequestId(String json) {
        return json.split("\"requestId\":")[1]
                .split("\"")[1];
    }
}