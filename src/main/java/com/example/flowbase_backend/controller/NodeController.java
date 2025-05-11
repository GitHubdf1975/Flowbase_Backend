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
}