import { WebSocketServer } from "ws";

const wss = new WebSocketServer({
  port: 3001,
  path: "/api/ws",
});

wss.on("connection", (ws) => {
  ws.on("message", (msg) => {
    wss.clients.forEach((ws2) => {
      ws2.send(msg.toString());
    });
  });
});
