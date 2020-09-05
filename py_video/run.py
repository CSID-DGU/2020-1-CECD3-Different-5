import os
import cv2
import dlib
import numpy as np
from eye_slope import EyeandSlope

webcam = cv2.VideoCapture(0)

while True:
    ret, frame = webcam.read()

    eyeAndslope = EyeandSlope(frame)
    frame = eyeAndslope._analyze()
    
    cv2.imshow("Frame", frame)
    
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

webcam.release()
cv2.destroyAllWindows()
