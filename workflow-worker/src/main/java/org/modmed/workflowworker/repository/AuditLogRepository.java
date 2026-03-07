package org.modmed.workflowworker.repository;

import org.modmed.workflowworker.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    // Custom query methods can be added here if needed
}
