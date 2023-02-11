#!/bin/bash
test ! -f /usr/bin/go && \
    sudo apt update && \
    sudo apt install -y golang-go gcc g++ make libc-dev
GOOS=linux GOARCH=arm64 go build -ldflags '-s -w'
scp xikara *.sh asineth@10.10.27.100:xikara
