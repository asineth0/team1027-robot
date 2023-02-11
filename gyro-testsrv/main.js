import "./style.css";
import * as THREE from "three";

const ws = new WebSocket(`${location.origin.replace("http", "ws")}/api/ws`);
const send = ws.send.bind(ws);
ws.send = (val) => send(JSON.stringify(val));

const initMobile = () => {
  let s = 0;
  addEventListener("deviceorientationabsolute", (e) => {
    ws.send({
      s: s++,
      t: performance.now(),
      x: e.beta,
      y: e.gamma,
      z: e.alpha,
    });
  });
};

const initDesktop = () => {
  const w = 640;
  const h = 480;
  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(75, w / h, 0.1, 1000);
  const renderer = new THREE.WebGL1Renderer({ antialias: true });
  renderer.setSize(w, h);
  document.body.appendChild(renderer.domElement);

  // const cubeSize = [160.2, 72.9, 8];
  const cubeSize = [72.9, 160.2, 8];
  const cubeScale = Math.max(...cubeSize);
  const cube = new THREE.Mesh(
    new THREE.BoxGeometry(...cubeSize.map((n) => n / cubeScale)),
    // new THREE.MeshBasicMaterial({ color: 0x00ff00 })
    new THREE.MeshNormalMaterial()
  );
  camera.position.z = 2;
  scene.add(cube);

  const render = () => {
    renderer.render(scene, camera);
    requestAnimationFrame(render);
  };
  render();

  const p = document.createElement("p");
  p.style.color = "#00ff00";
  p.style.fontSize = "2rem";
  document.body.appendChild(p);

  ws.onmessage = ({ data }) => {
    data = JSON.parse(data);
    data.x = Math.round(data.x * 1000) / 1000;
    data.y = Math.round(data.y * 1000) / 1000;
    data.z = Math.round(data.z * 1000) / 1000;
    cube.rotation.x = data.x * (Math.PI / 180);
    cube.rotation.y = data.y * (Math.PI / 180);
    cube.rotation.z = data.z * (Math.PI / 180);
    const _data = { y: data.y };
    if (_data.y > 0) {
      _data.y = 90 - _data.y;
    }
    if (_data.y < 0) {
      _data.y = -90 - _data.y;
    }
    p.innerText = JSON.stringify(_data, null, 2);
  };
};

ws.onopen = () => {
  console.log("WS open");

  if (navigator.userAgent.includes("Mobile")) {
    console.log("initMobile");
    initMobile();
  } else {
    console.log("initDesktop");
    initDesktop();
  }
};
