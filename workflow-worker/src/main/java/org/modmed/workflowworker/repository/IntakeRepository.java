package org.modmed.workflowworker.repository;

import org.modmed.workflowworker.model.IntakeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository
        extends JpaRepository<IntakeRequest, String> {
}