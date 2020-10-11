from analysisVideo import AnalyzeVideo

analyze = AnalyzeVideo()

fname = 'image/sample5.jpg'

analyze._analyzeFace(fname, 0)
# analyze._analyzePose(fname)

# from database.connect import Database
# import datetime
# db = Database()

# args = (datetime.datetime.now(),1,0,0,1,0)
# db.insertData(args)
# db.selectAllData()