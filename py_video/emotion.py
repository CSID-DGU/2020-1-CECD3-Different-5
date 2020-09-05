import os
import cv2
import numpy as np
from keras.models import load_model
from keras.preprocessing.image import img_to_array

class Emotion(object):
    
    def __init__(self):
        self.frame = None
        
        cwd = os.path.abspath(os.path.dirname(__file__))
        model_path = os.path.abspath(os.path.join(cwd,"trained_models/haarcascade_frontalface_default.xml"))

        self._emotion_detector = cv2.CascadeClassifier(model_path)
        self._emotion_classifier = load_model('trained_models/emotion_model.hdf5',compile=False)
        
    def _analyze(self, frame):
        self.frame = frame
        gray = cv2.cvtColor(self.frame,cv2.COLOR_BGR2GRAY)

        faces = self._emotion_detector.detectMultiScale(gray, 
                                                scaleFactor=1.1,
                                                minNeighbors=5,
                                                minSize=(30,30))

        if len(faces) > 0:
            # For the largest image
            face = sorted(faces, reverse=True, key=lambda x: (x[2] - x[0]) * (x[3] - x[1]))[0]
            (fX, fY, fW, fH) = face
            # Resize the image to 48x48 for neural network
            roi = gray[fY:fY + fH, fX:fX + fW]
            roi = cv2.resize(roi, (48, 48))
            roi = roi.astype("float") / 255.0
            roi = img_to_array(roi)
            roi = np.expand_dims(roi, axis=0)
            
            # Emotion predict
            preds = self._emotion_classifier.predict(roi)[0]
            emotion_probability = np.max(preds)

            EMOTIONS=["Angry","Disgusting","Fearful","Happy","Sad","Surprising","Neutral"]
            label = EMOTIONS[preds.argmax()]
            
            # # Assign labeling
            # cv2.putText(frame, label, (fX, fY - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.45, (0, 0, 255), 2)
            # cv2.rectangle(frame, (fX, fY), (fX + fW, fY + fH), (0, 0, 255), 2)
        
        else:
            emotion_probability = None
            label = None
            fX=fY=fH=fW=None

        return emotion_probability, label, fX, fY, fH, fW