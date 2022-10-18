from time import sleep
import ffmpeg
import cv2
import numpy as np
import time
import math

time.sleep(1)  # prevent DC from shitting itself

frames = 0
capture = cv2.VideoCapture()
capture.set(cv2.CAP_PROP_BUFFERSIZE, 1)
capture.open("http://asineth-op8:4747/video")
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
faces = []
faces_scale = 0.5
faces_scale_w = math.floor(640 * faces_scale)
faces_scale_h = math.floor(480 * faces_scale)

while True:
    _, frame = capture.read()

    frames += 1
    start = time.time()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    gray = cv2.resize(gray, (faces_scale_w, faces_scale_h))
    faces = face_cascade.detectMultiScale(gray, 1.1, 4)

    for (x, y, w, h) in faces:
        x = math.floor((x / faces_scale_w) * 640)
        w = math.floor((w / faces_scale_w) * 640)
        y = math.floor((y / faces_scale_h) * 480)
        h = math.floor((h / faces_scale_h) * 480)
        cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 0, 0), 2)

    print(f"frame {frames} +{(time.time() - start)*1000}ms")
    cv2.imshow("frame", frame)

    key = cv2.waitKey(1)

    if key == ord("q"):
        break
