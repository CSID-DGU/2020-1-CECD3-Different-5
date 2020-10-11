from analysisVideo import AnalyzeVideo

analyze = AnalyzeVideo()

fname = 'image/hand3.jpg'

# analyze._analyzeFace(fname)
# analyze._analyzePose(fname)
analyze._analyzeHand(fname)

# from database.connect import Database
# import datetime
# db = Database()

# args = (datetime.datetime.now(),1,0,0,1,0)
# db.insertData(args)
# db.selectAllData()