# BraiNet

BraiNet (Thought-ID) is an Android application used to unlock a mobile device using brainwave signals. 
The application uses an adaptive offloading algorithm to determine using either a fog server or a cloud server running the authentication service. The authentication service trains a machine learning model using different registration signals. 
Moreover, it returns whether the user is authenticated by testing the user's brain signal against the model. 
The data corresponding to each registered is stored in a database during the first execution of the application. 
When a user is trying to authenticate, the respective data will be sent to the cloud/fog server in JSON format and the result is obtained by running Machine Learning Algorithms (Naive Bayes, Logistic Regression, Linear SVM) on them. 
The Adaptive Offloading algorithm will determine which server should be used to offload

Setup
1. Install python 2.7.14
2. Install the following libraries:
  a. Pandas
  b. Sci-kit Learn
  c. Flask

Instruction to run
1. To start Server type python [app | fog_app].py


File Details
1. app.py is the cloud server code
2. fog_app.py is the fog server code
3. processedTransformedData.csv contains the transformed data for 10 users
4. AndroidProject folder contains the code for the android app
5. Preprocessing.ipynb contains the data pre-processing and the machine learning 
