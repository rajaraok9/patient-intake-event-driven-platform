package org.modmed.workflowworker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_task")
public class ProviderTask {
    @Id
    @Column(length = 36, nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String requestId;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String taskStatus; // OPEN, ASSIGNED, CLOSED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ProviderTask() {}

    public ProviderTask(String id, String requestId, String providerId, String taskStatus, LocalDateTime createdAt) {
        this.id = id;
        this.requestId = requestId;
        this.providerId = providerId;
        this.taskStatus = taskStatus;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getProviderId() {
        return providerId;
    }
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
