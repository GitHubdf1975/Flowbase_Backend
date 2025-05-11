package com.example.flowbase_backend.controller;

import com.example.flowbase_backend.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StompTriggerController {

    private final NodeService nodeService;

    @Autowired
    public StompTriggerController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    /**
     * Fängt STOMP-SEND an /app/trigger/nodeA ab und aktiviert Node A.
     */
    @MessageMapping("/trigger/nodeA")
    public void handleTriggerNodeA() {
        nodeService.activateNodeA();
    }
}
