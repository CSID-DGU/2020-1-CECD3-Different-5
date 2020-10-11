from analysisVideo import AnalyzeVideo

analyze = AnalyzeVideo()

<<<<<<< HEAD
fname = 'image/hand3.jpg'

# analyze._analyzeFace(fname)
# analyze._analyzePose(fname)
analyze._analyzeHand(fname)
=======
fname = 'image/sample5.jpg'

analyze._analyzeFace(fname, 0)
# analyze._analyzePose(fname)
>>>>>>> b1d6affefd4b43de8026bdad0f10a3b5f78445d7

# from database.connect import Database
# import datetime
# db = Database()

# args = (datetime.datetime.now(),1,0,0,1,0)
# db.insertData(args)
# db.selectAllData()