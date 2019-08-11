
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from skimage.color import lab2rgb
import sys
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import FunctionTransformer
import skimage
from sklearn.svm import SVC
from sklearn.preprocessing import MinMaxScaler, StandardScaler

'''
terminal command:
python3 weather_city.py monthly-data-labelled.csv monthly-data-unlabelled.csv labels.csv
'''



def main():
    data = pd.read_csv(sys.argv[1])
    #print(data)
    # TRAIN ON LABELED
    x_cols = data.columns
    x_cols = x_cols[2:]
    X = data[x_cols]
    #print(X)
    y = data['city'].values


    # TODO: create model
    X_train, X_valid, y_train, y_valid = train_test_split(X, y, test_size= 0.33, random_state= 42)

    # TODO: train svc_rgb_model
    svc_weather = make_pipeline(
        StandardScaler(),
        SVC(kernel='linear', C = 2.0)
    )
    svc_weather.fit(X_train, y_train)


    print(svc_weather.score(X_valid, y_valid))
    #print(svc_weather.predict(X_valid));
    #see what the model go wrong
    '''
    df = pd.DataFrame({'truth': y_valid, 'prediction': svc_weather.predict(X_valid)})
    print(df[df['truth'] != df['prediction']])
    '''

    # PREDICT ON UNLABELED
    data_unlabel = pd.read_csv(sys.argv[2])

    x_cols = data_unlabel.columns
    x_cols = x_cols[2:]
    X = data_unlabel[x_cols]
    predictions = svc_weather.predict(X)
    pd.Series(predictions).to_csv(sys.argv[3], index=False, header=False)




if __name__ == '__main__':
    main()
