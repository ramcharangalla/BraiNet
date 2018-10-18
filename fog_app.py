#!flask/bin/python
from flask import Flask, jsonify, request
import json
import pandas as pd
from sklearn.svm import LinearSVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression
#import mysql.connector
from sklearn.metrics import accuracy_score
import numpy as np
app = Flask(__name__)

errorCodes = {
    "err1" : 'No User data found'
}
sampleSize = 40
runTimes = 10
def get_db_creds(f):

    with open(f) as f:
        dbCreds = json.load(f)

    return dbCreds["password"], dbCreds["db_name"], dbCreds["host"], dbCreds["user"]

def queryUserData(user):
    #Pulling only the first 40 samples as the remaining 40 will be used for testing
    #Query = "SELECT * FROM processedTransformedData WHERE userId = " + str(user) + ";"
    #PASSWORD, DATABASE_NAME, HOST, USER = get_db_creds("dbCreds.json")

    cnx, cursor, userDf = None, None, None
    err = None
    try:
        #cnx = mysql.connector.connect(user=USER, password=PASSWORD, host=HOST, database=DATABASE_NAME)
        #cursor = cnx.cursor()
        #cursor.execute(Query)
        #data = cursor.fetchall()
        #if not len(data):
            #raise ValueError(errorCodes["err1"])
        #fieldNames = [i[0] for i in cursor.description]
        data = pd.read_csv("processedTransformedData.csv")
        data1= data.loc[data['userId'] == (user)]
        userDf = pd.DataFrame(data1)
        #userDf.columns = fieldNames
	userDf = userDf.sample(sampleSize * runTimes)
        #userDf = pd.read_csv('transformedDataSet_S001R01.csv').iloc[40:, :]
    except Exception as e:
        print "There was an error in fetching the user information", e.message
        err = e.message
    finally:
        if cursor is not None:
            cursor.close()
        if cnx is not None:
            cnx.close()
    return err, userDf
'''
Models to try
1. Random Forest
2. Naive Bayes
3. Logistic Regression
'''
models = {
    'Linear SVM': LinearSVC(C = 0.01),
    'naiveBayes': GaussianNB(),
    'logisticRegression': LogisticRegression(C = 0.01)
}

class PredictionModel(object):
    def __init__(self, modelType):
        self.mdl = models[modelType]

    def runModel(self, storedData, receivedData):
        #storedData = storedData.drop(["user_id"], 1)
        results = []
        for x in range(runTimes):
            st_x = storedData.sample(sampleSize).drop(['userId'], 1)
            re_x = receivedData.sample(sampleSize)[st_x.columns]
            print 'Stored Columns', st_x.columns
	    print 'Recieved Columns', re_x.columns
	    train_x = st_x.append(re_x)
            train_y = [1]*st_x.shape[0] + [0]*re_x.shape[0]
            self.mdl.fit(train_x, train_y)
            pred_y = self.mdl.predict(train_x)
            results.append((np.array(train_y) == pred_y).mean())
        return np.array(results).mean()


@app.route('/prediction/<int:user_id>', methods=['POST'])
def get_prediction(user_id):
    receivedData = request.json
    mdls = ['Linear SVM', 'naiveBayes', 'logisticRegression']
    err, userData = queryUserData(user_id)

    if err is not None:
        for errType in errorCodes.keys():
            if errorCodes[errType] == err:
                return jsonify({"Message": err}), 201
        return jsonify({"Error": err}), 500
    userData = userData.sample(sampleSize)
    #Sampling the data recieved to remove temporal dependency
    receivedData = pd.DataFrame(receivedData)
    authResult = {}
    for mdlType in mdls:
        mdl = PredictionModel(mdlType)
        score = mdl.runModel(userData, receivedData)
        print 'The Score is', score, mdlType
        mdlAuth = True if score > 0.45 and score < 0.57 else False
        authResult[mdlType] = mdlAuth

    #data = filter(lambda x: x['userId'] == user_id, sampleData)
    return jsonify({'userID': user_id, 'auth': authResult}), 201
'''
@app.errorhandler(400)
def bad_request(error):
    return make_response(jsonify(request.json),400)
'''
'''
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)
'''
@app.route('/')
def index():
    return "Hello, use /prediction endpoint to get the prediction"

if __name__ == '__main__':
    app.run(debug = True, host="0.0.0.0", port=5000)
