#!/bin/bash
test ! -f /usr/bin/go && \
    sudo apt update && \
    sudo apt install -y golang-go gcc g++ make libc-dev
GOOS=linux GOARCH=arm64 go build -ldflags '-s -w'
scp xikara *.sh *.service asineth@10.10.27.100:xikara
ssh asineth@10.10.27.100 systemctl enable xikara
ssh asineth@10.10.27.100 systemctl restart xikara
