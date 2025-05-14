<script setup>
import { onMounted, onUnmounted } from 'vue';
import { useWebSocket } from './useWebSocket';

// Use the WebSocket composable
const { nodes, connectWebSocket, disconnectWebSocket } = useWebSocket();

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
