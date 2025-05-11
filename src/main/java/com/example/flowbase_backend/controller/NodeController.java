package com.example.flowbase_backend.controller;

import com.example.flowbase_backend.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class NodeController {

    private final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * Endpoint to trigger Node A activation after 10 seconds
     * @return Response indicating the process has started
     */
    @GetMapping("/trigger/nodeA")
    public ResponseEntity<String> triggerNodeA() {
        // Use CompletableFuture to run the activation asynchronously
        CompletableFuture.runAsync(nodeService::activateNodeA);
        return ResponseEntity.ok("Node A activation process started. It will be activated after 10 seconds.");
    }

    /**
     * Endpoint to trigger Node B activation check
     * @return Response indicating the process has started
     */
    @GetMapping("/trigger/nodeB")
    public ResponseEntity<String> triggerNodeB() {
        // The scheduled task in NodeService will handle this
        return ResponseEntity.ok("Node B activation check started. It will be activated once Resources/hello.txt is found.");
    }

    /**
     * Endpoint to get the status of all nodes
     * @return Map containing node names and their status
     */
    @GetMapping("/node-status")
    public ResponseEntity<Map<String, Boolean>> getNodeStatus() {
        return ResponseEntity.ok(nodeService.getAllNodeStatus());
    }

    /**
     * Endpoint to immediately activate Node A (change from Idle to Running)
     * @return Response indicating the node has been activated
     */
    @GetMapping("/activate/nodeA")
    public ResponseEntity<String> activateNodeA() {
        nodeService.activateNodeAImmediately();
        return ResponseEntity.ok("Node A has been activated immediately.");
    }

    /**
     * Alternative endpoint to immediately activate Node A (change from Idle to Running)
     * This is an alternative to /activate/nodeA for clients that might have issues with that path
     * @return Response indicating the node has been activated
     */
    @GetMapping("/activate-node-a")
    public ResponseEntity<String> activateNodeAAlternative() {
        nodeService.activateNodeAImmediately();
        return ResponseEntity.ok("Node A has been activated immediately (alternative endpoint).");
    }

    /**
     * Endpoint to immediately activate Node A with trailing slash
     * This handles the case where the client adds a trailing slash to the URL
     * @return Response indicating the node has been activated
     */
    @GetMapping("/activate/nodeA/")
    public ResponseEntity<String> activateNodeAWithTrailingSlash() {
        nodeService.activateNodeAImmediately();
        return ResponseEntity.ok("Node A has been activated immediately (trailing slash endpoint).");
    }

    /**
     * Endpoint to immediately activate Node A with lowercase 'a'
     * This handles the case where the client uses lowercase 'a' in the URL
     * @return Response indicating the node has been activated
     */
    @GetMapping("/activate/nodea")
    public ResponseEntity<String> activateNodeALowercase() {
        nodeService.activateNodeAImmediately();
        return ResponseEntity.ok("Node A has been activated immediately (lowercase endpoint).");
    }
}
