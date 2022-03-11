import numpy as np
import cv2
import imutils
import time
from imutils.video import VideoStream

stream = VideoStream(src=1).start()
time.sleep(1)

while True:
    img = stream.read()
    # img = cv2.imread("test.png")
    img = cv2.resize(img, (1280, 730))
    img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    img_mask = cv2.inRange(img, (40, 90, 100), (100, 255, 255))
    img_mask = cv2.erode(img_mask, None, iterations=2)
    img_mask = cv2.dilate(img_mask, None, iterations=2)
    img = cv2.add(img, img, mask=img_mask)
    img = cv2.cvtColor(img, cv2.COLOR_HSV2BGR)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    img = cv2.Canny(img, 35, 125)

    fp_x = 0
    lp_x = 0

    for y in range(len(img)):
        y_val = img[y]
        for x in range(len(y_val)):
            x_val = img[y][x]
            if x_val != 0:
                lp_x = x
                if fp_x == 0:
                    fp_x = x

    w = lp_x-fp_x
    d = (160.2 * 4.2)/(0.0028*w) if w != 0 else 0
    print(f"fp_x={fp_x} lp_x={lp_x} w={w} d={d}")
