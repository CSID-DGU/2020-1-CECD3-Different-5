import cv2
import dlib
import numpy as np
import os
import datetime
from eye_slope import EyeandSlope
from database.connect import Database
from emotion import Emotion

class AnalyzeVideo(object):

    def __init__(self):
        self.total_focus = []   #다섯 프레임 단위로 [시선, 눈깜빡임, 기울기, 손, 다섯 프레임 동안의 대표 감정, score(100점 만점)]
        self.EMOTIONS = ["Angry","Disgusting","Fearful","Happy","Sad","Surprising","Neutral","NoPerson"]
        self.moment_focus = [[] for _ in range(5)]  #각 요소마다 한 프레임씩 상태 저장(5프레임마다 초기화 됨), 0=비집중 / 1=집중, [[시선], [눈깜빡임], [기울기], [손], [감정]]
        self.total_emotions = [0] * len(self.EMOTIONS)  #각 프레임의 대표 감정 확인
        self.count_info = [0] * 4   #각 요소마다 비집중 횟수 카운트 [시선회피횟수, 졸음시간, 자세불량횟수, 산만함횟수]
        self.face = EyeandSlope()
        self.emotion = Emotion()
        self.check_5sec = 0

    def _analyzeFace(self, fname, p):
        frame = cv2.imread(fname, cv2.IMREAD_GRAYSCALE)

        result = self.face._analyze(frame, fname)
        if result :
            for i, sub_result in enumerate(result) : self.moment_focus[i].append(sub_result)
            emotion = self.emotion._analyze(frame)
            self.moment_focus[4].append(emotion)
            self.total_emotions[emotion] += 1
        else : 
            for i, sub_result in enumerate(result) : self.moment_focus[i].append(0)
            self.moment_focus[4].append(7)
            self.total_emotions[-1] += 1

        os.remove(fname)

        self.check_5sec += 1
        if self.check_5sec > 5 :
            self._score5moment()
            self.check_5sec = 0

    def _score5moment(self) :
        emotion = [0] * len(self.EMOTIONS)
        for i in range(5) :
            # 다섯 프레임에 나타난 감정 카운트
            emotion[self.moment_focus[4][i]] += 1

        if not sum(self.moment_focus[1]) : self.total_focus.append([0, 0, 0, 0, emotion.index(max(emotion)), 0])    #졸고 있으면 무조건 전체 비집중
        else :
            gaze = sum(self.moment_focus[0])
            blink = 5
            slope = sum(self.moment_focus[2])
            hand = sum(self.moment_focus[3])

            self.total_focus.append([gaze, blink, slope, hand, emotion.index(max(emotion)), (gaze+blink+slope+hand)*5])
            self.count_info[1] += 5     #졸음 시간 카운트
        
        if len(self.total_focus) >= 2 :     #만약 이전 테이블이 존재한다면 비집중->집중의 상태 변화 확인으로 횟수 카운트
            if self.total_focus[-2][0] != 5 and self.total_focus[-1][0] == 5 : self.count_info[0] += 1
            if self.total_focus[-2][2] != 5 and self.total_focus[-1][2] == 5 : self.count_info[2] += 1
            if self.total_focus[-2][3] != 5 and self.total_focus[-1][3] == 5 : self.count_info[3] += 1

        self.moment_focus = [[] for _ in range(5)]

    def _break(self, start_time) :
        total_time = datetime.datetime.now()-start_time
        maximum_emotion = self.total_emotions.index(max(self.total_emotions))

        if len(self.total_focus) >= 2 :    #만약 마지막에 비집중->집중의 상태변화가 없어 비집중 횟수 카운트가 안 됐는지 확인
            if self.total_focus[-2][0] != 5 and self.total_focus[-1][0] != 5 : self.count_info[0] += 1
            if self.total_focus[-2][2] != 5 and self.total_focus[-1][2] != 5 : self.count_info[2] += 1
            if self.total_focus[-2][3] != 5 and self.total_focus[-1][3] != 5 : self.count_info[3] += 1
        elif len(self.total_focus) == 1 :   #만약 5프레임 이상 10프레임 미만으로 찍혔을 때 비집중 횟수 카운트
            if self.total_focus[-1][0] != 5 : self.count_info[0] += 1
            if self.total_focus[-1][2] != 5 : self.count_info[2] += 1
            if self.total_focus[-1][3] != 5 : self.count_info[3] += 1

        total_score = 0

        for score in self.total_focus :
            total_score += score[-1]
        if len(self.total_focus) : total_score /= len(self.total_focus)

        return total_time, maximum_emotion, "%06.2f" % total_score