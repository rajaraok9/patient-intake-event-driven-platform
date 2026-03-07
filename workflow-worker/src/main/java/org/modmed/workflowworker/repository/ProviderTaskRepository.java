package org.modmed.workflowworker.repository;

import org.modmed.workflowworker.model.ProviderTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderTaskRepository extends JpaRepository<ProviderTask, String> {
    long countByProviderIdAndTaskStatus(String providerId, String taskStatus);
}
