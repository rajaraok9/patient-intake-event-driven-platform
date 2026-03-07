package org.modmed.workflowworker.controller;

import org.modmed.workflowworker.model.AuditLog;
import org.modmed.workflowworker.model.IntakeRequest;
import org.modmed.workflowworker.model.ProviderTask;
import org.modmed.workflowworker.repository.AuditLogRepository;
import org.modmed.workflowworker.repository.IntakeRepository;
import org.modmed.workflowworker.repository.ProviderTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

// Placeholder imports for IntakeRepository and IntakeRequest
// import org.modmed.workflowworker.repository.IntakeRepository;
// import org.modmed.workflowworker.model.IntakeRequest;

@Service
public class Workflowprocessor {
    private static final Logger log = LoggerFactory.getLogger(Workflowprocessor.class);

    private final IntakeRepository intakeRepository;
    private final ProviderTaskRepository taskRepository;
    private final AuditLogRepository auditRepository;
    private final RestTemplate restTemplate;

    public Workflowprocessor(IntakeRepository intakeRepository,
                             ProviderTaskRepository taskRepository,
                             AuditLogRepository auditRepository,
                             RestTemplate restTemplate) {
        this.intakeRepository = intakeRepository;
        this.taskRepository = taskRepository;
        this.auditRepository = auditRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void process(String messageBody) {
        String requestId = extractRequestId(messageBody);
        IntakeRequest intake = intakeRepository.findById(requestId)
                .orElseThrow();
        if (!"SUBMITTED".equals(intake.getStatus())) {
            log.info("Request already processed: {}", requestId);
            return; // idempotency protection
        }
        intake.setStatus("PROCESSING");
        intakeRepository.save(intake);
        // Capacity rule
        long openTasks = taskRepository.countByProviderIdAndTaskStatus(
                intake.getProviderId(), "OPEN");
        if (openTasks >= 5) {
            intake.setStatus("ON_HOLD");
            intakeRepository.save(intake);
            auditRepository.save(new AuditLog(
                    UUID.randomUUID().toString(),
                    requestId,
                    "PROVIDER_OVER_CAPACITY",
                    "SYSTEM",
                    LocalDateTime.now()
            ));
            return;
        }
        // Create Provider Task
        ProviderTask task = new ProviderTask(
                UUID.randomUUID().toString(),
                requestId,
                intake.getProviderId(),
                "OPEN",
                LocalDateTime.now()
        );
        taskRepository.save(task);
        // External Notification Call (Real HTTP call)
        restTemplate.postForObject(
                "https://httpbin.org/post",
                Map.of("requestId", requestId),
                String.class
        );
        intake.setStatus("AWAITING_PROVIDER_ACTION");
        intakeRepository.save(intake);
        auditRepository.save(new AuditLog(
                UUID.randomUUID().toString(),
                requestId,
                "TASK_CREATED",
                "SYSTEM",
                LocalDateTime.now()
        ));
        log.info("Workflow completed for {}", requestId);
    }

    private String extractRequestId(String json) {
        return json.split("\"requestId\":")[1]
                .split("\"")[1];
    }
}
