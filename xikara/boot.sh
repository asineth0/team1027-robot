#!/bin/bash
pkill -9 xikara
pkill -9 adb
adb start-server
adb reverse tcp:3000 tcp:3000
./xikara
