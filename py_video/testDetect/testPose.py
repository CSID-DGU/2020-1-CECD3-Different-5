import cv2
from pose import OpenPose

#webcam = cv2.VideoCapture(0)
pose = OpenPose()
fname = './sample3.jpg'
while True:
    #ret, frame = webcam.read()

    frame = cv2.imread(fname, cv2.IMREAD_COLOR)
    frame = pose.inputImg(frame)

    cv2.imshow("Frame", frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

#webcam.release()
cv2.destroyAllWindows()
