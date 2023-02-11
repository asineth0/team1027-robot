package main

import (
	"embed"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"os/exec"

	"golang.org/x/net/websocket"
)

//go:embed public
var public embed.FS

var msgChan = make(chan string)

func handleWS(ws *websocket.Conn) {
	log.Printf("[xs] WS connect\n")

	var msg string
	for {
		err := websocket.Message.Receive(ws, &msg)

		if err == io.EOF {
			log.Printf("[xs] WS gone\n")
			return
		}

		if err != nil {
			panic(err)
		}

		msgChan <- msg
	}
}

func main() {
	log.Println("[xs] booting v2")
	log.Println("[xs] preparing to take over the world")

	go func() {
		http.Handle("/", http.FileServer(http.Dir("public")))
		http.Handle("/ws", websocket.Handler(handleWS))
		http.ListenAndServe(":3000", nil)
	}()

	go func() {
		server, _ := net.Listen("tcp", ":3100")
		for {
			conn, err := server.Accept()
			if err != nil {
				panic(err)
			}

			go func() {
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

	exec.Command("adb", "shell", "am force-stop com.android.chrome").Run()
	exec.Command("adb", "shell", "am start -a android.intent.action.VIEW -d http://localhost:3000").Run()

	select {}
}
