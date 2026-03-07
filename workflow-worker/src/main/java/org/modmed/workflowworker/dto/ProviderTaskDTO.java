package org.modmed.workflowworker.dto;

import java.time.LocalDateTime;

public class ProviderTaskDTO {
    private String id;
    private String requestId;
    private String providerId;
    private String taskStatus;
    private LocalDateTime createdAt;

    public ProviderTaskDTO() {}

    public ProviderTaskDTO(String id, String requestId, String providerId, String taskStatus, LocalDateTime createdAt) {
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
