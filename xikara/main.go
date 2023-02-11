package main

import (
	"embed"
	"fmt"
	"io"
	"io/fs"
	"log"
	"net"
	"net/http"
	"os/exec"
	"time"

	"golang.org/x/net/websocket"
)

//go:embed public
var publicFS embed.FS

var msgChan = make(chan string)
var msgLatest = ""

func handleWS(ws *websocket.Conn) {
	log.Println("[xs] WS connected")

	var msg string
	for {
		err := websocket.Message.Receive(ws, &msg)

		if err == io.EOF {
			log.Println("[xs] WS closed")
			return
		}

		if err != nil {
			panic(err)
		}

		msgLatest = msg
		msgChan <- msg
	}
}

func main() {
	log.Println("[xs] booting v2")
	log.Println("[xs] preparing to take over the world")

	go func() {
		publicFSSub, _ := fs.Sub(publicFS, "public")
		httpFSHandler := http.FileServer(http.FS(publicFSSub))
		http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
			w.Header().Set("cache-control", "public, max-age=0")
			httpFSHandler.ServeHTTP(w, r)
		})
		http.Handle("/ws", websocket.Handler(handleWS))
		http.ListenAndServe(":8080", nil)
	}()

	go func() {
		server, err := net.Listen("tcp", ":8081")
		if err != nil {
			panic(err)
		}

		for {
			conn, err := server.Accept()
			if err != nil {
				panic(err)
			}

			go func() {
				if len(msgLatest) > 0 {
					conn.Write([]byte(fmt.Sprintf("%s\n", msgLatest)))
				}

				for {
					msg := <-msgChan
					_, err := conn.Write([]byte(fmt.Sprintf("%s\n", msg)))

					if err != nil {
						conn.Close()
						break
					}
				}
			}()
		}
	}()

	for {
		exec.Command("adb", "start-server").Run()
		exec.Command("adb", "reverse", "tcp:8080", "tcp:8080").Run()
		exec.Command("adb", "shell", "input keyevent 224").Run()
		exec.Command("adb", "shell", "input keyevent 82").Run()
		exec.Command("adb", "shell", "am force-stop com.android.chrome").Run()
		exec.Command("adb", "shell", "am start -a android.intent.action.VIEW -d http://localhost:8080").Run()
		shell := exec.Command("adb", "shell")
		shell.StdinPipe()
		shell.StdoutPipe()
		shell.Start()
		shell.Wait()
		log.Println("[xs] ADB closed - retrying in 1s")
		time.Sleep(1 * time.Second)
	}
}
