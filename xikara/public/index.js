const init = () => {
  let s = 0;
  const ws = new WebSocket("ws://localhost:8080/ws");
  const wsSend = ws.send.bind(ws);
  ws.send = (val) => wsSend(JSON.stringify(val));
  ws.binaryType = "arraybuffer";

  const handler = (e) => {
    e.gamma > 0 && (e.gamma = 90 - e.gamma);
    e.gamma < 0 && (e.gamma = -90 - e.gamma); // maths

    ws.send({
      s: s++,
      t: performance.now(),
      x: Math.round(e.beta * 1000) / 1000,
      y: Math.round(e.gamma * 1000) / 1000,
      z: Math.round(e.alpha * 1000) / 1000,
      _: 0, // makes our half-ass JSON parsing work
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
