import cv2
import dlib
import numpy as np
import os
import datetime
from eye_slope import EyeandSlope
from emotion import Emotion
from database.connect import Database
class AnalyzeVideo(object):

    def __init__(self):
        self.total_focus = []
        self.EMOTIONS = ["Angry","Disgusting","Fearful","Happy","Sad","Surprising","Neutral","NoPerson"]
        self.moment_focus = [[] for _ in range(5)] # 고침(emotion 추가)
        self.total_emotions = [0] * len(self.EMOTIONS)
        self.face = EyeandSlope()
        self.emotion = Emotion()
        self.db = Database()
        self.check_5sec = 0

    def _analyzeFace(self, fname, p):
        frame = cv2.imread(fname, cv2.IMREAD_GRAYSCALE)

        result = self.face._analyze(frame, fname) # result = [gaze, blink, slope, hand]
        if result :
            for i, sub_result in enumerate(result) : self.moment_focus[i].append(sub_result)
            emotion = self.emotion._analyze(frame)
            self.moment_focus[4].append(emotion)
            self.total_emotions[emotion] += 1
        else : 
            for i, sub_result in enumerate(result) : self.moment_focus[i].append(0)
            self.moment_focus[4].append(7) # NoPerson
            self.total_emotions[-1] += 1

        os.remove(fname)

        self.check_5sec += 1
        if self.check_5sec > 5 :
            self._score5moment()
            self.check_5sec = 0

    def _score5moment(self) :
        if not sum(self.moment_focus[1]) : self.total_focus.append([0, 0, 0, 0])
        else :
            gaze = sum(self.moment_focus[0])
            blink = 5
            slope = sum(self.moment_focus[2])
            hand = sum(self.moment_focus[3])

            self.total_focus.append([gaze, blink, slope, hand])

        # 5 frame 분석 정보 tmpResult 테이블에 저장(emotion, blink, gaze, slope, hand 순서)
        for i in range(len(self.moment_focus[0])):
            args=(self.moment_focus[-1][i],self.moment_focus[1][i],self.moment_focus[0][i],self.moment_focus[2][i],self.moment_focus[3][i])
            db.insertTmpResData(args)

        self.moment_focus = [[] for _ in range(5)]


    def _break(self, start_time) :
        total_time = datetime.datetime.now()-start_time
        maximum_emotion = self.total_emotions.index(max(self.total_emotions))

        total_score = 0

        for score in self.total_focus :
            total_score += sum(score)*5
        if len(self.total_focus) : total_score /= len(self.total_focus)

        return total_time, self.EMOTIONS[maximum_emotion], "%.2f" % total_score