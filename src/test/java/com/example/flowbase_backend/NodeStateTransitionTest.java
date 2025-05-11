package com.example.flowbase_backend;

import com.example.flowbase_backend.service.NodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for debugging node state transitions
 */
@SpringBootTest
public class NodeStateTransitionTest {

    @Autowired
    private NodeService nodeService;

    /**
     * Test to debug the node state transition from RUNNING to DONE
     * This test activates a node and then waits to observe the state transition
     */
    @Test
    public void testNodeStateTransition() throws InterruptedException {
        System.out.println("[DEBUG_LOG] Starting node state transition test");

        // Check initial state
        String initialState = nodeService.getNodeStateForTesting("node-A");
        System.out.println("[DEBUG_LOG] Initial state of node-A: " + initialState);
        // Initial state should be INACTIVE
        assertEquals("INACTIVE", initialState, "Initial state should be INACTIVE");

        // Activate Node A immediately
        System.out.println("[DEBUG_LOG] Activating Node A");
        nodeService.activateNodeAImmediately();

        // Check state right after activation
        String stateAfterActivation = nodeService.getNodeStateForTesting("node-A");
        System.out.println("[DEBUG_LOG] State of node-A after activation: " + stateAfterActivation);
        // State after activation should be RUNNING
        assertEquals("RUNNING", stateAfterActivation, "State after activation should be RUNNING");

        // Wait for the node to transition from RUNNING to DONE
        // The scheduled task runs every second and should transition the node after 5 seconds
        System.out.println("[DEBUG_LOG] Waiting for state transition (should take about 6 seconds)");

        // Check status every second for 7 seconds
        boolean transitionedToDone = false;
        int transitionSecond = -1;

        for (int i = 1; i <= 7; i++) {
            Thread.sleep(1000);
            String currentState = nodeService.getNodeStateForTesting("node-A");
            System.out.println("[DEBUG_LOG] Second " + i + " of waiting - node-A state: " + currentState);

            if ("DONE".equals(currentState) && !transitionedToDone) {
                transitionedToDone = true;
                transitionSecond = i;
                System.out.println("[DEBUG_LOG] Node has transitioned to DONE state after " + i + " seconds");
            }
        }

        // Final state check
        String finalState = nodeService.getNodeStateForTesting("node-A");
        System.out.println("[DEBUG_LOG] Final state of node-A: " + finalState);

        // Assert that the node transitioned to DONE state
        assertEquals("DONE", finalState, "Final state should be DONE");

        // Assert that the transition happened after approximately 5 seconds (NODE_RUNNING_DURATION)
        // Allow for some timing variation (5-7 seconds is acceptable)
        System.out.println("[DEBUG_LOG] Node transitioned to DONE after " + transitionSecond + " seconds");
        assertTrue(transitionSecond >= 5 && transitionSecond <= 7, 
                "Node should transition to DONE after 5-7 seconds, but took " + transitionSecond + " seconds");

        System.out.println("[DEBUG_LOG] Test completed - all assertions passed");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
