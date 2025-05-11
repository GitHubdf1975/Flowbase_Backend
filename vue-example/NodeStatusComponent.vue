<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { Client } from '@stomp/stompjs';

// State for node statuses
const nodes = ref({
  'node-A': 'INACTIVE',
  'node-B': 'INACTIVE'
});

// STOMP client reference
let stompClient = null;

// Function to update node state
const updateNodeState = (nodeId, state) => {
  nodes.value[nodeId] = state;
  console.log(`Node ${nodeId} state updated to ${state}`);
};

// Connect to WebSocket
const connectWebSocket = () => {
  // Create STOMP client
  stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    debug: function (str) {
      console.log('STOMP: ' + str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000
  });

  // Connect handler
  stompClient.onConnect = (frame) => {
    console.log('Connected to WebSocket');

    // Subscribe to node activation topic
    stompClient.subscribe('/topic/node-activated', (message) => {
      const data = JSON.parse(message.body);
      updateNodeState(data.nodeId, data.state);
    });
  };

  // Error handler
  stompClient.onStompError = (frame) => {
    console.error('STOMP error', frame.headers['message']);
    console.error('Additional details', frame.body);
  };

  // Start connection
  stompClient.activate();
};

// Disconnect from WebSocket
const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
    console.log('Disconnected from WebSocket');
  }
};

// Connect when component is mounted
onMounted(() => {
  connectWebSocket();
});

// Disconnect when component is unmounted
onUnmounted(() => {
  disconnectWebSocket();
});
</script>

<template>
  <div class="node-status">
    <h2>Node Status</h2>
    <div class="node-list">
      <div v-for="(state, nodeId) in nodes" :key="nodeId" class="node-item">
        <div class="node-id">{{ nodeId }}</div>
        <div class="node-state" :class="{ 'running': state === 'RUNNING', 'done': state === 'DONE' }">
          {{ state }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.node-status {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  max-width: 500px;
  margin: 0 auto;
}

.node-list {
  margin-top: 20px;
}

.node-item {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.node-state {
  font-weight: bold;
  color: #999;
}

.node-state.running {
  color: #4caf50;
}

.node-state.done {
  color: #2196f3;
}
</style>
