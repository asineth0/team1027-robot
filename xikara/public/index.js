const init = () => {
  let s = 0;
  const ws = new WebSocket("ws://localhost:3000/ws");
  const wsSend = ws.send.bind(ws);
  ws.send = (val) => wsSend(JSON.stringify(val));
  ws.binaryType = "arraybuffer";

  const handler = (e) => {
    ws.send({
      s: s++,
      t: performance.now(),
      x: e.beta,
      y: e.gamma,
      z: e.alpha,
    });
  };

  ws.addEventListener("open", () => {
    addEventListener("deviceorientation", handler);
  });

  ws.addEventListener("close", async () => {
    removeEventListener("deviceorientation", handler);
    await new Promise((resolve) => setTimeout(resolve, 1000));
    init();
  });
};

init();
