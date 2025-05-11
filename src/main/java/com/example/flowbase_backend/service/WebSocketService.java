package com.example.flowbase_backend.service;

import com.example.flowbase_backend.model.NodeStateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for sending WebSocket messages to clients
 */
@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Send a node state update message to the topic
     * @param nodeId The ID of the node
     * @param state The new state of the node
     */
    public void sendNodeStateUpdate(String nodeId, String state) {
        NodeStateMessage message = new NodeStateMessage(nodeId, state);
        System.out.println("[DEBUG] Preparing to send WebSocket message: nodeId=" + nodeId + ", state=" + state + " to topic /topic/node-activated");
        messagingTemplate.convertAndSend("/topic/node-activated", message);
        System.out.println("Sent WebSocket message: nodeId=" + nodeId + ", state=" + state);
    }
}
