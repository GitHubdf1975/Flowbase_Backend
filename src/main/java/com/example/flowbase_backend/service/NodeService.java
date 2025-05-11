package com.example.flowbase_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.time.Instant;
import java.time.Duration;

@Service
public class NodeService {

    private final Map<String, Boolean> nodeStatus = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Instant> runningNodes = new ConcurrentHashMap<>();
    private static final String NODE_A = "nodeA";
    private static final String NODE_B = "nodeB";
    private static final Duration NODE_RUNNING_DURATION = Duration.ofSeconds(5); // Time a node stays in RUNNING state

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
            // Send WebSocket message and record running timestamp
            webSocketService.sendNodeStateUpdate("node-A", "RUNNING");
            runningNodes.put("node-A", Instant.now());
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
            // Send WebSocket message and record running timestamp
            webSocketService.sendNodeStateUpdate("node-B", "RUNNING");
            runningNodes.put("node-B", Instant.now());
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

    /**
     * Activates Node A immediately without delay
     */
    public void activateNodeAImmediately() {
        // Activate Node A
        nodeStatus.put(NODE_A, true);
        System.out.println("Node A has been activated immediately");

        // Record the current time
        Instant startTime = Instant.now();
        System.out.println("[DEBUG] Adding node-A to runningNodes map with timestamp: " + startTime);

        // Send WebSocket message with state RUNNING to match frontend expectations and record running timestamp
        webSocketService.sendNodeStateUpdate("node-A", "RUNNING");
        runningNodes.put("node-A", startTime);

        System.out.println("[DEBUG] Current runningNodes map after activation: " + runningNodes);
    }

    /**
     * Scheduled task to check for nodes that should transition from RUNNING to DONE
     */
    @Scheduled(fixedRate = 1000) // Check every second
    public void checkForNodeStateTransitions() {
        Instant now = Instant.now();

        // Debug: Log the current running nodes
        if (!runningNodes.isEmpty()) {
            System.out.println("[DEBUG] Currently running nodes: " + runningNodes.size());
            runningNodes.forEach((nodeId, startTime) -> {
                Duration runningTime = Duration.between(startTime, now);
                System.out.println("[DEBUG] Node " + nodeId + " running for " + runningTime.getSeconds() + " seconds (threshold: " + NODE_RUNNING_DURATION.getSeconds() + " seconds)");
            });
        }

        // Check each running node
        runningNodes.forEach((nodeId, startTime) -> {
            // If the node has been running for longer than NODE_RUNNING_DURATION
            Duration runningTime = Duration.between(startTime, now);
            if (runningTime.compareTo(NODE_RUNNING_DURATION) > 0) {
                // Transition to DONE state
                System.out.println("[DEBUG] Transitioning node " + nodeId + " to DONE state after running for " + runningTime.getSeconds() + " seconds");
                webSocketService.sendNodeStateUpdate(nodeId, "DONE");
                System.out.println("Node " + nodeId + " state transitioned from RUNNING to DONE");
                // Remove from running nodes
                runningNodes.remove(nodeId);
            }
        });
    }

    /**
     * Get the current state of a node for testing purposes
     * @param nodeId the ID of the node
     * @return the current state of the node (INACTIVE, RUNNING, or DONE)
     */
    public String getNodeStateForTesting(String nodeId) {
        // If the node is in the runningNodes map, it's in RUNNING state
        if (runningNodes.containsKey(nodeId)) {
            Instant startTime = runningNodes.get(nodeId);
            Duration runningTime = Duration.between(startTime, Instant.now());
            System.out.println("[DEBUG] Node " + nodeId + " is in RUNNING state for " + runningTime.getSeconds() + " seconds");
            return "RUNNING";
        }

        // If the node is active but not in runningNodes, it's in DONE state
        String internalNodeId = nodeId.equals("node-A") ? NODE_A : NODE_B;
        if (nodeStatus.getOrDefault(internalNodeId, false)) {
            System.out.println("[DEBUG] Node " + nodeId + " is in DONE state");
            return "DONE";
        }

        // Otherwise, it's inactive
        System.out.println("[DEBUG] Node " + nodeId + " is in INACTIVE state");
        return "INACTIVE";
    }
}
