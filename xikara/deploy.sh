#!/bin/bash
test ! -f /usr/bin/go && \
    sudo apt update && \
    sudo apt install -y golang-go gcc g++ make libc-dev
GOOS=linux GOARCH=arm64 go build -ldflags '-s -w'
ssh asineth@10.10.27.100 sudo xikara/stop.sh
scp xikara *.sh asineth@10.10.27.100:xikara
ssh asineth@10.10.27.100 sudo xikara/start.sh
