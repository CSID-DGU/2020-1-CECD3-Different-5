import cv2
import dlib
import numpy as np
import os
#import datetime
from eye_slope import EyeandSlope
from emotion import Emotion
# from pose import OpenPose
from hand import Hand

fname = 'image/hand3.jpg'

class AnalyzeVideo(object):

    def __init__(self):
        self.total_focus = []
        self.moment_focus = [[] for _ in range(4)]
        self.face = EyeandSlope()
        self.emotion = Emotion()
    #     self.pose = OpenPose()
        self.hand = Hand()

    def _analyzeHand(self, fname):

        while True:
            
            frame = cv2.imread(fname, cv2.IMREAD_COLOR)
            frame = self.hand.inputImg(frame)

            cv2.imshow(fname,frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

        cv2.destroyAllWindows()

    def _analyzeFace(self, fname, p):
        check_5sec = 0

        while True:
            frame = cv2.imread(fname, cv2.IMREAD_GRAYSCALE)

            canvas = np.zeros((250,300,3),dtype="uint8")

            result = self.face._analyze(frame)
            for i, sub_result in enumerate(result) : self.moment_focus[i].append(sub_result)
            frame, canvas = self.emotion._analyze(frame, canvas)

            cv2.imwrite("Frame"+str(p)+".jpg",frame)
            cv2.imwrite("Propotion"+str(p)+".jpg",canvas)

            # os.remove(fname)

            check_5sec += 1
            if check_5sec > 5 :
                self._score5moment()
                check_5sec = 0

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
                
        cv2.destroyAllWindows()

    # def _analyzePose(self, fname):

    #     while True:
    #         frame2 = cv2.imread(fname, cv2.IMREAD_COLOR)
    #         frame2 = self.pose.inputImg(frame2)

    #         cv2.imshow("Pose Frame",frame2)

    #         if cv2.waitKey(1) & 0xFF == ord('q'):
    #             break
    #     cv2.destroyAllWindows()

    def _score5moment(self) :
        gaze = sum(self.moment_focus[0])
        blink = 5 if sum(self.moment_focus[1]) > 0 else 0
        slope = sum(self.moment_focus[2])
        # hand = sum(self.moment_focus[3])

        self.total_focus.append([gaze, blink, slope])
        print(self.total_focus)

        self.moment_focus = [[] for _ in range(4)]