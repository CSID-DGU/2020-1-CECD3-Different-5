# 눈깜빡임, 시선, 얼굴 기울기, 손 인식

import dlib
import cv2
import os
import numpy as np
import math
from gaze_tracking import GazeTracking
from hand import Hand

class EyeandSlope(object):

    def __init__(self):
        cwd = os.path.abspath(os.path.dirname(__file__))
        model_path = os.path.abspath(os.path.join(cwd, "trained_models/shape_predictor_68_face_landmarks.dat"))

        self.detector = dlib.get_frontal_face_detector()
        self.predictor = dlib.shape_predictor(model_path)
        self.hand = Hand()

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

        if (eye2_x-eye1_x) :
            if (eye2_y-eye1_y) / (eye2_x-eye1_x) < 0.4 :
                result_slope = self._calculateAngle([(eye2_x+eye1_x)/2, (eye2_y+eye1_y)/2], [mouth_x,mouth_y], [[eye1_x,eye1_y],[eye2_x,eye2_y]])

        return result_slope

    def _handTracking(self, fname) :
        return self.hand.inputImg(fname)

    def _analyze(self, frame, fname):
        faces = self.detector(frame)
        result = []
        for face in faces:
            x1 = face.left()
            y1=face.top()
            x2=face.right()
            y2=face.bottom()

            landmarks = self.predictor(frame, face)
            
            result.extend(list(self._gazeTracking(frame)))
            result.append(self._facialSlope(landmarks))
            
            hand_min_point = self._handTracking(fname)

            if hand_min_point != 1e9 and landmarks.part(8).y :
                if hand_min_point <= landmarks.part(8).y :
                    result.append(0)
                else : result.append(1)
            else : result.append(0)

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