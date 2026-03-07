package org.modmed.workflowworker.service;

import org.modmed.workflowworker.model.AuditLog;
import org.modmed.workflowworker.model.Patient;
import org.modmed.workflowworker.model.ProviderTask;
import org.modmed.workflowworker.repository.AuditLogRepository;
import org.modmed.workflowworker.repository.PatientRepository;
import org.modmed.workflowworker.repository.ProviderTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WorkflowOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(WorkflowOrchestrator.class);

    private final PatientRepository patientRepository;
    private final ProviderTaskRepository providerTaskRepository;
    private final AuditLogRepository auditLogRepository;

    public WorkflowOrchestrator(PatientRepository patientRepository,
                                ProviderTaskRepository providerTaskRepository,
                                AuditLogRepository auditLogRepository) {
        this.patientRepository = patientRepository;
        this.providerTaskRepository = providerTaskRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void process(String patientId) {

        log.info("Processing patient {}", patientId);

        Patient patient = patientRepository.findById(Long.valueOf(patientId))
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Create provider task
        ProviderTask task = new ProviderTask(
                UUID.randomUUID().toString(),
                patientId,
                "DEFAULT_PROVIDER",
                "OPEN",
                LocalDateTime.now()
        );

        providerTaskRepository.save(task);

        // Create audit log
        AuditLog audit = new AuditLog(
                UUID.randomUUID().toString(),
                patientId,
                "TASK_CREATED",
                "SYSTEM",
                LocalDateTime.now()
        );

        auditLogRepository.save(audit);

        log.info("Workflow completed for patient {}", patientId);
    }
}