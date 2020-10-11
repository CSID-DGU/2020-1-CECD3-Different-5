# 눈깜빡임, 시선, 얼굴 기울기 인식

import dlib
import cv2
import os
import numpy as np
import math
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
        result_blink = 1
        result_gaze = 1

        if gaze.is_blinking() or gaze.is_right() or gaze.is_left() or gaze.is_center() or gaze.is_up():
            if gaze.is_blinking() : result_blink = 0
            result_gaze = 0

        # cv2.putText,[
        #     (frame, text, (90, 60), cv2.FONT_HERSHEY_DUPLEX, 1.6, (147, 58, 31), 2)
        # ]
        # # position of the pupil (x,y)
        # left_pupil = gaze.pupil_left_coords()
        # right_pupil = gaze.pupil_right_coords()
        # cv2.putText(frame, "Left pupil:  " + str(left_pupil), (90, 130), cv2.FONT_HERSHEY_DUPLEX, 0.9, (147, 58, 31), 1)
        # cv2.putText(frame, "Right pupil: " + str(right_pupil), (90, 165), cv2.FONT_HERSHEY_DUPLEX, 0.9, (147, 58, 31), 1)           

        return result_gaze, result_blink

    def _facialSlope(self,landmarks):
        # facial slope 측정을 위한 삼각형
        eye1_x=eye1_y=eye2_x=eye2_y=mouth_x=mouth_y=0
        result_slope = 0

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
                
        # pts = np.array([[eye1_x,eye1_y],[eye2_x,eye2_y],[mouth_x,mouth_y]], np.int32)
        # pts = pts.reshape((-1,1,2))

        # 눈이 인식되고 기울기를 구할 수 있음
        if (eye2_x-eye1_x) :
            eye_slope = (eye2_y-eye1_y) / (eye2_x-eye1_x) # 눈이 이루는 기울기
            print("eye_slope : ", eye_slope)
            # 어깨와 평행이 되는지 인식하는 코드 추가 필요
            # 평행하다면 몸 자체가 기울어져 있는 건 아닌지
            result_slope = self._calculateAngle([(eye2_x+eye1_x)/2, (eye2_y+eye1_y)/2], [mouth_x,mouth_y], [[eye1_x,eye1_y],[eye2_x,eye2_y]])

        return result_slope

    def _analyze(self, frame):
        faces = self.detector(frame)
        result = []
        for face in faces:
            x1 = face.left()
            y1=face.top()
            x2=face.right()
            y2=face.bottom()

            # 얼굴 인식된 부분 사각형으로 표시
            #cv2.rectangle(frame, (x1,y1),(x2,y2),(0,255,0),3)

            landmarks = self.predictor(frame, face)
                
            # for n in range(0,68):
            #     x = landmarks.part(n).x
            #     y = landmarks.part(n).y

            #     # face landmark 출력
            #     cv2.circle(frame,(x,y),3,(255,0,0),-1)
            
            result.extend(list(self._gazeTracking(frame)))
            result.append(self._facialSlope(landmarks))
            # cv2.polylines(frame, [pts], True, (255,0,0),3)

        return result

    def _calculateAngle(self, center, mouth, eyes) :
        # vector_left = [eyes[0][0]-center[0], eyes[0][1]-center[1]]
        # vector_right = [eyes[1][0]-center[0], eyes[1][1]-center[1]]
        # vector_mouse = [mouth[0]-center[0], mouth[1]-center[1]]
        # left_length = math.sqrt(vector_left[0]**2 + vector_left[1]**2)
        # right_length = math.sqrt(vector_right[0]**2 + vector_right[1]**2)
        # mouth_length = math.sqrt(vector_mouse[0]**2 + vector_mouse[1]**2)
        # print(vector_left, vector_right)
        # left_angle = abs(math.asin((vector_mouse[0]*vector_left[1]-vector_mouse[1]*vector_left[0])/(mouth_length*left_length)))*(180/math.pi)
        # right_angle = abs(math.asin((vector_mouse[0]*vector_right[1]-vector_mouse[1]*vector_right[0])/(mouth_length*right_length)))*(180/math.pi)
        
        dy = center[1]-mouth[1]
        dx = center[0]-mouth[0]
        angle = abs(math.atan(dy/dx))*(180/math.pi)

        if 90*0.7 <= angle <= 90*1.3 : return 1
        else : return 0