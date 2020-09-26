import os
import cv2
import dlib
import numpy as np
from eye_slope import EyeandSlope
from emotion import Emotion

webcam = cv2.VideoCapture(0)
es = EyeandSlope()
emotion = Emotion()

while True:
    ret, frame = webcam.read()

    canvas = np.zeros((250,300,3),dtype="uint8")

    frame = es._analyze(frame)
    frame, canvas= emotion._analyze(frame,canvas)

    cv2.imshow("Frame", frame)
    cv2.imshow("Proportion", canvas)
    
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

webcam.release()
cv2.destroyAllWindows()
