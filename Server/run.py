#from analysisVideo import AnalyzeVideo
from database.connect import Database
from datetime import datetime
#analyze = AnalyzeVideo()
db = Database()
fname = 'image/sample3.jpg'

# analyze._analyzeFace(fname)
# analyze._analyzePose(fname)
#analyze._analyzeHand(fname)

# frame별로 분석한 데이터 저장
args=(1,0,0,0,0)
db.insertTmpResData(args)


# 학생의 하나의 영상 테이블 생성
studentID = "AAA"
today = datetime.today().strftime("%Y%m%d")
args = (studentID,today)
cnt = int(db.checkCnt(args))
cnt=cnt+1
args = (studentID,today,cnt)
db.createOneVideo(args)


# 학생의 하나의 영상에 대한 데이터 삽입
tabName = studentID+"_"+today+"_"+str(cnt-1)
print(tabName)
data = (4,5,3,2,5,19)
args = (tabName,data)
db.insertOneVideo(args)


# 학생 한명의 모든 영상 저장하는 테이블 생성
args=studentID
db.createStuTab(args)


# 최종 분석 결과 저장
# data = (timestamp, round, totalTime, score, feedback) # 해당 데이터 나중에 넣어주기
args = (studentID, data) # round 넣을 때, 1회차, 2회차로 들어가도록 처리하기
db.insertFinalRes(args)
