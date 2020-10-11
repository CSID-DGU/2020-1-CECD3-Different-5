import os
import cv2
import numpy as np
from keras.models import load_model
from keras.preprocessing.image import img_to_array

class Emotion(object):

    def __init__(self):

        cwd = os.path.abspath(os.path.dirname(__file__))

        #emotion detector
        model_path = os.path.abspath(os.path.join(cwd, "trained_models/haarcascade_frontalface_default.xml"))
        self.emotion_detector = cv2.CascadeClassifier(model_path)

        #emotion model
        self.emotion_classifier=load_model('trained_models/emotion_model.hdf5',compile=False)

    def _analyze(self,frame):
        # gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Face detection in frame
        faces = self.emotion_detector.detectMultiScale(frame,
                                                scaleFactor=1.1,
                                                minNeighbors=5,
                                                minSize=(30,30))
                
        # 얼굴이 인식되면 감정 인식
        if len(faces) > 0:
            # 가장 큰 이미지
            face = sorted(faces, reverse=True, key=lambda x: (x[2] - x[0]) * (x[3] - x[1]))[0]
            (fX, fY, fW, fH) = face # 얼굴 시작 x, y좌표, width, height
            # 이미지 사이즈 조정 for neural network
            roi = frame[fY:fY + fH, fX:fX + fW]
            roi = cv2.resize(roi, (48, 48))
            roi = roi.astype("float") / 255.0
            roi = img_to_array(roi)
            roi = np.expand_dims(roi, axis=0)
                        
            # Emotion predict
            preds = self.emotion_classifier.predict(roi)[0]
            # emotion_probability = np.max(preds) #대표하는 감정

            return preds.argmax()

        return -1