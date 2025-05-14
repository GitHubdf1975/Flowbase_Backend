import { ref } from 'vue';
import { Client } from '@stomp/stompjs';

export function useWebSocket() {
  // State for node statuses
  const nodes = ref({
    'node-A': 'INACTIVE',
    'node-B': 'INACTIVE'
  });

  // STOMP client reference
  let client = null;

  // Function to update node state
  const updateNodeState = (nodeId, state) => {
    nodes.value[nodeId] = state;
    console.log(`Node ${nodeId} state updated to ${state}`);
  };

  // Connect to WebSocket
  const connectWebSocket = () => {
    // Create STOMP client
    client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      debug: function (str) {
        console.log('STOMP: ' + str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    // Connect handler
    client.onConnect = (frame) => {
      console.log('Connected to WebSocket');
      
      // Ab hier einfügen ────────────────────────────────────────
      client.onReceive = frame => {
        console.group(`📥 STOMP Frame: ${frame.command}`);
        console.log('Headers:', frame.headers);
        let body = frame.body;
        if (!body && frame.binaryBody) {
          body = new TextDecoder('utf-8').decode(frame.binaryBody);
        }
        console.log('Body  :', body);
        console.groupEnd();
      };

      client.subscribe('/topic/node-activated', message => {
        console.group('🛰️ /topic/node-activated MESSAGE');
        console.log('Raw message:', message);
        const payload = message.body ?? new TextDecoder('utf-8').decode(message.binaryBody);
        console.log('Decoded payload:', payload);
        console.groupEnd();

        try {
          const { nodeId, state } = JSON.parse(payload);
          console.log(`→ updateNodeState("${nodeId}", "${state}")`);
          updateNodeState(nodeId, state);
        } catch (e) {
          console.error('❌ JSON.parse failed:', e);
        }
      });
      // ─────────────────────────────────────────────────────────
    };

    // Error handler
    client.onStompError = (frame) => {
      console.error('STOMP error', frame.headers['message']);
      console.error('Additional details', frame.body);
    };

    // Start connection
    client.activate();
  };

  // Disconnect from WebSocket
  const disconnectWebSocket = () => {
    if (client) {
      client.deactivate();
      console.log('Disconnected from WebSocket');
    }
  };

  return {
    nodes,
    connectWebSocket,
    disconnectWebSocket,
    updateNodeState
  };
}