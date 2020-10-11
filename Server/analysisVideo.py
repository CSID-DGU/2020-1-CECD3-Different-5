import cv2
import dlib
import numpy as np
#import datetime
from eye_slope import EyeandSlope
from emotion import Emotion
# from pose import OpenPose
from hand import Hand

fname = 'image/hand3.jpg'

class AnalyzeVideo(object):

    def __init__(self):
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

        while True:
            frame = cv2.imread(fname, cv2.IMREAD_GRAYSCALE)

            canvas = np.zeros((250,300,3),dtype="uint8")

            frame = self.face._analyze(frame)
            frame, canvas = self.emotion._analyze(frame, canvas)

            cv2.imwrite("Frame"+str(p)+".jpg",frame)
            cv2.imwrite("Propotion"+str(p)+".jpg",canvas)

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

