package com.example.flowbase_backend.model;

/**
 * Model class for node state messages sent via WebSocket
 */
public class NodeStateMessage {
    private String nodeId;
    private String state;

    // Default constructor required for JSON deserialization
    public NodeStateMessage() {
    }

    public NodeStateMessage(String nodeId, String state) {
        this.nodeId = nodeId;
        this.state = state;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}