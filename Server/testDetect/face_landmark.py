# face_landmrk 찍고, 눈,코,입 삼각형 추출
# 필요 없음
import dlib
import cv2
import os
import numpy as np


webcam = cv2.VideoCapture(0)

cwd = os.path.abspath(os.path.dirname(__file__))
model_path = os.path.abspath(os.path.join(cwd, "trained_models/shape_predictor_68_face_landmarks.dat"))

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor(model_path)


while True:
    _, frame = webcam.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    faces = detector(gray)
    for face in faces:
        x1 = face.left()
        y1=face.top()
        x2=face.right()
        y2=face.bottom()

        # 얼굴 인식된 부분 사각형으로 표시
        #cv2.rectangle(frame, (x1,y1),(x2,y2),(0,255,0),3)

        landmarks = predictor(gray, face)
            
        for n in range(0,68):
            x = landmarks.part(n).x
            y = landmarks.part(n).y

            # face landmark 출력
            cv2.circle(frame,(x,y),3,(255,0,0),-1)

        # facial slope 측정을 위한 삼각형
        eye1_x=eye1_y=eye2_x=eye2_y=mouth_x=mouth_y=0
        for n in range(36,42):
            eye1_x += landmarks.part(n).x
            eye1_y += landmarks.part(n).y
            eye2_x += landmarks.part(n+6).x
            eye2_y += landmarks.part(n+6).y

        eye1_x /= 6
        eye1_y /= 6
        eye2_x /= 6
        eye2_y /= 6

        for n in range(48, 68):
            mouth_x += landmarks.part(n).x
            mouth_y += landmarks.part(n).y

        mouth_x /= 20
        mouth_y /= 20
            
        pts = np.array([[eye1_x,eye1_y],[eye2_x,eye2_y],[mouth_x,mouth_y]], np.int32)
        pts = pts.reshape((-1,1,2))
        cv2.polylines(frame, [pts], True, (255,0,0),3)

    cv2.imshow("Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

webcam.release()
cv2.destroyAllWindows()
