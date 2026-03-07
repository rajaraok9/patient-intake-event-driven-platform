package org.modmed.workflowworker.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private String id;
    private String requestId;
    private String action;
    private String actor;
    private LocalDateTime timestamp;

    public AuditLogDTO() {}

    public AuditLogDTO(String id, String requestId, String action, String actor, LocalDateTime timestamp) {
        this.id = id;
        this.requestId = requestId;
        this.action = action;
        this.actor = actor;
        this.timestamp = timestamp;
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
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
