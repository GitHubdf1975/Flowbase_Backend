package com.example.flowbase_backend.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Git repository responses
 */
public class GitRepositoryResponse {
    private String status;
    private String message;
    private Map<String, Object> data;

    // Default constructor
    public GitRepositoryResponse() {
        this.data = new HashMap<>();
    }

    // Constructor with status and message
    public GitRepositoryResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = new HashMap<>();
    }

    // Static factory methods for common responses
    public static GitRepositoryResponse success(String message) {
        return new GitRepositoryResponse("success", message);
    }

    public static GitRepositoryResponse error(String message) {
        return new GitRepositoryResponse("error", message);
    }

    // Add data to the response
    public GitRepositoryResponse addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}