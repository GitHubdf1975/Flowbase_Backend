# FlowBase Backend with WebSocket Support

This Spring Boot application provides REST endpoints for node activation and WebSocket support for real-time node state updates.

## Features

- REST API for triggering node activation
- WebSocket support for real-time node state updates
- Vue 3 example component for WebSocket integration

## Backend Setup

### REST Endpoints

- `GET /trigger/nodeA`: Triggers Node A activation after 10 seconds
- `GET /trigger/nodeB`: Checks for Resources/hello.txt every 5 seconds and activates Node B when found
- `GET /node-status`: Returns the current status of all nodes

### WebSocket Configuration

- WebSocket endpoint: `/ws`
- Topic for node activation: `/topic/node-activated`
- Message format:
  ```json
  {
    "nodeId": "node-A",
    "state": "RUNNING"
  }
  ```

## Frontend Integration

### Dependencies

Add the STOMP.js client to your Vue project:

```bash
npm install @stomp/stompjs
```

### Usage

1. Import the Vue component from `vue-example/NodeStatusComponent.vue`
2. The component will automatically connect to the WebSocket endpoint when mounted
3. It subscribes to the `/topic/node-activated` topic and updates the UI when messages are received

### Example

```vue
<template>
  <div>
    <NodeStatusComponent />
    <button @click="triggerNodeA">Trigger Node A</button>
    <button @click="triggerNodeB">Trigger Node B</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import NodeStatusComponent from './NodeStatusComponent.vue';

const triggerNodeA = async () => {
  await fetch('http://localhost:8080/trigger/nodeA');
};

const triggerNodeB = async () => {
  await fetch('http://localhost:8080/trigger/nodeB');
};
</script>
```

## How It Works

1. When a node is activated (either through the REST API or scheduled tasks), the backend sends a WebSocket message to the `/topic/node-activated` topic
2. Connected clients receive the message and update their UI accordingly
3. This provides real-time updates without the need for polling

## Development

### Backend

1. Make sure you have Java 17 installed
2. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend

1. Set up a Vue 3 project
2. Install the required dependencies
3. Import and use the provided Vue component
4. Connect to the WebSocket endpoint at `ws://localhost:8080/ws`