from analysisVideo import AnalyzeVideo

analyze = AnalyzeVideo()

fname = 'image/sample3.jpg'
# analyze._analyzeHand(fname)
analyze._analyzeFace(fname, 0)
# from database.connect import Database
# import datetime
# db = Database()

# args = (datetime.datetime.now(),1,0,0,1,0)
# db.insertData(args)

# db.selectAllData()