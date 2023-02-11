#!/bin/sh
cd /home/asineth/xikara
pkill -9 adb
pkill -9 xikara
adb start-server
adb reverse tcp:3000 tcp:3000
./xikara >lastest.log 2>&1 &
