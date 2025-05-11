package com.example.flowbase_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NodeService {

    private final Map<String, Boolean> nodeStatus = new ConcurrentHashMap<>();
    private static final String NODE_A = "nodeA";
    private static final String NODE_B = "nodeB";

    private final WebSocketService webSocketService;

    @Autowired
    public NodeService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        // Initialize nodes with inactive status
        nodeStatus.put(NODE_A, false);
        nodeStatus.put(NODE_B, false);
    }

    /**
     * Activates Node A after a delay
     */
    public void activateNodeA() {
        try {
            // Wait for 10 seconds
            Thread.sleep(10000);
            // Activate Node A
            nodeStatus.put(NODE_A, true);
            System.out.println("Node A has been activated");
            // Send WebSocket message
            webSocketService.sendNodeStateUpdate("node-A", "RUNNING");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error while activating Node A: " + e.getMessage());
        }
    }

    /**
     * Checks if hello.txt exists and activates Node B if it does
     */
    @Scheduled(fixedRate = 5000) // Check every 5 seconds
    public void checkForNodeBActivation() {
        File file = new File("Resources/hello.txt");
        if (file.exists() && !nodeStatus.get(NODE_B)) {
            nodeStatus.put(NODE_B, true);
            System.out.println("Node B has been activated after finding hello.txt");
            // Send WebSocket message
            webSocketService.sendNodeStateUpdate("node-B", "RUNNING");
        }
    }

    /**
     * Returns the current status of all nodes
     * @return Map containing node names and their status
     */
    public Map<String, Boolean> getAllNodeStatus() {
        return new HashMap<>(nodeStatus);
    }

    /**
     * Returns the status of a specific node
     * @param nodeName the name of the node
     * @return boolean indicating if the node is active
     */
    public boolean getNodeStatus(String nodeName) {
        return nodeStatus.getOrDefault(nodeName, false);
    }
}
