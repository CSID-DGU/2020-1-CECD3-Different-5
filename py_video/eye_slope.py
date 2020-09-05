import dlib
import cv2
import os
import numpy as np
from gaze_tracking import GazeTracking

class EyeandSlope(object):

    def __init__(self):
        cwd = os.path.abspath(os.path.dirname(__file__))
        model_path = os.path.abspath(os.path.join(cwd, "trained_models/shape_predictor_68_face_landmarks.dat"))

        self.detector = dlib.get_frontal_face_detector()
        self.predictor = dlib.shape_predictor(model_path)

    def _gazeTracking(self,frame): 

        gaze = GazeTracking(self.detector, self.predictor)
        gaze.refresh(frame)

        frame = gaze.annotated_frame()
        text = ""

        if gaze.is_blinking():
            text = "Blinking"
        elif gaze.is_right():
            text = "Looking right"
        elif gaze.is_left():
            text = "Looking left"
        elif gaze.is_center():
            text = "Looking center"

        print(text)
        cv2.putText(frame, text, (90, 60), cv2.FONT_HERSHEY_DUPLEX, 1.6, (147, 58, 31), 2)

        # position of the pupil (x,y)
        left_pupil = gaze.pupil_left_coords()
        right_pupil = gaze.pupil_right_coords()
        cv2.putText(frame, "Left pupil:  " + str(left_pupil), (90, 130), cv2.FONT_HERSHEY_DUPLEX, 0.9, (147, 58, 31), 1)
        cv2.putText(frame, "Right pupil: " + str(right_pupil), (90, 165), cv2.FONT_HERSHEY_DUPLEX, 0.9, (147, 58, 31), 1)           

        return frame

    def _facialSlope(self,landmarks):
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

        return pts

    def _analyze(self, frame):
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        faces = self.detector(gray)
        for face in faces:
            x1 = face.left()
            y1=face.top()
            x2=face.right()
            y2=face.bottom()

            # 얼굴 인식된 부분 사각형으로 표시
            #cv2.rectangle(frame, (x1,y1),(x2,y2),(0,255,0),3)

            landmarks = self.predictor(gray, face)
                
            for n in range(0,68):
                x = landmarks.part(n).x
                y = landmarks.part(n).y

                # face landmark 출력
                cv2.circle(frame,(x,y),3,(255,0,0),-1)
            
            frame = self._gazeTracking(frame)
            pts = self._facialSlope(landmarks)
            cv2.polylines(frame, [pts], True, (255,0,0),3)

        return frame
